package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class ClientHandler extends Thread {
    private GameHandler game;
    private BufferedReader in;
    private BufferedWriter out;
    private String playername;
    private Socket sock;

    /**
     * Constructs a ClientHandler object
     * Initialises both Data streams.
     *@ requires server != null && sock != null;
     */
    
    public ClientHandler(GameHandler serverArg, Socket sockArg) throws IOException {
        this.game = serverArg;
        this.sock = sockArg;
        this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream()));
        
    }

    /**
     * Sets the name for the clienthandler
     * @param name Name for the clienthandler
     */
    public void setPlayerName(String name) {
    	playername = name;
    }
    
    
    /**
     * Returns the Clienthandlers playername
     * @return playername
     */
    public String getPlayerName() {
    	return playername;
    }
    
    
    /**
     * This method takes care of sending messages from the Client.
     * Every message that is received, is preprended with the name
     * of the Client, and the new message is offered to the Server
     * for broadcasting. If an IOException is thrown while reading
     * the message, the method concludes that the socket connection is
     * broken and shutdown() will be called.
     */
    
    public void run() {
        String nextLine;
        try {
            while ((nextLine = in.readLine()) != null) {
                game.HandleIncommingMesg(this, nextLine);
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * This method can be used to send a message over the socket
     * connection to the Client. If the writing of a message fails,
     * the method concludes that the socket connection has been lost
     * and shutdown() is called.
     */
    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
  
        } catch (IOException e) {
        	shutdown();
            e.printStackTrace();
        }
    }

    /**
     *shuts down the clienthandler
     */
	private void shutdown() {
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
