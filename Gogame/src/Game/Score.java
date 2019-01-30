package Game;


import java.util.Set;
/**
 * The Score class contains the static functions that are able to calculate scores after a game of Go.
 * @author luuk.bosch
 *
 */


public class Score {

	/**
	 * To calculate the score for a specific color
	 * the amount of stones and the captured areas are needed.
	 * @param board
	 * @param color
	 * @return
	 */
	public static int getScore(Board board, int color) {
		setEmptytoNumber(board);
		Board copy1 = board.getDeepcopy();
		int stones = getAmountofStones(copy1, color);
		if (color == Constants.WHITE) {
			removeStones(copy1, Constants.BLACK);
		} else {
			removeStones(copy1, Constants.WHITE);
		}
		
		return getAreaScore(copy1) + stones;
	}

	/**
	 * Sets all the empty field with a empty marker creating groups of empty intersections. 
	 * @param board
	 */
	public static void setEmptytoNumber(Board board) {
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.isEmptyField(i, j)) {
					board.setField(i, j, Constants.EMPTY);
				}
			}
		}
	}

	/**
	 * Removes the stones of a specific color from the board. 
	 * @param board
	 * @param color
	 */
	public static void removeStones(Board board, int color) {
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (!board.isEmptyField(i, j)) {
					if (board.getField(i, j).getGroup().getColor() == color) {
						Set<Intersection> whitestones = board.getField(i, j).getGroup().getStones();
						for (Intersection whitestone : whitestones) {
							whitestone.setGroup(null);
							Set<Group> adjgroups = whitestone.getAdjGroups();
							for (Group adjgroup : adjgroups) {
								adjgroup.addLibertie(whitestone);
							}
						}
					}

				}
			}
		}
	}
	
	/**
	 * Counts the amount of stones of a specific color on a specific board. 
	 * @param board
	 * @param color
	 * @return
	 */
	public static int getAmountofStones(Board board, int color) {
		int score = 0;
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.getField(i, j).getGroup().getColor() ==  color) {
					score++;
				}
			}
		}
		return score;
	}
	
	/**
	 * Checks for all the empty areas on the board if they are surrounded by a single color.
	 * If this is the case the size of this captured area is added to the score. 
	 * returns the total score. 
	 * @param board
	 * @return
	 */
	
	public static int getAreaScore(Board board) {
		int score = 0;
		Boolean test = false;
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (!board.isEmptyField(i, j)) {
					test = true;
					if (board.getField(i, j).getGroup().getColor() == Constants.EMPTY
							&& board.getField(i, j).getGroup().getLiberties().size() == 0) {
						score += board.getField(i, j).getGroup().getStones().size();
						board.getField(i, j).getGroup().getCaptured();
					}
				}

			}
		}
		if(test) {
		return score;
		}else {
			return 0;
		}
		
	}
	
	
}