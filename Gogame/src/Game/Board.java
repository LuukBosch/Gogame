package Game;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Board {

	Intersection[][] stones;
	int size;

	public Board(int size) {
		stones = new Intersection[size][size];
		this.size = size;
		createBoard();
	}
	
	public Board() {
		this(9);
	}
	

	public void createBoard() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				stones[i][j] = new Intersection(i, j, this);
			}
		}
	}
	
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
	

	public int getSize() {
		return size;
	}

	public boolean isField(int row, int col) {
		return row >= 0 && row < size && col >= 0 && col < size;
	}

	
	public boolean isField(Intersection intersection) {
		int col = intersection.getCol();
		int row = intersection.getRow();
		return isField(row, col);
	}

	public Intersection getField(int row, int col) {
		if (isField(row, col)) {
			return stones[row][col];
		} else {
			return null;
		}
	}
	
	public boolean isEmptyField(int row, int col) {
		if (isField(row, col)) {
			return stones[row][col].getGroup() == null;
		}
			return false;
	}

	public void setField(int row, int col, int color) {
		Intersection stone = getField(row, col);
		if (isField(stone) && isEmptyField(row,col)) {
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
		  for (Intersection stones : newGroup.getStones()) {
	            stones.setGroup(newGroup);
	        }

		}
	}
	
	
	public String getStringRepresentation() {
		String bordstringrep = "";
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if (stones[i][j].getGroup() == null){
					bordstringrep +="0";
				}
				else if(stones[i][j].getGroup().getColor() == Constants.BLACK){
					bordstringrep +="1";
				}else if(stones[i][j].getGroup().getColor() == Constants.WHITE){
					bordstringrep +="2";
			}
		}
		}
		return bordstringrep;
	}
	@Override
	public String toString() {
		String bord = "";
		for(int i = 0; i<size ; i++) {
			for(int j = 0; j<size ; j++) {
				bord += getMark(stones[i][j]);
				bord += " ";
				
			}
			bord +="\n";
			
		}
		return bord;
	}
	
	public String getMark(Intersection stone) {
		if(stone.getGroup() == null) {
			return("Empty");
		}else if(stone.getGroup().getColor() == 1) {
			return("  1  ");
			
		}else if(stone.getGroup().getColor() == 2) {
			return("  2  ");
		}else {
			return null;
		}
	}
	
}
