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


/**
 * Client class for a simple client-server application
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Client extends Thread {
	private static final String USAGE
        = "usage: java week7.cmdchat.Client <name> <address> <port>";
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private int port;
	private String name; 
	private InetAddress host;

	public static void main(String args[]) throws IOException {
		Client client = new Client();
		client.startTUI();
	}
	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client()
			throws IOException {
		//sock = new Socket(host, port);
		//this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
		//this.out = new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream()));


	}
	public void startTUI() {
		ClientTUI tui = new ClientTUI(this);
		tui.start();
	}
	
	public void initializePort(int port) {
		if(port < 0) {
			System.out.println("not valid!");
		} else {
		this.port = port;
		System.out.println("Port is:  " + port);
		}

	}
	public void initializeName(String name) {
		if(name.length() < 2) {
			System.out.println("not Valid!");
		} else {
		this.clientName = name;
			System.out.println("Name is:  " + name);
		}
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
					System.out.println(line);
				}
		} catch (IOException e) {
			System.out.println("connection lost!");
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
	

	/**
	 * Reads the messages in the socket connection. Each message will
	 * be forwarded to the MessageUI
	
	public void run() {
		String nextLine;
		try {
			while ((nextLine = this.in.readLine()) != null) {
				System.out.println(nextLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}



	public void shutdown() {
		print("Closing socket connection...");
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getClientName() {
		return clientName;
	}
	
	private static void print(String message){
		System.out.println(message);
	}
	
	public static String readString(String tekst) {
		System.out.print(tekst);
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}
	*/
}