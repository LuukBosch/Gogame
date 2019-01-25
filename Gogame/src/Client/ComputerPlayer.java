package Client;

import Game.Board;
import Game.History;

public class ComputerPlayer extends Player {
	Strategy strategy;
	
	public ComputerPlayer() {
		super();
		this.strategy = new NaiveStrategy();
		
	}

	public Strategy getStrategy() {
		return strategy;
	}
	
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	

	@Override
	public int determineMove(Board board, History history, int color) {
		int move = strategy.determineMove(board, history, color);
		return move;
	}
}
