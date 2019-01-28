package Tests;


import org.junit.Before;
import org.junit.Test;
import Game.Intersection;
import Game.Score;
import Game.Board;
import Game.Constants;
import Game.History;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static Game.MoveValidator.isValidMove;


public class MoveValidatorTest {
	Board board;
	History history;
	
	
	@Before
	public void setUp() {
		board = new Board();		
		history = new History();
	}
	
	@Test
	public void testOccupiedField() {
		board.setField(0, 0, 1);
		
		history.addSituation(board.getStringRepresentation());
		assertFalse(isValidMove(board, 0, 1, history));
		board.setField(8, 0, 1);
		history.addSituation(board.getStringRepresentation());
		assertFalse(isValidMove(board, 72, 1, history));

		board.setField(0, 8, 1);
		history.addSituation(board.getStringRepresentation());
		assertFalse(isValidMove(board, 8, 1, history));

		board.setField(8, 8, 1);
		history.addSituation(board.getStringRepresentation());
		assertFalse(isValidMove(board, 80, 1, history));

		board.setField(5, 5, 1);
		history.addSituation(board.getStringRepresentation());
		assertFalse(isValidMove(board, 50, 1, history));

		
	}
	
	@Test
	public void testNoField() {
		assertFalse(isValidMove(board, 81, 1, history));
		assertFalse(isValidMove(board, -2, 2, history));
	}
	
	@Test
	public void testPass() {
	
		assertTrue(isValidMove(board, -1, 2, history));
	}
	
	
	
	@Test
	public void testSuperKoRule() {
		board.setField(0, 1, 1);
		history.addSituation(board.getStringRepresentation());
		board.setField(1, 0, 1);
		history.addSituation(board.getStringRepresentation());
		assertFalse(isValidMove(board, 0, 2, history));
		board.setField(0, 3, 2);
		board.setField(1, 2, 2);
		board.setField(1, 4, 2);
		board.setField(2, 3, 2);
		history.addSituation(board.getStringRepresentation());
		assertFalse(isValidMove(board, 12, 1, history));
		assertTrue(isValidMove(board, 12, 2, history));
		
		
	}

	
	
	
	
	
}
