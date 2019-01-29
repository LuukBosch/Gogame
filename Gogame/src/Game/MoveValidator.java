package Game;
import static Game.EnforceRule.enforceRules;

/**
 * Checks if a move is a valid move regarding the go rules. 
 * @author luuk.bosch
 *
 */
public class MoveValidator {
	/**
	 * for the validation of a move the board, move, color and history are given. 
	 * A move is valid if it is a field on the board, and this field is empty
	 * and the move doesn't violate the super Ko rule. 
	 * @param board
	 * @param move
	 * @param color
	 * @param history
	 * @return
	 */
	public static boolean isValidMove(Board board, int move, int color, History history) {
		if(move == -1) {
			return true;
		}
		int col = move % board.getSize();
		int row = (move - col)/board.getSize();
		return isEmptyField(board, row, col) && isField(board, row, col) && checkSuperKo(board, history, color, row, col);
	}
	
	/*
	 * Checks if a field is empty. 
	 */
	public static boolean isEmptyField(Board board, int row, int col) {
		return board.isEmptyField(row, col);
		
	}
	
	/*
	 * Checks if a field is part of the Goban. 
	 */
	public static boolean isField(Board board, int row, int col) {
		return board.isField(row, col);
	}
	
	
	
	/*
	 * Plays the suggested move and checks if the result of this move 
	 * equals a previous board position in the played game. 
	 */
	public static boolean checkSuperKo(Board board, History history, int color, int row, int col) {
		Board copy = board.getDeepcopy();
		copy.setField(row, col, color);
		enforceRules(copy, color);
		for(String situation: history.getHistory()) {
			if (situation.equals(copy.getStringRepresentation())) {
				return false;
			}
		}
		return true;
	}

}