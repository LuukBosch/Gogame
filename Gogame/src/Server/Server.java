package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
/**
 * The Threaded class server is responsible for accepting clients 
 * and assigning these clients to a specific game. The amount of players in
 * a game is always two. 
 * @author luuk.bosch
 *
 */


public class Server extends Thread {
	private int port;
	private int gameid = 0;
	private HashMap<Integer, GameHandler> gameNumber_games;
	private boolean gameAvailable;
	private GameHandler game;
	private ServerSocket serverSocket;

	public static void main(String args[]) {
		Server test = new Server();
		test.start();
	}
	
	/**
	 * For the creation of the server a port number is asked to the user;
	 * If a port number is given a server socket is created. 
	 */
	public Server() {
		initializePort();
		gameNumber_games = new HashMap<Integer, GameHandler>();

	}
	
	public int getPort() {
		return port;
	}
	/**
	 * Ask the user for a port number. Stops if a valid int is given. 
	 */
	private void initializePort() {
		int value = -1;
		Scanner line = new Scanner(System.in);
		do {
			System.out.print("Enter port number: ");
			try (Scanner scannerLine = new Scanner(line.nextLine());) {
				if (scannerLine.hasNextInt()) {
					value = scannerLine.nextInt();
				}
			}
		} while (!createPort(value));
		port = value;
		line.close();
		System.out.println("listening on port: " + port);
	}

	/**
	 * Tries to Create a server socket using the port given by the user. If this
	 * fails a new port is asked and an other attempt is made until it succeeds.
	 */
	public boolean createPort(int port) {
		boolean result;
		if (port <= 0) {
			result = false;
		} else {
			try {
				serverSocket = new ServerSocket(port);
				result = true;
			} catch (IOException e) {
				System.out.println("Not a valid port, try again");
				result = false;

			}
		}
		return result;

	}

	/**
	 * Creates sockets for incoming clients. 
	 */
	public void run() {
		while (true) {
			Socket sock;
			try {
				System.out.println("Listening!");
				sock = serverSocket.accept();
				System.out.println("connected to new Client!");
				addToGame(sock);
			} catch (IOException e) {
				System.out.println("Connection failed");
			}
		}
	}

	/**
	 * Adds the incomming clients to a game. If no game is available a new instance of Game is created. 
	 * Games are given a GameId and are stored in a list
	 * @param sock Socket 
	*/
	

	public void addToGame(Socket sock) {
		if (gameAvailable == false) {
			try {
				game = new GameHandler(gameid, this);
				gameNumber_games.put(gameid, game);
				ClientHandler clientHandler = new ClientHandler(game, sock);
				clientHandler.start();
				game.addFirstPlayer(clientHandler);
				gameAvailable = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ClientHandler clientHandler = new ClientHandler(gameNumber_games.get(gameid), sock);
				clientHandler.start();
				game.addSecondPlayer(clientHandler);
				gameAvailable = false;
				gameid++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void removeGame(GameHandler game) {
		if (gameNumber_games.containsValue(game)) {
			System.out.println("Game " + game.getGame().getid() + " is finished and has been removed");
			gameNumber_games.remove(game.getGame().getid());
		}
	}

}
