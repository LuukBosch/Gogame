package Client;
import java.util.Scanner;

import Game.Board;
import Game.History;
	public class HumanPlayer extends Player {

	    public HumanPlayer() {
	    }

	    
	    public int determineMove(Board board, History history, int color) {
	        int choice = readInt("what is your choice? ");
	        return choice;
	    }

	   
	    private int readInt(String prompt) {
	        int value = 0;
	        boolean intRead = false;
	        @SuppressWarnings("resource")
	        Scanner line = new Scanner(System.in);
	        do {
	            System.out.print(prompt);
	            try (Scanner scannerLine = new Scanner(line.nextLine());) {
	                if (scannerLine.hasNextInt()) {
	                    intRead = true;
	                    value = scannerLine.nextInt();
	                }
	            }
	        } while (!intRead);
	        return value;
	    }

		}


