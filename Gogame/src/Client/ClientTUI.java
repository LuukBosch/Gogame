package Client;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import Game.Constants;

public class ClientTUI {
	Client client;
	Scanner in;
	String playername;
	boolean portcheck = false, adresscheck = false, playercheck = false;
	int port;
	String adress;
	String player = "Computerplayer";

	ClientTUI(Client client) {
		this.client = client;
		this.in = new Scanner(System.in);
	}

	public void start() {
		System.out.println("  / ____|/ __ \\| |\n" + 
			            	" | |  __| |  | | |\n" + 
				            " | | |_ | |  | | |\n" + 
			               	" | |__| | |__| |_|\n" + 
			            	"  \\_____|\\____/(_)\n" + 
		                  		"                ");
		int choice = -1;
		while (choice != 4) {
			displayMenu();
			choice = readIntWithPrompt("Enter choice:");
			executeChoice(choice);
		}
	}

	private void displayMenu() {
		System.out.println();
		System.out.println(" Enter the number denoting the action");
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("▐  ENTER PORT NUMBER.................1 ▍");
		System.out.println("▐  SET HOST ADRESS...................2 ▍");
		System.out.println("▐  Create Player.....................3 ▍");
		System.out.println("▐  SEND HANDSHAKE....................4 ▍");
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.println("");
		checklist();
	}

	private void executeChoice(int choice) {
		if (choice == 1) {
			port = readIntWithPrompt("Enter port number:  ");
			client.initializePort(port);
			portcheck = true;
		} else if (choice == 2) {
			adress = readStringWithPrompt("Enter host Adress:  ");
			client.initializeIP(adress);
			adresscheck = true;
		} else if (choice == 3) {
			int keuze = 0;
			client.createPlayer(keuze);
			playercheck = true;
		} else if (choice == 4) {
			if (playercheck && adresscheck && portcheck) {
				String name = readStringWithPrompt("Enter a name please:  ");
				client.initializeName(name);
				client.startGame();
			} else {
				System.out.println("Fill in all required fields.");
				start();
			}
		} else {
			System.out.println("not a valid input!");
		}
	}

	public void handShakeAcknowledged(int isLeader) {
		if (isLeader == 1) {
			System.out.println("You are the leader!");
			int size = readIntWithPrompt("What is your prefered size:   ");
			int color = readIntWithPrompt("What is your preferred color:  ");
			client.sendMessage(Constants.SET_CONFIG + Constants.DELIMITER + color + Constants.DELIMITER + size);
		} else {
			System.out.println("you are not the leader");
		}
	}

	public void getMove() {
		int move = client.getPlayer().determineMove(client.getBoard(), client.getHistory(), client.getColor());
		System.out.println("move played is: " + move);
		client.sendMessage(Constants.MOVE + Constants.DELIMITER + client.getGameId() + Constants.DELIMITER
				+ client.getPlayerName() + Constants.DELIMITER + move);

	}

	public void invalidMove(String[] message) {
		System.out.println("you played a invalid move because, " + message[1]);
	}

	public void failed() {

	}

	private int readIntWithPrompt(String prompt) {
		int value = 0;
		boolean intRead = false;
		Scanner line = new Scanner(System.in);
		do {
			System.out.print(prompt);
			try (Scanner scannerLine = new Scanner(line.nextLine());) {
				if (scannerLine.hasNextInt()) {
					intRead = true;
					value = scannerLine.nextInt();
				} else {
					System.out.println("not a valid input!");
				}
			}

		} while (!intRead);
		return value;
	}

	private String readStringWithPrompt(String prompt) {
		String text = "";
		boolean intRead = false;
		Scanner line = new Scanner(System.in);
		do {
			System.out.print(prompt);
			try (Scanner scannerLine = new Scanner(line.nextLine());) {
				if (scannerLine.hasNext()) {
					intRead = true;
					text = scannerLine.next();
				} else {
					System.out.println("not a valid input!");
				}
			}
		} while (!intRead);
		return text;
	}

	public void checklist() {
		boolean[] listchecks = new boolean[] { portcheck, adresscheck, playercheck };
		String[] names = new String[] { "  Port ", "Host adress ", "Player " };
		String list = "";
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		for (int i = 0; i < listchecks.length; i++) {
			if (listchecks[i] == true) {
				list += (names[i] + "\u2705" + "   ");
			} else {
				list += (names[i] + "\u274C" + "   ");
			}
		}
		System.out.println(list);
		System.out.println(" ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ ");
		System.out.println("  Port is:          " + port);
		System.out.println("  Host adress is:   " + adress);
		System.out.println("  Player is:        " + player);
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	}

}
