package Client;

import Game.Board;
import Game.History;

public interface Strategy {
	
	
	public int determineMove(Board board, History history, int color, int maxtime);
	
	

}
