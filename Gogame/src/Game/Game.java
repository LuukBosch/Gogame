package Game;

import java.util.Scanner;

import com.nedap.go.gui.GoGuiIntegrator;




public class Game {

    public static final int NUMBER_PLAYERS = 2;
    private Board board;
    private Player[] players;
    private int passes;
    private History history;


    public Game(Player s0, Player s1) {
        board = new Board(19);
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        passes = 0;
        history = new History();
    }

    public void start() {
        boolean doorgaan = true;
        while (doorgaan) {
            play();
            doorgaan = readBoolean("\n> Play another time? (y/n)?", "y", "n");
        }
    }


	private boolean readBoolean(String prompt, String yes, String no) {
        String answer;
        Scanner in;
        do {
            System.out.print(prompt);
            in = new Scanner(System.in);
            answer = in.hasNextLine() ? in.nextLine() : null;
        } while (answer == null || (!answer.equals(yes) && !answer.equals(no)));
        return answer.equals(yes);
    }

    private void play() {
    	
    	while(passes != 2) {
    	int choice = players[0].makeMove(board);
    	while(!MoveValidator.isValidMove(board, choice, players[0].getcolor(), history)) {
    		choice = players[0].makeMove(board);
    	}
    	this.update(choice, players[0]);
    	history.addSituation(board.getStringRepresentation());
    	if(passes != 2) {
    		int choice2 = players[1].makeMove(board);
        	while(!MoveValidator.isValidMove(board, choice2, players[1].getcolor(), history)) {
        		choice2 = players[1].makeMove(board);
        	}
        	this.update(choice2, players[1]);
        	history.addSituation(board.getStringRepresentation());
    	}
    	}
    	this.printResult();
    	
        
    }

    private void update(int choice, Player player) {
    	if(choice == -1) {
    		passes++;
    	} else {
    		int col =  choice%board.getSize();
    		int row = (choice - col)/board.getSize();
    		board.setField(row, col, player.getcolor());
    		EnforceRule.apply(board, player.getcolor());
    		
    	}
        System.out.println(board.toString());
    }

    private void printResult() {
    	Score.apply(board);
        System.out.println("einde spel");
    }
    
    public Player otherPlayer(Player player) {
    	if (player == players[0]) {
    		return players[1];
    	} else {
    		return players[0];
    	}
    }
}
