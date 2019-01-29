package Game;
/**
 * Class containing the static function that is responsible for removing the stones from the board. 
 * @author luuk.bosch
 *
 */
public class EnforceRule {

	/**
	 * Checks for a specific board which groups needs to be removed.
	 * Since this depends on the color of the last played stone this is a 
	 * parameter of the function. 
	 * @param board
	 * @param color
	 */
	public static void enforceRules(Board board, int color) {
		int colorcur = 0;
		int colorop = 0;
		if (color == Constants.BLACK) {
			colorcur = Constants.BLACK;
			colorop = Constants.WHITE;
		}
		if (color == Constants.WHITE) {
			colorcur = Constants.WHITE;
			colorop = Constants.BLACK;
		}
		check(board, colorop);
		check(board, colorcur);
	}
	
	/**
	 * Checks if a group is present with zero liberties and consists out of stones of a specific color. 
	 * if a group has zero liberties it is removed. 
	 * @param board
	 * @param color
	 */
	public static void check(Board board, int color) {
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (!board.isEmptyField(i, j)) {
					Group groep = board.getField(i, j).getGroup();
					if (groep.getLiberties().size() == 0 && groep.getColor() == color) {
						board.getField(i, j).getGroup().getCaptured();
					}
				}
			}
		}
		
	}
}