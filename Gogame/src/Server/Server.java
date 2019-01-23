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
	
	
	/**
	public void HandleIncommingMesg(ClientHandler client, String message) {
		System.out.println(message);
		String[] messagesplit = message.split("\\" + Constants.DELIMITER);
		if(messagesplit[0].equals(Constants.HANDSHAKE)) {
			String name = messagesplit[1];
			client.setPlayerName(name);
			assignToGame(client);
		} else if(messagesplit[0].equals(Constants.SEND_CONFIG)) {
			System.out.println(message + "  requestconfig");
			int color = Integer.parseInt(messagesplit[1]);
			int size = Integer.parseInt(messagesplit[2]);
			System.out.println(client.getPlayerName()+ "In choice menu");
			createBoard(client, size, color);
		} else if(messagesplit[0].equals(Constants.MOVE)) {
			handleMove(client, messagesplit);
		} else if(messagesplit[0].equals(Constants.EXIT)){
			
		} else {
			System.out.println(message);
		}
	}
	*/
	/**
	public void addHandler(ClientHandler handler) {
		clients.add(handler);
		
	}
	
	public void removeHandler(ClientHandler handler) {
		clients.remove(handler);
	}
	*/
	public int getPort() {
		return port;
	}
	
	public void addToGame(Socket sock) {
		if(gameAvailable == false) {
			try {	
			Game game = new Game();
			gameNumber_games.put(gameNumber, game);
			ClientHandler client;
			client = new ClientHandler(game, sock);
			game.addPlayer(client);
			client.start();
			gameAvailable = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		} else {
			try {	
			ClientHandler client = new ClientHandler(gameNumber_games.get(gameNumber), sock);
			client.start();
			gameAvailable = false;
			gameNumber++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
