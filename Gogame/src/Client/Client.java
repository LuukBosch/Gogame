package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.nedap.go.gui.GoGuiIntegrator;

import Game.Board;
import Game.Constants;
import Game.EnforceRule;
import Game.History;
import static Game.EnforceRule.enforceRules;

/**
 * Class for creating a go playing client.
 * @author luuk.bosch
 *
 */
public class Client extends Thread {
	
	private int port;
	private int gameID;
	private int color;
	
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private InetAddress host;
	private GoGuiIntegrator gogui;
	private ClientTUI tui;
	private Player player;
	private Player hintPlayer;
	private History history;
	private Board bord;
	private boolean connectionSuccessful = true;

	public static void main(String args[]) throws IOException {
		new Client();
	}

	/**
	 * Creates a client and starts a Tui that can handle user input. 
	 */
	public Client() {
		history = new History();
		tui = new ClientTUI(this);
		tui.start();
	}

	/**
	 * Returns the history of the game the client is participating in. 
	 * @return History of the game the client is participating
	 */
	public History getHistory() {
		return history;
	}
	

	/**                        
	 * Checks if the given portnumber is a valid port number.
	 * if this is the case it sets the port
	 * @param port
	 */
	public void initializePort(int port) {
		if (port < 0 || port > 64535) {
			System.out.println("not valid!");
		} else {
			this.port = port;
			System.out.println("Port is:  " + port);
			tui.portset();
		}

	}
	
	/**
	 * initializes the ip address of the future host. 
	 * @param ip
	 */
	public void initializeIP(String ip) {
		try {
			host = InetAddress.getByName(ip);
			System.out.println("Adress is:  " + host.toString());
			tui.ipSet();
		} catch (UnknownHostException e) {
			System.out.println("ERROR: no valid hostname!");

		}

	}

	
	/**
	 * Creates either a Human or Computer player. 
	 * @param choice
	 */
	public void createPlayer(int choice) {
		if (choice == 1) {
			player = new HumanPlayer();
		} else {
			player = new ComputerPlayer();
		}

	}

	/**
	 * returns the player of the client
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	
	/**
	 * Initialize the name of the client. Name is not allow to contain +. 
	 * @param name
	 */
	public void initializeName(String name) {
		if (name.length() < 1 || name.contains("+")) {
			System.out.println("not Valid!");
		} else {
			this.clientName = name;
			System.out.println("Name is:  " + name);
		}
	}

	/**
	 * Returns the playername 
	 * @return 
	 */
	public String getPlayerName() {
		return clientName;
	}

	
	/**
	 * Returns to bord of the game the client is playing. 
	 * @return
	 */
	public Board getBoard() {
		return bord;
	}
	
	

	
	/**
	 * Tries to build a socket connection with a server
	 */
	public void buildSocket() {
		try {
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			System.out.println("socket build");

		} catch (IOException e) {
			System.out.println("connection failed!");
			connectionSuccessful = false;
			tui.start();

		}
	}

	/**
	 * Sends the first handshake to the server if a socket connection was build successfully.  
	 */
	public void startGame() {

		buildSocket();

		if (connectionSuccessful) {
			this.start();
			sendMessage(Constants.HANDSHAKE + Constants.DELIMITER + getPlayerName());
		}
	}

	/**
	 * Reads messages send by the server
	 */
	public void run() {
		String line = null;
		try {
			while ((line = in.readLine()) != null) {
				handleMessage(line);
			}
			shutdown();
		} catch (IOException e) {
			shutdown();
			System.out.println("connection lost!");
		}
	}

	
	/**
	 * returns gameid 
	 * @return
	 */
	public int getGameId() {
		return gameID;
	}

