package Game;

import java.util.ArrayList;
import java.util.List;

public class History {
	List<String> history;
	
	public History() {
		history = new ArrayList<>();
	}
	
	public void addSituation(String board) {
		history.add(board);
	}
	
	public List<String> getHistory() {
		return history;
	}
	

}
