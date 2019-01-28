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


    public ClientHandler(Game serverArg, Socket sockArg) throws IOException {
        this.game = serverArg;
        this.sock = sockArg;
        this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream()));
        
    }

    
    public void setPlayerName(String name) {
    	playername = name;
    }
    
    public String getPlayerName() {
    	return playername;
    }

    public void run() {
        String nextLine;
        try {
            while ((nextLine = this.in.readLine()) != null) {
                game.HandleIncommingMesg(this, nextLine);
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }


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


	private void shutdown() {
		try {
			game.removePlayer(this);
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

}
