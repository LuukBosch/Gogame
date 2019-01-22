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
	private HashSet<ClientHandler> clients;
	private HashMap<String, Integer> client_GameID;
	private HashMap<Integer, Game> gameID_Game;
	private boolean gameAvailable;
	private boolean waitingforConfig;
	private int gameID = 0;

	public static void main(String args[]) {
		Server test = new Server();
		System.out.println(test.getPort());
		test.start();
	}

	public Server() {
		port = initializePort();
		clients = new HashSet<ClientHandler>();
		client_GameID = new HashMap<String, Integer>();
		gameID_Game = new HashMap<Integer, Game>();
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
				ClientHandler newHandler = new ClientHandler(this, sock);
				System.out.println("connected!");
				addHandler(newHandler);
				newHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	public void HandleIncommingMesg(ClientHandler client, String message) {
		String[] messagesplit = message.split("");
		if(messagesplit[0].equals(Constants.HANDSHAKE)) {
			String name = messagesplit[1];
			client.setPlayerName(name);
			assignToGame(client);
		} else if(messagesplit[0].equals(Constants.REQUEST_CONFIG)) {
			int color = Integer.parseInt(messagesplit[1]);
			int size = Integer.parseInt(messagesplit[2]);
			createBoard(client, size, color);
		} else if(messagesplit[0].equals(Constants.MOVE)) {
			handleMove(client, messagesplit);
		} else if(messagesplit[0].equals(Constants.EXIT)){
			//TODO			
		} else {
			System.out.println(message);
		}
	}
	
	public void addHandler(ClientHandler handler) {
		clients.add(handler);
		
	}
	
	public void removeHandler(ClientHandler handler) {
		clients.remove(handler);
	}
		
	

	public int getPort() {
		return port;
	}
	
	public void assignToGame(ClientHandler handler) {
		
		if(gameAvailable == true) {
			while(waitingforConfig) {
			}
			addToGame(handler);
			} else {
			handler.sendMessage(Constants.ACKNOWLEDGE_CONFIG+Constants.DELIMITER+gameID+Constants.DELIMITER+true);//TODO overbodig????
			createNewGame(handler);
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
		int id = client_GameID.get(client);
		gameID_Game.put(id, game);
		waitingforConfig = false;
	}
	
	public void addToGame(ClientHandler client) {
		client_GameID.put(client.getPlayerName(), gameID);
		gameID_Game.get(gameID).addSecondPlayer(client);
		client.sendMessage(Constants.ACKNOWLEDGE_CONFIG+
				Constants.DELIMITER+client.getPlayerName()+
				Constants.DELIMITER+gameID_Game.get(gameID).getColor(client)+
				Constants.DELIMITER+gameID_Game.get(gameID).getSize()+
				Constants.DELIMITER+gameID_Game.get(gameID).getGameState()+
				Constants.DELIMITER+gameID_Game.get(gameID).getOpponentColor(client));
		
		client.sendMessage(Constants.ACKNOWLEDGE_CONFIG+
				Constants.DELIMITER+client.getPlayerName()+
				Constants.DELIMITER+gameID_Game.get(gameID).getColor(client)+
				Constants.DELIMITER+gameID_Game.get(gameID).getSize()+
				Constants.DELIMITER+gameID_Game.get(gameID).getGameState()+
				Constants.DELIMITER+gameID_Game.get(gameID).getOpponentColor(client));
		
		
		gameAvailable = false;
	}
	
	public void handleMove(ClientHandler client, String[] message) {
		Game game = gameID_Game.get(Integer.parseInt(message[1]));
		int move = Integer.parseInt(message[3]);
		if(game.isTurn(client)) {
			if(game.isValidMove(move, client)) {
				game.playMove(move, client);
			}
		}
		//TODO sent confirmation!
	}
}

/**
 * 
 * // /** // // // Server server = new Server(Integer.parseInt(args[0])); //
 * server.run(); // // } // // // private int port; // private
 * List<ClientHandler> threads; // // /** Constructs a new Server object
 */
//    public Server(int portArg) {
//        this.port = portArg;
//        this.threads = new ArrayList<>();
//    }
//    
//    /**
//     * Listens to a port of this Server if there are any Clients that 
//     * would like to connect. For every new socket connection a new
//     * ClientHandler thread is started that takes care of the further
//     * communication with the Client.
//     */
//    public void run() {
//        try {
//            ServerSocket serverSocket = new ServerSocket(port);
//
//            while (true) {
//            	System.out.println("Listening!");
//                Socket sock = serverSocket.accept();
//                ClientHandler newHandler = new ClientHandler(this, sock);
//                addHandler(newHandler);
//                newHandler.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(0);
//        }
//    }
//    
//    public void print(String message){
//        System.out.println(message);
//    }
//    
//    /**
//     * Sends a message using the collection of connected ClientHandlers
//     * to all connected Clients.
//     * @param msg message that is send
//     */
//    public void broadcast(String msg) {
//        for (int i = 0; i < threads.size(); i++) {
//            threads.get(i).sendMessage(msg);
//        }
//    }
//    
//    /**
//     * Add a ClientHandler to the collection of ClientHandlers.
//     * @param handler ClientHandler that will be added
//     */
//    public void addHandler(ClientHandler handler) {
//        threads.add(handler);
//    }
//    
//    /**
//     * Remove a ClientHandler from the collection of ClientHanlders. 
//     * @param handler ClientHandler that will be removed
//     */
//    public void removeHandler(ClientHandler handler) {
//        threads.remove(handler);
//    }
//}
//*/