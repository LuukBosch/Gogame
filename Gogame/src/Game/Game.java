package Game;

import static Game.EnforceRule.enforceRules;

import java.util.HashMap;

import Server.ClientHandler;

public class Game {
	private int gameid;
	private Board board;
	private History history;
	private int turn = Constants.BLACK;
	private int passed = 0;

	public Game(int gameid) {
		this.gameid = gameid;
		history = new History();

	}
	
	public void setBoard(int size) {
		board = new Board(size);
	}
	
	public int getid() {
		return gameid;
	}
	

	public int handleMove(int color, int move) {
		int opponent = getOpponent(color);
		if (isTurn(color)) {
			if (isValidMove(move, color)) {
				if (move == -1) {
					passed++;
					if (passed == 2) {

						return 1;
					} else {
						changeTurn();

						return 2;
					}
				} else {
					playMove(move, color);

					passed = 0;
					return 2;
				}
			} else {
				return 3;
			}
		} else {
			return 4;
		}
	}

	// overbodig
	/**
	 * Plays a move on the board. Enforces the rules, adds board position to history
	 * and changes turn.
	 * 
	 * @param move
	 * @param player
	 */
	public void playMove(int move, int color) {
		int col = move % board.getSize();
		int row = (move - col) / board.getSize();
		board.setField(row, col, color);
		enforceRules(board, color);
		history.addSituation(board.getStringRepresentation());
		changeTurn();
	}

	public Board getBoard() {
		return board;
	}

	public int getSize() {
		return board.getSize();
	}

	/**
	 * Returns the color that is to move.
	 * 
	 * @return color of the player that is to move
	 */

	public int getTurn() {
		return turn;
	}

	/**
	 * Returns the color that did the previous move.
	 * 
	 * @return color of the player that did the previous move
	 */
	public int getPrevTurn() {
		if (turn == Constants.BLACK) {
			return Constants.WHITE;
		}
		if (turn == Constants.WHITE) {
			return Constants.BLACK;
		}
		return 0;
	}

	/**
	 * Changes turn.
	 */
	public void changeTurn() {
		if (turn == Constants.WHITE) {
			turn = Constants.BLACK;
		} else if (turn == Constants.BLACK) {
			turn = Constants.WHITE;
		}
	}

	/**
	 * checks if given color is to move.
	 * 
	 * @param player Clienthandler
	 * @return if player is to move or not
	 */
	public boolean isTurn(int color) {
		return turn == color;
	}

	/**
	 * Checks if a move suggested by a player is valid or not.
	 * 
	 * @param move
	 * @param player
	 * @return if voldig move or not
	 */
	public boolean isValidMove(int move, int color) {
		return MoveValidator.isValidMove(board, move, color, history);
	}

	// overbodig
	/**
	 * Resets the Game
	 */
	public void resetGame() {
		board = new Board(board.getSize());
		history = new History();
		turn = Constants.BLACK;

	}
	
	public int getOpponent(int color) {
		if (color == Constants.BLACK) {
			return Constants.WHITE;
		}
		if (color == Constants.WHITE) {
			return Constants.BLACK;
		}
		return -1;
	}
}
