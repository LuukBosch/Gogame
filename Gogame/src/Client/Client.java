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

/**
 * Client class for a simple client-server application
 * 
 * @author Theo Ruys
 * @version 2005.02.21
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
	private History history;
	private Board bord;
	private boolean connectionSuccessful;

	public static void main(String args[]) throws IOException {
		Client client = new Client();
	}

	public Client() {
		history = new History();
		tui = new ClientTUI(this);
		tui.start();
	}


	public History getHistory() {
		return history;
	}

	public void initializePort(int port) {
		if (port < 0) {
			System.out.println("not valid!");
		} else {
			this.port = port;
			System.out.println("Port is:  " + port);
		}

	}

	public void createPlayer(int choice) {
		if (choice == 1) {
			player = new HumanPlayer();
		} else {
			player = new ComputerPlayer();
		}

	}
	public Player getPlayer() {
		return player;
	}

	public void initializeName(String name) {
		if (name.length() < 1 || name.contains("+")) {
			System.out.println("not Valid!");
		} else {
			this.clientName = name;
			System.out.println("Name is:  " + name);
		}
	}

	public String getPlayerName() {
		return clientName;
	}
	public Board getBoard() {
		return bord;
	}


	public void initializeIP(String ip) {
		try {
			host = InetAddress.getByName(ip);
			System.out.println("Adress is:  " + host.toString());
		} catch (UnknownHostException e) {
			System.out.println("ERROR: no valid hostname!");

		}

	}

	public void buildSocket() {
		try {
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			System.out.println("socket build");

		} catch (IOException e) {
			System.out.println("connection failed!");
			connectionSuccessful = false;

		}
	}

	public void startGame() {
		buildSocket();
		if(connectionSuccessful) {
		start();
		sendMessage(Constants.HANDSHAKE + Constants.DELIMITER + getPlayerName());
		}else {
		tui.start();
		}
	}

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
	
	public int getGameId() {
		return gameID;
	}
	public int getColor() {
		return color;
	}

	public synchronized void handleMessage(String message) {
		System.out.println(message);
		String[] messagesplit = message.split("\\" + Constants.DELIMITER);
		if (messagesplit[0].equals(Constants.ACKNOWLEDGE_HANDSHAKE)) {
			gameID = Integer.parseInt(messagesplit[1]);
			tui.handShakeAcknowledged(Integer.valueOf(messagesplit[2]));
		} else if (messagesplit[0].contentEquals(Constants.ACKNOWLEDGE_MOVE)) {
			addToHistory(messagesplit);
			playOwnBoard(messagesplit);
			drawBoard(messagesplit);

			if(Integer.valueOf(messagesplit[3].split(";")[1]) == color) {
				tui.getMove();
			}
		} else if (messagesplit[0].equals(Constants.ACKNOWLEDGE_CONFIG)) {
			acknowledgeConfig(messagesplit);
			startGui(messagesplit);
			createBoard(messagesplit);
			if(Integer.valueOf(messagesplit[4].split(";")[1])==color) {
				tui.getMove();
			}
		} else if (messagesplit[0].equals(Constants.INVALID_MOVE)) {
			tui.invalidMove(messagesplit);
			tui.getMove();
		} else if (messagesplit[0].equals(Constants.GAME_FINISHED)) {	
			System.out.println(message);
		} else {
			System.out.println(message);
		}
	}
	public void acknowledgeConfig(String[] message){
		color = Integer.parseInt(message[2]);
		
	}
	public void createBoard(String[] message) {
		bord = new Board(Integer.valueOf(message[3]));
		String[] gamestatus = message[4].split(";");
		String board = gamestatus[2];
		history.addSituation(board);
	}
	
	public void startGui(String[] message) {
		gogui = new GoGuiIntegrator(false, true, Integer.parseInt(message[3]));
		gogui.startGUI();
	}

	
	public void addToHistory(String[] message){
		String[] gamestatus = message[3].split(";");
		String board = gamestatus[2];
		history.addSituation(board);
	}

	public void playOwnBoard(String[] message) {
		String movestring = message[2];
		int move = Integer.valueOf(movestring.split(";")[0]);
		int color = Integer.valueOf(movestring.split(";")[1]);
		if(move != -1) {
		int col = move % bord.getSize();
		int row = (move - col)/bord.getSize();
		bord.setField(row, col, color);
		EnforceRule.apply(bord, color);
		}
		
	}
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
	
	public void printBoard(String[] board) {
		int size = board.length;
		int width = (int) Math.sqrt(size);
		int count = 0;
		for (int i = 0; i < size; i++) {
			System.out.print(board[i]);
			count++;
			if(count == width) {
				System.out.println("");
				count = 0;
			}
			
		}
	}

	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
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

