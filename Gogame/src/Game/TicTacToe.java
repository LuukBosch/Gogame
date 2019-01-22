package Game;

/**
 * Executable class for the game Tic Tac Toe. The game can be played against the
 * computer. Lab assignment Module 2
 * 
 * @author Theo Ruys
 * @version $Revision: 1.4 $
 */
public class TicTacToe {
    public static void main(String[] args) {
        HumanPlayer luuk = new HumanPlayer("luuk", 1);
        HumanPlayer pieter = new HumanPlayer("pieter", 2);
    	Game spel = new Game(luuk, pieter);
        spel.start();
    }
}
