package Client;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import Game.Constants;


public class ClientTUI{
		Client client;
		Scanner in;
		
		ClientTUI(Client client){
			this.client = client;
			this.in = new Scanner(System.in);
		}
		
		public void start() {
			int choice = -1;
			while (choice != 4) {
				displayMenu();
				choice = readIntWithPrompt("Enter choice:");
				executeChoice(choice);
			}
			
			while (choice != 8) {
				displayMenu2();
				choice = readIntWithPrompt("Enter choice:");
				executeChoice(choice);
			}
		}

		private void displayMenu(){
			System.out.println();
			System.out.println("Enter the number denoting the action");
			System.out.println("ENTER NAME........................1");
			System.out.println("ENTER PORT NUMBER.................2");
			System.out.println("SET HOST ADRESS...................3");
			System.out.println("START A GAME......................4");
		}
		
		public void displayMenu2() {
			System.out.println();
			System.out.println("Enter the number denoting the action");
			System.out.println("Send Handshake.....................5");
			System.out.println("SEND Config........................6");
			System.out.println("PlayMove...........................7");
			System.out.println("Exit...............................8");
			
		}
		
		
		private void executeChoice(int choice) {
			if(choice == 1) {
				String name = readStringWithPrompt("enter a name:  ");
				client.initializeName(name);
			} else if(choice == 2) {
				int port = readIntWithPrompt("Enter a port:  ");
				client.initializePort(port);
			} else if(choice == 3) {
				String adress = readStringWithPrompt("Enter a host adress:  ");
				client.initializeIP(adress);
			} else if(choice == 4) {
				System.out.println("Preparing Game");
				client.startGame();
				client.start();
			}else if(choice == 5) {
				String name = readStringWithPrompt("enter a name:  ");
				client.sendMessage(Constants.HANDSHAKE+Constants.DELIMITER+name);
			}else if(choice == 6) {
				int color  = readIntWithPrompt("What is your preffered color:  ");
				int size  = readIntWithPrompt("What is your preffered size:    ");
				client.sendMessage(Constants.SEND_CONFIG+Constants.DELIMITER+color+Constants.DELIMITER+size);
			} else if(choice == 7) {
				int move  = readIntWithPrompt("What is your preffered move:  ");
				client.sendMessage(Constants.MOVE+Constants.DELIMITER+client.getGameid()+Constants.DELIMITER+client.getPlayerName()+Constants.DELIMITER+move);
			}
			else {
				System.out.println("choice not valid!");
			}
		}
		
		
		
		private int readIntWithPrompt(String prompt) {
			System.out.println(prompt);
			System.out.flush();
			while (!in.hasNextInt()) {
				System.out.println(prompt);
				System.out.flush();
			}
			int input = in.nextInt();
			in.nextLine();
			return input;
			
		}
		
		private String readStringWithPrompt(String prompt) {
			System.out.println(prompt);
			System.out.flush();
			while (!in.hasNext()) {
				System.out.println(prompt);
				System.out.flush();
			}
			String input = in.next();
			in.nextLine();
			return input;
		}

	}
