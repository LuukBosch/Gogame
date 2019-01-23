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
	/**
	public void assignToGame(ClientHandler client) {
		
		if(gameAvailable == true) {
			while(waitingforConfig) {
			}
			addToGame(client);
			} else {
			sendAcknowledgeHandshake(client, gameID, true);
			createNewGame(client);
			gameAvailable = true;
		}
		
	}
	
	public void createNewGame(ClientHandler handler) {
		client_GameID.put(handler.getPlayerName(), gameID);
		handler.sendMessage(Constants.REQUEST_CONFIG+Constants.DELIMITER+Constants.REQUEST_CONFIG_MESSAGE);
		waitingforConfig = true;
	}
	
	public void createBoard(ClientHandler client, int size, int color) {
		Game game = new Game(size);
		game.addFirstPlayer(client, color);
		client.getName();
		int id = client_GameID.get(client.getPlayerName());
		gameID_Game.put(id, game);
		waitingforConfig = false;
		System.out.println("WAITNG FOR OP");
	}
	
	public void addToGame(ClientHandler client) {
		client_GameID.put(client.getPlayerName(), gameID);
		gameID_Game.get(gameID).addSecondPlayer(client);
		
		client.sendMessage(Constants.ACKNOWLEDGE_HANDSHAKE+Constants.DELIMITER+gameID+Constants.DELIMITER+false);//TODO overbodig????
		client.sendMessage(Constants.ACKNOWLEDGE_CONFIG+
				Constants.DELIMITER+client.getPlayerName()+
				Constants.DELIMITER+gameID_Game.get(gameID).getColor(client)+
				Constants.DELIMITER+gameID_Game.get(gameID).getSize()+
				Constants.DELIMITER+gameID_Game.get(gameID).getGameState()+
				Constants.DELIMITER+gameID_Game.get(gameID).getOpponentColor(client));
		
		ClientHandler opponent = gameID_Game.get(gameID).getOpponent(client);
		
		opponent.sendMessage(Constants.ACKNOWLEDGE_CONFIG+
				Constants.DELIMITER+opponent.getPlayerName()+
				Constants.DELIMITER+gameID_Game.get(gameID).getColor(opponent)+
				Constants.DELIMITER+gameID_Game.get(gameID).getSize()+
				Constants.DELIMITER+gameID_Game.get(gameID).getGameState());//+
		gameAvailable = false;
	}
	
	public void handleMove(ClientHandler client, String[] message) {
		Game game = gameID_Game.get(Integer.parseInt(message[1]));
		int move = Integer.parseInt(message[3]);
		if(game.isTurn(client)) {
			if(game.isValidMove(move, client)) {
				game.playMove(move, client);
				client.sendMessage(Constants.ACKNOWLEDGE_MOVE+Constants.DELIMITER+message[1]+Constants.DELIMITER+move+Constants.DELIMITER+game.getGameState());
				ClientHandler opponent = game.getOpponent(client);
				opponent.sendMessage(Constants.ACKNOWLEDGE_MOVE+Constants.DELIMITER+message[1]+Constants.DELIMITER+move+Constants.DELIMITER+game.getGameState());
			} else {
				client.sendMessage(Constants.INVALID_MOVE+"not a valid move!");
			}
		} else {
			client.sendMessage(Constants.INVALID_MOVE+"Not your turn!");
		}
		//TODO sent confirmation!
	}
	
	public void sendAcknowledgeHandshake(ClientHandler client, int gameid, boolean isleader) {
		client.sendMessage(Constants.ACKNOWLEDGE_HANDSHAKE+Constants.DELIMITER+gameid+Constants.DELIMITER+isleader);
	}
	*/
}
