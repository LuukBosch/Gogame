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
    private Game game;
    private BufferedReader in;
    private BufferedWriter out;
    private String playername;
    private Socket sock;

    /**
     * Constructs a ClientHandler object
     * Initialises both Data streams.
     */
    //@ requires serverArg != null && sockArg != null;
    public ClientHandler(Game serverArg, Socket sockArg) throws IOException {
        this.game = serverArg;
        this.sock = sockArg;
        this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream()));
        
    }

    /**
     * Reads the name of a Client from the input stream and sends 
     * a broadcast message to the Server to signal that the Client
     * is participating in the chat. Notice that this method should 
     * be called immediately after the ClientHandler has been constructed.
     */

    
    public void setPlayerName(String name) {
    	playername = name;
    }
    
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
            while ((nextLine = this.in.readLine()) != null) {
                game.HandleIncommingMesg(this, nextLine);
            }
        } catch (IOException e) {
            //game.removeHandler(this);;
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
            e.printStackTrace();
        }
    }

    /**
     * This ClientHandler signs off from the Server and subsequently
     * sends a last broadcast to the Server to inform that the Client
     * is no longer participating in the chat. 
     */
    private void shutdown() {
        //server.removeHandler(this);
    }
}
