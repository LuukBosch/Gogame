package Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Game.Board;
import Game.History;
import Game.MoveValidator;

public class NaiveStrategy implements Strategy{



	@Override
	public int determineMove(Board board, History history, int color) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Integer> possiblemoves = new ArrayList<Integer>();
		for (int i = 0; i < history.getSize(); i++) {
			if (MoveValidator.isValidMove(board, i, color, history)) {
				possiblemoves.add(i);
			}
		}
		Random rand = new Random();
		if (possiblemoves.size() == 0) {
			return -1;
		}
		System.out.println(board.toString());
		System.out.println("----------------------------------");
		System.out.println(possiblemoves.toString());
		return possiblemoves.get(rand.nextInt(possiblemoves.size()));
	}

}
