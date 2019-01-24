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
			while (choice != 3) {
				displayMenu();
				choice = readIntWithPrompt("Enter choice:");
				executeChoice(choice);
			}
			
			
		}

		private void displayMenu(){
			System.out.println();
			System.out.println("Enter the number denoting the action");
			System.out.println("ENTER PORT NUMBER.................1");
			System.out.println("SET HOST ADRESS...................2");
			System.out.println("SEND HANDSHAKE....................3");
		}
		
		
		private void executeChoice(int choice) {
			if(choice == 1) {
				int port = readIntWithPrompt("Enter a port:  ");
				client.initializePort(port);
			} else if(choice == 2) {
				String adress = readStringWithPrompt("Enter a host adress:  ");
				client.initializeIP(adress);
			} else if(choice == 3) {
				String name = readStringWithPrompt("what is your name?");
				client.startGame();
				client.start();
				client.sendMessage(Constants.HANDSHAKE+Constants.DELIMITER+name);
				} 
		}
		
		public void handShakeAcknowledged(boolean isLeader) {
			if(isLeader) {
				System.out.println("You are the leader!");
				int size = readIntWithPrompt("What is your prefered size:   ");
				int color = readIntWithPrompt("What is your preferred color:  ");
				client.sendMessage(Constants.SEND_CONFIG+Constants.DELIMITER+color+Constants.DELIMITER+size);
			} else {
				System.out.println("you are not the leader");
			}
		}
		
		public void getMove() {
			int move = readIntWithPrompt("what is your next move?");
			client.sendMessage(Constants.MOVE+Constants.DELIMITER+1+Constants.DELIMITER+client.getPlayerName()+Constants.DELIMITER+move);
			
			}
		public void invalidMove() {
			System.out.println("you played a invalid move!");
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
			return input;
		}

	}
