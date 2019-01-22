package Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Intersection {
		
	private final int row;
	private final int col;
	private Group group;
	private Board board;
	
	public Intersection(int row, int col, Board board) {
		this.row = row;
		this.col = col;
		this.board = board;
		this.group = group;
		
	}
	
	public int getCol() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
	
	public boolean isEmpty() {
		return group == null;
	}
	

	public Group getGroup() {
		
		return group;
	}
	
	public Set<Group> getAdjGroups(){
		Set<Group> adjGroups = new HashSet<Group>();
		int[] rowadd = {-1, 0, 1, 0};
		int[] coladd = {0, -1, 0, 1};
		for(int i = 0; i < rowadd.length ; i++) {
			int newRow = rowadd[i] + row;
			int newCol = coladd[i] + col;
			if(board.isField(newRow, newCol)) {
				Intersection adjIntersection = board.getField(newRow, newCol);
				if(adjIntersection.getGroup() !=  null) {
					adjGroups.add(adjIntersection.getGroup());
				}
			}
		}
			
			
		return adjGroups;
	}
	
  
	
	public Set<Intersection> getEmptyNeighbors() {
		Set<Intersection> emptyFields = new HashSet<Intersection>();
		int[] rowadd = {-1, 0, 1, 0};
		int[] coladd = {0, -1, 0, 1};
		for(int i = 0; i < rowadd.length ; i++) {
			int newRow = rowadd[i] + row;
			int newCol = coladd[i] + col;
			if(board.isField(newRow, newCol)) {
				Intersection adjIntersection = board.getField(newRow, newCol);
				if(adjIntersection.isEmpty()) {
					emptyFields.add(adjIntersection);
				}
			}
		}
		return emptyFields;
	}
	
	
	
	public void setGroup(Group group) {
		this.group = group;
		
	}

	public String toString() {
		int ind = row*board.getSize()+ col;
		return Integer.toString(ind);
	}
	
}
	
	
	
	
		




