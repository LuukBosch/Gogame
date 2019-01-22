package Game;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Board {

	Intersection[][] stones;
	int size;
	public static void main(String args[]) {
		Board bord = new Board(9);
		System.out.println(bord.toString());
		bord.setField(3, 3, 1);
		bord.setField(3, 4, 1);
		bord.setField(3, 5, 2);
		bord.setField(4, 3, 2);
		bord.setField(4, 3, 1);
		bord.setField(4, 4, 2);
		bord.setField(4, 5, 2);
		bord.setField(4, 6, 2);
		bord.setField(4, 7, 2);
		bord.setField(4, 8, 2);
		bord.setField(8, 8, 1);
		
	
		System.out.println(bord.toString());
		System.out.println(bord.toString());
	
		Set<Intersection> test = bord.getField(4, 3).getGroup().getLiberties();
		Set<Intersection> test2 = new HashSet<Intersection>();
		for(Intersection S: test) {
			test2.add(S);
		}
		for(Intersection R: test2) {
			bord.setField(R.getRow(), R.getCol(), 1);
			
		}
		System.out.println(bord.toString());
		//EnforceRule.apply(bord, 2);
		System.out.println(bord.toString());
		
	
		
	}	

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

	
	//overbodig?
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
				else if(stones[i][j].getGroup().getColor() == 1){
					bordstringrep +="1";
				}else if(stones[i][j].getGroup().getColor() == 2){
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
			return("  0  ");
			
		}else if(stone.getGroup().getColor() == 2) {
			return("  1  ");
		}else {
			return null;
		}
	}
	
}
