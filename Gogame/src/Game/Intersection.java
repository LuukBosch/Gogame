package Game;

import java.util.HashSet;
import java.util.Set;
/**
 * Models the intersection of a Goban.  
 * @author luuk.bosch
 *
 */
public class Intersection {
	/**
	 * The private variables row and column describe 
	 * the position of the intersection on the go board.
	 * The Board is a model of the Goban of which the intersection is a part. 
	 * During the game of go an intersection can become part of a group.
	 */
	private final int row;
	private final int col;
	private Group group;
	private Board board;
	
	/**
	 * Creates an Intersection for a specific position on the Goban. 
	 * @param row position of the Intersection. 
	 * @param col position of the Intersection. 
	 * @param board The board to which the intersection belongs. 
	 */
	public Intersection(int row, int col, Board board) {
		this.row = row;
		this.col = col;
		this.board = board;
		
	}
	
	/**
	 * Returns the column of the intersection. 
	 * @return
	 */
	public int getCol() {
		return col;
	}
	
	/**
	 * Returns the row of the intersection. 
	 * @return
	 */
	
	public int getRow() {
		return row;
	}
	
	/** 
	 * Checks if an Intersection is empty or not. 
	 * @return
	 */
	public boolean isEmpty() {
		return group == null;
	}
	
	/**
	 * returns the group of which the Intersection is a member. 
	 * @return
	 */
	public Group getGroup() {
		
		return group;
	}
	
	/**
	 * returns the adjacent groups of the intersection. 
	 * @return
	 */
	public Set<Group> getAdjGroups() { 
		Set<Group> adjGroups = new HashSet<Group>();
		int[] rowadd = {-1, 0, 1, 0 };
		int[] coladd = {0, -1, 0, 1 };
		for (int i = 0; i < rowadd.length; i++) {
			int newRow = rowadd[i] + row;
			int newCol = coladd[i] + col;
			if (board.isField(newRow, newCol)) {
				Intersection adjIntersection = board.getField(newRow, newCol);
				if (adjIntersection.getGroup() != null) {
					adjGroups.add(adjIntersection.getGroup());
				}
			}
		}
			
			
		return adjGroups;
	}
	
  
	/**
	 * returns the empty neighbours of an Intersection. 
	 * @return
	 */
	public Set<Intersection> getEmptyNeighbors() {  
		Set<Intersection> emptyFields = new HashSet<Intersection>();
		int[] rowadd = {-1, 0, 1, 0 };
		int[] coladd = {0, -1, 0, 1 };
		for (int i = 0; i < rowadd.length; i++) {  
			int newRow = rowadd[i] + row;
			int newCol = coladd[i] + col;
			if (board.isField(newRow, newCol)) {
				Intersection adjIntersection = board.getField(newRow, newCol);
				if (adjIntersection.isEmpty()) {
					emptyFields.add(adjIntersection);
				}
			}
		}
		return emptyFields;
	}
	
	/**
	 * assigns the intersection to a specific group.
	 * @param group
	 */
	public void setGroup(Group group) {
		this.group = group;
		
	}

	/**
	 * returns a string containing the index of the Intersection. 
	 */
	public String toString() {
		int ind = row * board.getSize() + col;
		return Integer.toString(ind);
	}
	
}
	
	
	
	
		



