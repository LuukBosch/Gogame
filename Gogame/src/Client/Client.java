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

import Game.Constants;

/**
 * Client class for a simple client-server application
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class Client extends Thread {
	private static final String USAGE = "usage: java week7.cmdchat.Client <name> <address> <port>";
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private int port;
	private String name;
	private InetAddress host;
	private int gameID;
	private int color;
	private GoGuiIntegrator gogui;
	ClientTUI tui;

	public static void main(String args[]) throws IOException {
		Client client = new Client();
		client.startTUI();
	}

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client() throws IOException {

	}

	public void startTUI() {
		tui = new ClientTUI(this);
		tui.start();
	}

	public void initializePort(int port) {
		if (port < 0) {
			System.out.println("not valid!");
		} else {
			this.port = port;
			System.out.println("Port is:  " + port);
		}

	}

	public void initializeName(String name) {
		if (name.length() < 2) {
			System.out.println("not Valid!");
		} else {
			this.clientName = name;
			System.out.println("Name is:  " + name);
		}
	}

	public String getPlayerName() {
		return clientName;
	}


	public void initializeIP(String ip) {
		try {
			host = InetAddress.getByName(ip);
			System.out.println("Adress is:  " + host.toString());
		} catch (UnknownHostException e) {
			System.out.println("ERROR: no valid hostname!");

		}

	}

	public void startGame() {
		try {
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			System.out.println("socket build");
		} catch (IOException e) {
			System.out.println("connection failed!");
		}
	}

	public void run() {
		String line = null;
		try {
			while ((line = in.readLine()) != null) {
				handleMessage(line);
			}
		} catch (IOException e) {
			System.out.println("connection lost!");
		}
	}
	public int getGameId() {
		return gameID;
	}

	public synchronized void handleMessage(String message) {
		String[] messagesplit = message.split("\\" + Constants.DELIMITER);
		if (messagesplit[0].equals(Constants.ACKNOWLEDGE_HANDSHAKE)) {
			gameID = Integer.parseInt(messagesplit[1]);
			tui.handShakeAcknowledged(Integer.valueOf(messagesplit[2]));
		} else if (messagesplit[0].contentEquals(Constants.ACKNOWLEDGE_MOVE)) {
			String[] gamestatus = messagesplit[3].split(";");
			String board = gamestatus[2];
			drawBoard(board);;
			if(Integer.valueOf(gamestatus[1]) == color) {
				tui.getMove();
			}
		} else if (messagesplit[0].equals(Constants.ACKNOWLEDGE_CONFIG)) {
			color = Integer.parseInt(messagesplit[2]);
			gogui = new GoGuiIntegrator(false, true, Integer.parseInt(messagesplit[3]));
			gogui.startGUI();
			String[] gamestatus = messagesplit[4].split(";");
			if(Integer.valueOf(gamestatus[1])==color) {
				tui.getMove();
			}
		} else if (messagesplit[0].equals(Constants.INVALID_MOVE)) {
			tui.invalidMove();
			tui.getMove();
		} else if (messagesplit[0].equals(Constants.GAME_FINISHED)) {	
			System.out.println(message);
		} else {
			System.out.println(message);
		}
	}

	public void drawBoard(String board) {
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

}