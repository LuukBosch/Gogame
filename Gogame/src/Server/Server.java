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
	private boolean gameAvailable;
	private int gameid = 0;
	private Game game;
	private ServerSocket serverSocket;

	public static void main(String args[]) {
		Server test = new Server();
		test.start();
	}

	public Server() {
		initializePort();
		createPort();
		gameNumber_games = new HashMap<Integer, Game>();
		
	}

    private void initializePort() {
        int value = 0;
        boolean intRead = false;
        Scanner line = new Scanner(System.in);
        do {
            System.out.print("Enter port number: ");
            try (Scanner scannerLine = new Scanner(line.nextLine());) {
                if (scannerLine.hasNextInt()) {
                    intRead = true;
                    value = scannerLine.nextInt();
                }
            }
        } while (!intRead);
        port = value;
    }
    
    public void createPort() {
    	try {
			serverSocket = new ServerSocket(port);
    	} catch (IOException e) {
    		System.out.println("Not a valid port, try again");
    		initializePort();
    		createPort();
		}
    	
    }
	public void run() {
		
			while (true) {
				
				Socket sock;
				try {
					System.out.println("Listening!");
					sock = serverSocket.accept();
					System.out.println("connected to new Client!");
					addToGame(sock);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		
	}
	
	
	public int getPort() {
		return port;
	}
	

	public void addToGame(Socket sock) {
		if(gameAvailable == false) {
			try {	
			game = new Game(gameid);
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

}
