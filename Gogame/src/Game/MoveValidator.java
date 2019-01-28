package Game;
import static Game.EnforceRule.enforceRules;
public class MoveValidator {
	
	public static boolean isValidMove(Board board, int move, int color, History history) {
		if(move == -1) {
			return true;
		}
		int col = move % board.getSize();
		int row = (move - col)/board.getSize();
		return isEmptyField(board, row, col) && isField(board, row, col) && checkSuperKo(board, history, color, row, col);
	}
	
	public static boolean isEmptyField(Board board, int row, int col) {
		return board.isEmptyField(row, col);
		
	}
	
	public static boolean isField(Board board, int row, int col) {
		return board.isField(row, col);
	}
	
	
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