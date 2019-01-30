package Tests;

import org.junit.Before;
import org.junit.Test;

import Game.Board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static Game.Score.getScore;

/**
 * Tests if the Score calculator results in valid scores.
 * 
 * @author luuk.bosch
 *
 */
public class ScoreTest {
	Board board;
	Board board2;

	@Before
	public void setUp() {
		board = new Board();
		board2 = new Board(5);
	}

	/**
	 * Tests is a board filled with a single type of stone results in max score
	 */
	@Test
	public void testFullBoard() {
		board2.setField(0, 0, 1);
		board2.setField(0, 1, 1);
		board2.setField(0, 2, 1);
		board2.setField(0, 3, 1);
		board2.setField(0, 4, 1);
		board2.setField(1, 0, 1);
		board2.setField(1, 1, 1);
		board2.setField(1, 2, 1);
		board2.setField(1, 3, 1);
		board2.setField(1, 4, 1);
		board2.setField(2, 0, 1);
		board2.setField(2, 1, 1);
		board2.setField(2, 2, 1);
		board2.setField(2, 3, 1);
		board2.setField(2, 4, 1);
		board2.setField(3, 0, 1);
		board2.setField(3, 1, 1);
		board2.setField(3, 2, 1);
		board2.setField(3, 3, 1);
		board2.setField(3, 4, 1);
		board2.setField(4, 0, 1);
		board2.setField(4, 1, 1);
		board2.setField(4, 2, 1);
		board2.setField(4, 3, 1);
		board2.setField(4, 4, 1);
		assertEquals(25, getScore(board2, 1));

	}

	/**
	 * Tests if a empty board results in 0 score.
	 */
	@Test
	public void testEmptyField() {
		System.out.println(board2.toString());
		assertEquals(0, getScore(board2, 1));
		assertEquals(0, getScore(board2, 2));

	}

	/**
	 * Tests if corners are scored correctly.
	 */
	@Test
	public void testCorners() {
		board.setField(0, 1, 1);
		board.setField(1, 0, 1);
		board.setField(0, 7, 1);
		board.setField(1, 8, 1);
		board.setField(7, 0, 1);
		board.setField(8, 1, 1);
		board.setField(7, 8, 1);
		board.setField(8, 7, 1);
		board.setField(5, 5, 2);
		assertEquals(12, getScore(board, 1));

	}

	/**
	 * Tests if a large Captured area is scored correctly
	 */

	@Test
	public void largeCapturedArea() {
		board.setField(0, 0, 1);
		board.setField(1, 1, 1);
		board.setField(2, 1, 1);
		board.setField(3, 1, 1);
		board.setField(4, 1, 1);
		board.setField(5, 1, 1);
		board.setField(6, 0, 1);
		board.setField(7, 0, 1);
		board.setField(5, 5, 2);
		assertEquals(13, getScore(board, 1));
	}

	/**
	 * Test if a random filled field is scored correclty.
	 */
	@Test
	public void randomFilledField() {
		board.setField(0, 0, 1);
		board.setField(1, 1, 1);
		board.setField(2, 1, 1);
		board.setField(3, 1, 1);
		board.setField(4, 1, 1);
		board.setField(5, 1, 1);
		board.setField(6, 0, 1);
		board.setField(7, 0, 1);
		board.setField(5, 5, 2);
		board.setField(0, 8, 2);
		board.setField(1, 7, 2);
		board.setField(2, 7, 2);
		board.setField(3, 7, 2);
		board.setField(4, 7, 2);
		board.setField(5, 6, 2);
		board.setField(6, 7, 2);
		board.setField(7, 8, 2);
		board.setField(4, 4, 2);
		board.setField(4, 6, 2);
		board.setField(2, 3, 1);
		board.setField(3, 3, 1);
		board.setField(7, 4, 2);
		assertEquals(15, getScore(board, 1));
		assertEquals(19, getScore(board, 2));
	}

}
