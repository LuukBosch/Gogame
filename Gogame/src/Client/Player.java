package Client;

import Game.Board;
import Game.History;

public abstract class Player {
	
	public Player() {
	}
	
	
	public abstract int determineMove(Board board, History history, int color);

}