package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import Game.Board;
import Game.Constants;

public class Server extends Thread {
	private int port;
	private HashMap<Integer, Game> gameNumber_games;
	private HashMap<String, Integer> client_GameID;
	private HashMap<Integer, Game> gameID_Game;
	private boolean gameAvailable;
	private boolean waitingforConfig;
	private int gameNumber = 0;
	private Game game;

	public static void main(String args[]) {
		Server test = new Server();
		test.start();
	}

	public Server() {
		port = initializePort();
		//clients = new HashSet<ClientHandler>();
		client_GameID = new HashMap<String, Integer>();
		gameID_Game = new HashMap<Integer, Game>();
		gameNumber_games = new HashMap<Integer, Game>();
		
	}

	public int initializePort() {
		int desiredport = 0;
		try (Scanner in = new Scanner(System.in)) {
			while (true) {
				System.out.println("Provide a port number:  ");
				desiredport = in.nextInt();
				if (desiredport < 0) {
					System.out.println("Not a valid port number");
				} else {
					return desiredport;
				}
			}

		}

	}

	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				System.out.println("Listening!");
				Socket sock = serverSocket.accept();
				System.out.println("connected to new Client!");
				addToGame(sock);
			}
		} catch (IOException e) {
			System.out.println("not a valid port!");
		}

	}
	
	
	public int getPort() {
		return port;
	}
	
	public void removeGame(Game game) {
		
	}
	
	public void addToGame(Socket sock) {
		if(gameAvailable == false) {
			try {	
			game = new Game();
			gameNumber_games.put(gameNumber, game);
			ClientHandler clientHandler = new ClientHandler(game, sock);
			clientHandler.start();
			game.addFirstPlayer(clientHandler);

			gameAvailable = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {	
			ClientHandler clientHandler = new ClientHandler(gameNumber_games.get(gameNumber), sock);
			clientHandler.start();
			game.addSecondPlayer(clientHandler);
			gameAvailable = false;
			gameNumber++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
