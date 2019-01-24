package Game;

import java.util.HashSet;
import java.util.Set;

public class Score {
	static final int EMPTY = 10;

	public static int apply(Board board, int color) {
		setEmptytoNumber(board);
		Board copy1 = board.getDeepcopy();
		int stones = getAmountofStones(copy1, color);
		if( color == Constants.WHITE) {
			removeStones(copy1, Constants.BLACK);
		}else {
			removeStones(copy1, Constants.WHITE);
		}
		
		return (getScore(copy1)+stones);
	}

	public static void setEmptytoNumber(Board board) {
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.isEmptyField(i, j)) {
					board.setField(i, j, EMPTY);
				}
			}
		}
	}

	public static void removeStones(Board board, int color) {
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (!board.isEmptyField(i, j)) {
					if (board.getField(i, j).getGroup().getColor() == color) {
						Set<Intersection> whitestones = board.getField(i, j).getGroup().getStones();
						for (Intersection whitestone : whitestones) {
							whitestone.setGroup(null);
							Set<Group> adjgroups = whitestone.getAdjGroups();
							for(Group adjgroup : adjgroups) {
								adjgroup.addLibertie(whitestone);
							}
						}
					}

				}
			}
		}
	}
	public static int getAmountofStones(Board board, int color) {
		int score = 0;
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if(board.getField(i, j).getGroup().getColor() ==  color) {
					score++;
				}
			}
		}
		return score;
	}
	
	public static int getScore(Board board) {
		int score = 0;
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (!board.isEmptyField(i, j)) {
					if (board.getField(i, j).getGroup().getColor() == EMPTY
							&& board.getField(i, j).getGroup().getLiberties().size() == 0) {
						score += board.getField(i, j).getGroup().getStones().size();
						board.getField(i, j).getGroup().getCaptured();
					}
				}

			}
		}
		return score;
	}
	
	
}
