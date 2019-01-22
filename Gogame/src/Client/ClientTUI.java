package Client;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;


public class ClientTUI{
		Client client;
		Scanner in;
		
		ClientTUI(Client client){
			this.client = client;
			this.in = new Scanner(System.in);
		}
		
		public void start() {
			int choice = -1;
			while (choice != 6) {
				displayMenu();
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
			System.out.println("Send message!!!...................5");
			System.out.println("Exit..............................6");
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
				String Text = readStringWithPrompt("enter a name:  ");
				client.sendMessage(Text);
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
