package Tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import Game.Board;
import static Game.EnforceRule.enforceRules;

/**
 * Tests if the capturing of groups is done in a proper way. 
 * @author luuk.bosch
 *
 */
public class EnforceRuleTest {
	Board board;
	Board board2;
	
	
	@Before
	public void setUp() {
		board = new Board();	
	}
	
	/**
	 * Tests a simple caputring of a single stone. 
	 */
	
	@Test
	public void testSimpleCapture() {
		board.setField(1, 1, 1);
		board.setField(0, 1, 2);
		board.setField(2, 1, 2);
		board.setField(1, 0, 2);
		board.setField(1, 2, 2);
		enforceRules(board, 2);
		assertTrue(board.isEmptyField(1, 1));	
	}
	
	/**
	 * Tests the capturing of a single stone in all corners
	 */
	@Test
	public void testAllCornerCaptures() {
		board.setField(0, 1, 2);
		board.setField(0, 0, 1);
		board.setField(1, 0, 2);
		
		board.setField(0, 7, 2);
		board.setField(1, 8, 2);
		board.setField(0, 8, 1);
		
		board.setField(7, 0, 2);
		board.setField(8, 1, 2);
		board.setField(8, 0, 1);
		
		board.setField(7, 8, 2);
		board.setField(8, 7, 2);
		board.setField(8, 8, 1);
		enforceRules(board, 1);
		assertTrue(board.isEmptyField(0, 0));	
		assertTrue(board.isEmptyField(0, 8));	
		assertTrue(board.isEmptyField(8, 0));	
		assertTrue(board.isEmptyField(8, 8));	
		
	}
	
	/**
	 * Tests the capturing of a larger group on the edge of the board.  
	 */
	@Test
	public void testLargeCaputre() {
		board.setField(0, 0, 1);
		board.setField(1, 0, 1);
		board.setField(2, 0, 1);
		board.setField(3, 0, 1);
		board.setField(4, 0, 1);
		board.setField(5, 0, 1);
		board.setField(0, 1, 2);
		board.setField(1, 1, 2);
		board.setField(2, 1, 2);
		board.setField(3, 1, 2);
		board.setField(4, 1, 2);
		board.setField(5, 1, 2);
		board.setField(6, 0, 2);
		enforceRules(board, 1);
		assertTrue(board.isEmptyField(0, 0));
		assertTrue(board.isEmptyField(1, 0));
		assertTrue(board.isEmptyField(2, 0));
		assertTrue(board.isEmptyField(3, 0));
		assertTrue(board.isEmptyField(4, 0));
		assertTrue(board.isEmptyField(5, 0));
	}
	
	/**
	 * Tests the capturing of a larger group in the middle of the board. 
	 */
	@Test
	public void midBoardCapture() {
		board.setField(5, 5, 1);
		board.setField(5, 4, 1);
		board.setField(5, 6, 1);
		board.setField(4, 5, 1);
		board.setField(6, 5, 1);
		
		board.setField(5, 3, 2);
		board.setField(5, 7, 2);
		board.setField(4, 5, 2);
		board.setField(6, 5, 2);
		board.setField(4, 6, 2);
		board.setField(4, 4, 2);
		board.setField(6, 6, 2);
		board.setField(6, 4, 2);
		board.setField(3, 5, 2);
		board.setField(7, 5, 2);
		enforceRules(board, 1);
		assertTrue(board.isEmptyField(5, 5));
		assertTrue(board.isEmptyField(5, 4));
		assertTrue(board.isEmptyField(5, 6));
		assertTrue(board.isEmptyField(4, 5));
		assertTrue(board.isEmptyField(6, 5));
	}
	
	/**
	 * Tests if the order of Capturing/suicide is done properly. 
	 */
	@Test
	public void captureBeforeSuicide() {
		
		board.setField(0, 1, 2);
		board.setField(0, 2, 1);
		board.setField(0, 5, 1);
		board.setField(1, 4, 1);

		board.setField(1, 3, 2);
		board.setField(1, 2, 2);
		board.setField(0, 1, 2);
		board.setField(0, 4, 2);
		board.setField(0, 3, 1);
		enforceRules(board, 1);
		assertTrue(board.isEmptyField(0, 4));
		
	}
}
	