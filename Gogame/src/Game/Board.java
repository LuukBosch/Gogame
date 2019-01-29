package Game;

import java.util.Set;

/**
 * Models the Goban used in a game of go.
 * 
 * @author luuk.bosch
 *
 */

public class Board {
	/**
	 * A multidimensional(N*N) array contains all the intersections of the Goban.
	 * Size is set to N;
	 */
	Intersection[][] stones;
	int size;

	/**
	 * creates a empty goban board of a specific size.
	 * @param size
	 */
	public Board(int size) {
		stones = new Intersection[size][size];
		this.size = size;
		createBoard();
	}

	/**
	 * Creates a default board with size 9*9.
	 */
	public Board() {
		this(9);
	}

	/**
	 * Returns the array containing all the intersections.
	 * 
	 * @return
	 */
	public Intersection[][] getBoard() {
		return stones;
	}

	/**
	 * Creates all the Intersection instances an places them in the right position
	 * in the stones array.
	 */
	public void createBoard() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				stones[i][j] = new Intersection(i, j, this);
			}
		}
	}

	/**
	 * Returns a deepcopy of the Goban.
	 * 
	 * @return
	 */
	public Board getDeepcopy() {
		Board deepcopy = new Board(size);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (!this.isEmptyField(i, j)) {
					deepcopy.setField(i, j, this.getField(i, j).getGroup().getColor());
				}
			}

		}
		return deepcopy;
	}

	/**
	 * returns the size(N) of the Goban.
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * checks if a field is part of the Goban.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isField(int row, int col) {
		return row >= 0 && row < size && col >= 0 && col < size;
	}

	/**
	 * checks if a specific intersection is part of the Goban.
	 * 
	 * @param intersection
	 * @return
	 */
	public boolean isField(Intersection intersection) {
		int col = intersection.getCol();
		int row = intersection.getRow();
		return isField(row, col);
	}

	/**
	 * Checks if a field is empty or not.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isEmptyField(int row, int col) {
		if (isField(row, col)) {
			return stones[row][col].getGroup() == null;
		}
		return false;
	}

	/**
	 * returns the Intersection for a given row and column number. 
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Intersection getField(int row, int col) {
		if (isField(row, col)) {
			return stones[row][col];
		} else {
			return null;
		}
	}

	/**
	 * Places a "stone" with a specific color on an Intersection.
	 * When the stone is placed all the neighbours are checked.
	 * if the neighbouring Intersections have the same color the groups are merged.
	 * When they have a different color the liberty of this group is removed. 
	 * @param row
	 * @param col
	 * @param color
	 */
	public void setField(int row, int col, int color) {
		Intersection stone = getField(row, col);
		if (isField(stone) && isEmptyField(row, col)) {
			Set<Group> adjGroups = stone.getAdjGroups();
			Group newGroup = new Group(stone, color);
			stone.setGroup(newGroup);
			for (Group adjGroup : adjGroups) {
				if (adjGroup.getColor() == color) {
					newGroup.mergeGroup(adjGroup, stone);
				} else {
					adjGroup.removeLibertie(stone);
				}
			}
			for (Intersection steen : newGroup.getStones()) {
				steen.setGroup(newGroup);
			}

		}
	}

	/**
	 * Returns a string representation of the Goban. Reading from left to right, top to bottom. 
	 * 0 represents empty, 1 represents black, 2 represents white. 
	 * @return
	 */
	public String getStringRepresentation() {
		String bordstringrep = "";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (stones[i][j].getGroup() == null) {
					bordstringrep += "0";
				} else if (stones[i][j].getGroup().getColor() == Constants.BLACK) {
					bordstringrep += "1";
				} else if (stones[i][j].getGroup().getColor() == Constants.WHITE) {
					bordstringrep += "2";
				}
			}
		}
		return bordstringrep;
	}

	/**
	 * returns a simple string representation of the Goban. 
	 */
	@Override
	public String toString() {
		String bord = "";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				bord += getMark(stones[i][j]);
				bord += " ";

			}
			bord += "\n";

		}
		return bord;
	}

	/**
	 * returns a simple string representation of a stone. 
	 * @param stone
	 * @return
	 */
	public String getMark(Intersection stone) {
		if (stone.getGroup() == null) {
			return "Empty";
		} else if (stone.getGroup().getColor() == 1) {
			return "  1  ";

		} else if (stone.getGroup().getColor() == 2) {
			return "  2  ";
		} else {
			return null;
		}
	}

}
