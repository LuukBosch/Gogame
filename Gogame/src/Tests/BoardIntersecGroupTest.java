package Tests;

import org.junit.Before;
import org.junit.Test;
import Game.Intersection;
import Game.Board;
import Game.Constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests the basic functionality of the classes Group, Intersection, Board.
 * 
 * @author luuk.bosch
 *
 */
public class BoardIntersecGroupTest {
	Board board;
	Board board2;

	/**
	 * Boards of two different sizes are created.
	 */
	@Before
	public void setUp() {
		board = new Board();
		board2 = new Board(19);
	}

	/**
	 * Checks if the IsField() method works for both boards. valid fields(corners,
	 * non corners) are tested and invalid fields are tested
	 */
	@Test
	public void testIsField() {
		System.out.println(board2.toString());
		assertTrue(board.isField(0, 0));
		assertTrue(board.isField(0, 8));
		assertTrue(board.isField(8, 0));
		assertTrue(board.isField(8, 8));
		assertTrue(board.isField(5, 5));

		assertFalse(board.isField(9, 9));
		assertFalse(board.isField(0, 9));
		assertFalse(board.isField(-1, -1));

		assertTrue(board2.isField(0, 0));
		assertTrue(board2.isField(0, 18));
		assertTrue(board2.isField(18, 0));
		assertTrue(board2.isField(18, 18));
		assertTrue(board2.isField(5, 5));

		assertFalse(board.isField(19, 19));
		assertFalse(board.isField(0, 19));
		assertFalse(board.isField(-1, -1));
	}

	/**
	 * Tests if the getField method works
	 */
	@Test
	public void testgetField() {
		assertEquals(board.getBoard()[0][8], board.getField(0, 8));
		assertEquals(board.getBoard()[8][0], board.getField(8, 0));
		assertEquals(board.getBoard()[8][8], board.getField(8, 8));
		assertEquals(board.getBoard()[5][5], board.getField(5, 5));

		assertEquals(board2.getBoard()[0][18], board2.getField(0, 18));
		assertEquals(board2.getBoard()[18][0], board2.getField(18, 0));
		assertEquals(board2.getBoard()[18][18], board2.getField(18, 18));
		assertEquals(board2.getBoard()[5][5], board2.getField(5, 5));
	}

	/**
	 * Tests the getsize() method
	 */
	@Test
	public void testgetSize() {
		assertEquals(9, board.getSize());
		assertEquals(19, board2.getSize());
	}

	/**
	 * Tests the is Emptyfield() method
	 */
	@Test
	public void isEmptyField() {

		for (int i = 0; i < board.getSize(); i++) {
			int col = i % board.getSize();
			int row = (i - col) / board.getSize();
			assertTrue(board.isEmptyField(row, col));
		}

		for (int i = 0; i < board2.getSize(); i++) {
			int col = i % board2.getSize();
			int row = (i - col) / board2.getSize();
			assertTrue(board2.isEmptyField(row, col));
		}

		board.setField(0, 0, 1);
		board.setField(8, 8, 1);

		assertFalse(board.isEmptyField(0, 0));
		assertFalse(board.isEmptyField(8, 8));

		board2.setField(18, 18, 2);
		board2.setField(0, 0, 1);
		
		assertFalse(board2.isEmptyField(18, 18));
		assertFalse(board2.isEmptyField(0, 0));
	}

	/**
	 * Tests the setField() functionality of the Board class.
	 * It also checks if the merging of different groups is done correctly. 
	 */
	@Test
	public void testSetField_GroupConnection() {
		board.setField(0, 0, Constants.BLACK);
		board.setField(0, 1, Constants.BLACK);
		board.setField(0, 2, Constants.WHITE);
		board.setField(1, 0, Constants.BLACK);
		assertEquals(1, board.getField(0, 1).getGroup().getColor());
		assertEquals(1, board.getField(0, 0).getGroup().getColor());
		assertEquals(2, board.getField(0, 2).getGroup().getColor());
		System.out.println(board.toString());
		System.out.println(board.getField(0, 2).getGroup().getLiberties());
		assertTrue(board.getField(0, 0).getGroup() == board.getField(0, 1).getGroup());
		assertTrue(board.getField(0, 0).getGroup() == board.getField(1, 0).getGroup());
		assertFalse(board.getField(0, 0).getGroup() == board.getField(5, 5).getGroup());
		assertFalse(board.getField(0, 1).getGroup() == board.getField(0, 2).getGroup());
	}

	/**
	 * Tests if the StringRepresentation() works
	 */
	@Test
	public void testgetStringRepresentation() {
		board.setField(0, 0, Constants.BLACK);
		board.setField(0, 1, Constants.BLACK);
		board.setField(0, 2, Constants.WHITE);
		board.setField(1, 0, Constants.BLACK);
		board.setField(5, 5, Constants.WHITE);
		assertTrue("112000000100000000000000000000000000000000000000002000000000000000000000000000000"
				.equals(board.getStringRepresentation()));

	}

}
