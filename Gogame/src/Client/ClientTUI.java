package Client;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import Game.Constants;
/**
 * Responsible for providing a Tui for the client class.
 * @author luuk.bosch
 *
 */
public class ClientTUI {
	Client client;
	Scanner in;
	String playername;
	boolean portcheck = false, adresscheck = false, playercheck = false, leader, configNotSet = true, turn = false;
	int port;
	int playerChoice;
	int maxtime;
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
		System.out.println("Done, Waiting for reply!");
	}

	public void start2() {
	
			displayMenu2();
			int choice = readIntWithPrompt("Enter choice:");
			executeChoice2(choice);
			System.out.println("Doei");
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

	public void displayMenu2() {
		System.out.println();
		System.out.println("███╗   ███╗███████╗███╗   ██╗██╗   ██╗");
		System.out.println("████╗ ████║██╔════╝████╗  ██║██║   ██║");
	    System.out.println("██╔████╔██║█████╗  ██╔██╗ ██║██║   ██║");
	    System.out.println("██║╚██╔╝██║██╔══╝  ██║╚██╗██║██║   ██║");
	    System.out.println("██║ ╚═╝ ██║███████╗██║ ╚████║╚██████╔╝");		
	    System.out.println("╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝ ╚═════╝ ");
				                                      
				                         
		                        
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		if(leader && configNotSet) {
		System.out.println("▐  Set Game Configuration ............1 ▍");
		} else {
		System.out.println("▐  Set Game Configuration(disabled 🚫) ▍");
		} 
		if(turn && !configNotSet) {
		System.out.println("▐  Enter move.........................2 ▍");
		} else {
		System.out.println("▐  Enter move.............(disabled 🚫) ▍");
		}
		if(configNotSet) {
		System.out.println("▐  Get Hint...............(disabled 🚫) ▍");
			} else {
				System.out.println("▐  Get Hint...........................3 ▍");
			}
		System.out.println("▐  Exit...............................4 ▍");
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		
		
		System.out.println("┏━━━━━━━━━━━━━━━━┓");
		System.out.print("  Leader:     ");
		if(leader) {
			System.out.println("\u2705");
		} else {
			System.out.println("\u274C");
		}
		if(!configNotSet) {
		System.out.print("  Color:      ");
		if (client.getColor() == Constants.BLACK) {
		System.out.println("⚫");
		} else {
		System.out.println("⚪");
		}
		}
		System.out.println("┗━━━━━━━━━━━━━━━━┛");

	}

	private void executeChoice(int choice) {
		if (choice == 1) {
			port = readIntWithPrompt("Enter port number:  ");
			client.initializePort(port);
		} else if (choice == 2) {
			adress = readStringWithPrompt("Enter host Adress:  ");
			client.initializeIP(adress);
		} else if (choice == 3) {
			playerChoice = readIntWithPrompt("Do you want to play with Computer(0) or Human (1)");	
			maxtime = readIntWithPrompt("What is the maximum playing time(in seconds)?  ");	
			client.createPlayer(playerChoice, maxtime);

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
	private void executeChoice2(int choice) {
		if (choice == 1 && leader && configNotSet) {
			sendConfig(); 
		} else if (choice == 2 && turn && !configNotSet) {
			setMove();
		} else if (choice == 3) {
			client.setHint();
			start2();
		} else if (choice == 4) {
			exit();
		} else {
			System.out.println("not a valid input!");
			start2();
		}
	}

	public void handShakeAcknowledged(int isLeader) {
		System.out.println("Handshake acknowledged!");
		if(isLeader == 1) {
			leader = true;
			System.out.println("Is Leader!");
			start2();
		} else {
			leader = false;
			System.out.println("Is not Leader!");
		}

	}

	public void sendConfig() {
		int color = readIntWithPrompt("Enter desired color(Black = 1, White = 2):  ");
		int size = readIntWithPrompt("Enter desired size of board:  ");
		client.sendMessage(Constants.SET_CONFIG + Constants.DELIMITER + client.getGameId()+ Constants.DELIMITER + color + Constants.DELIMITER + size);
		configNotSet = false;
	}
	public void acknowledgeConfig() {
		configNotSet = false;
	}
	public void exit() {
		client.sendMessage(Constants.EXIT + Constants.DELIMITER + client.getGameId()+ Constants.DELIMITER +client.getPlayerName());
	}
	public void ipSet() {
		adresscheck = true;
	}
	
	public void portset() {
		portcheck= true;
	}
	
	public void playerSet() {
		playercheck = true;
	}
	
	public void setMove(){
		int move = client.getPlayer().determineMove(client.getBoard(), client.getHistory(), client.getColor(), maxtime);
		System.out.println("move played is: " + move);
		client.sendMessage(Constants.MOVE + Constants.DELIMITER + client.getGameId() + Constants.DELIMITER
				+ client.getPlayerName() + Constants.DELIMITER + move);
	}
	public void getMove() {
		System.out.println("you have the move!");
		if(playerChoice == 1) {
		turn = true;
		start2();
		}
		else {
			setMove();
		}
	}
	public void askRematch() {
		int choice = readIntWithPrompt("Do you want to play a rematch?(1 = yes, 0 = no)");
		if(choice == 1) {
			client.sendMessage(Constants.SET_REMATCH+Constants.DELIMITER+1);
		} else {
			client.sendMessage(Constants.SET_REMATCH+Constants.DELIMITER+0);
			}
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
