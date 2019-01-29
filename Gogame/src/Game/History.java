package Game;

import java.util.ArrayList;
import java.util.List;
/**
 * Simple class containing the history of a Go Game.
 * This class is used to control the super ko rule in go. 
 * @author luuk.bosch
 *
 */
public class History {
	/**
	 * Contains a List contain all the previous board states of a played go game. 
	 */
	List<String> history;
	
	/**
	 * Creates a fresh history. 
	 */
	public History() {
		history = new ArrayList<>();
	}
	
	/**
	 * adds a string representation of a board to the history. 
	 * @param board
	 */
	public void addSituation(String board) {
		history.add(board);
	}
	
	/**
	 * Returns the history of game. 
	 * @return
	 */
	public List<String> getHistory() {
		return history;
	}

	/**
	 * Returns the amount of previous board positions. 
	 * @return
	 */
	public int getSize() {
		return history.get(0).length();
	}
	
}