	/**
	 * returns the color of the player
	 * @return
	 */
	public int getColor() {
		return color;
	}
	
	
	/**
	 * Handles messages that are send by the server
	 * @param message
	 */
	public synchronized void handleMessage(String message) {
		System.out.println(message);
		try {
			String[] messagesplit = message.split("\\" + Constants.DELIMITER);
			if (messagesplit[0].equals(Constants.ACKNOWLEDGE_HANDSHAKE)) {
				gameID = Integer.parseInt(messagesplit[1]);
				tui.handShakeAcknowledged(Integer.valueOf(messagesplit[2]));
			} else if (messagesplit[0].contentEquals(Constants.ACKNOWLEDGE_MOVE)) {
				addToHistory(messagesplit);
				playOwnBoard(messagesplit);
				drawBoard(messagesplit);
				gogui.removeHintIdicator();
				if (Integer.valueOf(messagesplit[3].split(";")[1]) == color) {
					tui.getMove();
				}
			} else if (messagesplit[0].equals(Constants.ACKNOWLEDGE_CONFIG)) {
				acknowledgeConfig(messagesplit);
				startGui(messagesplit);
				createBoard(messagesplit);
				tui.acknowledgeConfig();
				if (Integer.valueOf(messagesplit[4].split(";")[1]) == color) {
					tui.getMove();
				}
			} else if (messagesplit[0].equals(Constants.INVALID_MOVE)) {
				tui.invalidMove(messagesplit);
				tui.getMove();
			} else if (messagesplit[0].equals(Constants.GAME_FINISHED)) {
				gogui.stopGUI();
			} else if (messagesplit[0].equals(Constants.REQUEST_REMATCH)) {
				tui.askRematch();
			} else if (messagesplit[0].equals(Constants.ACKNOWLEDGE_REMATCH)) {
				rematch(messagesplit[1]);
			} else {
				System.out.println(message + " is unknown");
			}
		} catch (NumberFormatException e) {
			System.out.println("Incomming message is not in the right format");
			tui.start2();
		}
	}

	
	/**
	 * sets the color of the player based upon the incomming message. 
	 * @param message
	 */
	public void acknowledgeConfig(String[] message) {
		color = Integer.parseInt(message[2]);

	}

	/**
	 * Creates a board that can be used to internally keep track of the game
	 * @param message
	 */
	public void createBoard(String[] message) {
		bord = new Board(Integer.valueOf(message[3]));
		String[] gamestatus = message[4].split(";");
		String board = gamestatus[2];
		history.addSituation(board);
	}

	/**
	 * Shutsdown the program if the user doesn't want to play a rematch 
	 * @param choice
	 */
	public void rematch(String choice) {
		if (choice.contentEquals("1")) {
		} else {
			shutdown();
		}
	}

	/**
	 * Starts the gui used to display the Game. 
	 * The size of the board is determined by the message of the server
	 * @param message
	 */
	public void startGui(String[] message) {
		gogui = new GoGuiIntegrator(false, true, Integer.parseInt(message[3]));
		gogui.startGUI();
	}

	/**
	 * Displays a hint on the board if the user requests this. 
	 */
	public void setHint() {
		hintPlayer = new ComputerPlayer();
		int hint = hintPlayer.determineMove(getBoard(), getHistory(), getColor());
		int col = hint % bord.getSize();
		int row = (hint - col) / bord.getSize();
		gogui.addHintIndicator(col, row);

	}
	
	
	/**
	 * Adds a game situation to the history. 
	 * @param message
	 */
	public void addToHistory(String[] message) {
		String[] gamestatus = message[3].split(";");
		String board = gamestatus[2];
		history.addSituation(board);
	}

	
	/**
	 * Plays a move on the client his own board. 
	 * @param message
	 */
	public void playOwnBoard(String[] message) {
		String movestring = message[2];
		int move = Integer.valueOf(movestring.split(";")[0]);
		int color = Integer.valueOf(movestring.split(";")[1]);
		if (move != -1) {
			int col = move % bord.getSize();
			int row = (move - col) / bord.getSize();
			bord.setField(row, col, color);
			enforceRules(bord, color);
		}

	}

	
	
	/**
	 * Draws a board send by the user on the gui. 
	 * @param message
	 */
	public void drawBoard(String[] message) {
		String[] gamestatus = message[3].split(";");
		String board = gamestatus[2];
		String[] stones = board.split("");
		int size = stones.length;
		int width = (int) Math.sqrt(size);
		gogui.clearBoard();
		for (int i = 0; i < size; i++) {
			int col = i % (width);
			int row = (i - col) / (width);
			if (stones[i].equals("1")) {
				gogui.addStone(col, row, false);
			} else if (stones[i].equals("2")) {
				gogui.addStone(col, row, true);
			}

		}
	}


	/**
	 * Sends a message to the server
	 * @param msg
	 */
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Shutsdown the programm. 
	 */
	private void shutdown() {
		try {
			sock.close();
			System.out.println("Connection with server lost!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

}
