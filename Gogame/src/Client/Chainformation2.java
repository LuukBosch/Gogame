package Client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import static Game.MoveValidator.isValidMove;
import Game.*;

public class Chainformation2 implements Strategy {

	/**
	 * This AI has three functions that are able to determine a possible moves.
	 * *Atary, checks if a group of the opponent can be captured. if this is the
	 * case this move is played
	 * 
	 * *buildGroup, Checks for every position if a stone belongs to a group of
	 * stones. It finds all the liberties of this group. For every liberty it checks
	 * the amount of empty neighbours. If the liberty has 2 or more empty neighbours
	 * this liberty is played. if this is not the case the liberty is remembered and
	 * played later if no other liberties are found.
	 * 
	 * 
	 * If a group can't be increased a simple move is played. The method looks for
	 * an empty field. If this empty field has 1 or more empty neighbours, doesn't
	 * cause suicide, and is a valid move, this move is played
	 * 
	 * 
	 */
	@Override
	public int determineMove(Board board, History history, int color, int time) {
		long start = System.currentTimeMillis();
		long end = start + time * 1000; // 60 seconds * 1000 ms/sec
		int move = atary(board, history, color);
		if (move != -2) {
			return move;
		}
		if (System.currentTimeMillis() > end) {
			return -1;
		}
		int move2 = buildGroup(board, history, color);
		if (move2 != -2) {
			return move2;
		} else {
			if (System.currentTimeMillis() > end) {
				return -1;
			}
			return simpleMove(board, history, color);
		}
	}

	
	/**
	 * A method that searches for a good move to enlarge a existing group.
	 * Its starts with searching for non empty fields
	 * if a non empty field belongs to a group with the same color as the input it checks the 
	 * liberties of this group. For every libertie it checks the amount of empty neighbours around it. 
	 * if this 2 or more it checks checks if the libertie is located at the border of the field.
	 * If this is the case this move is played directly. If this is not the case the move is remembered and 
	 * played if no other "group enlarging" move is found. If no good move is found -2 is returned. 
	 * @param board
	 * @param history
	 * @param color
	 * @return
	 */
	public int buildGroup(Board board, History history, int color) {
		int side = board.getSize();
		int remember = 99;
		for (int i = 0; i < side; i++) {
			for (int j = 0; j < side; j++) {
				if (!board.isEmptyField(i, j)) {
					Group groep = board.getField(i, j).getGroup();
					if (groep.getColor() == color) {
						for (Intersection move : groep.getLiberties()) {
							Set<Intersection> test = move.getEmptyNeighbors();
							if (test.size() >= 2) {
								if (move.getCol() == 0 || move.getRow() == 0 || move.getRow() == (side - 1)
										|| move.getCol() == (side - 1)) {
									int testmove = Integer.valueOf(move.toString());
									if (isValidMove(board, testmove, color, history)) {
										return testmove;
									}
								} else {
									remember = Integer.valueOf(move.toString());
								}
							}
						}

					}
				}
			}
		}
		if (remember != 99) {
			return remember;
		}
		return -2;
	}

	/**
	 * Checks if a stone or a group of stones of the opponent only has 1 liberty.
	 * If this is the case and playing this libertie is a valid move this move is
	 * returned
	 * 
	 * @param board   current board
	 * @param history history of the game
	 * @param color   color of the player
	 * @return
	 */
	public int atary(Board board, History history, int color) {
		int side = board.getSize();
		int opp;
		if (color == 1) {
			opp = 2;
		} else {
			opp = 1;
		}
		for (int i = 0; i < side; i++) {
			for (int j = 0; j < side; j++) {
				if (!board.isEmptyField(i, j)) {
					Set<Intersection> test = board.getField(i, j).getGroup().getLiberties();
					if (test.size() == 1 && board.getField(i, j).getGroup().getColor() == opp) {
						int testmove = Integer.valueOf(test.iterator().next().toString());
						if (isValidMove(board, testmove, color, history)) {
							return testmove;
						}
					}
				}

			}
		}

		return -2;
	}

	/**
	 * This function checks for every field if it is empty. If this is the case it checks of the 
	 * amount of empty neighbours 1 or more(prevents suicide) and checks if this is a valid intersection to play.
	 * If this is the case the move is added to a list. In the end a move is randomly chosen from the list. 
	 * if the list is empty a pass is returned. 
	 * @param board current board position
	 * @param history history of the game
	 * @param color color of the player who needs to move. 
	 * @return
	 */
	public int simpleMove(Board board, History history, int color) {
		ArrayList<Integer> possiblemoves = new ArrayList<Integer>();
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.isEmptyField(i, j)) {
					if (board.getField(i, j).getEmptyNeighbors().size() >= 1) {
						int move = Integer.valueOf(board.getField(i, j).toString());
						if (isValidMove(board, move, color, history)) {
							possiblemoves.add(move);
						}
					}

				}
			}
		}
		Random rand = new Random();
		if (possiblemoves.size() == 0) {
			return -1;
		}
		return possiblemoves.get(rand.nextInt(possiblemoves.size()));
	}

}