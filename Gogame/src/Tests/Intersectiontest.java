package Tests;

import org.junit.Before;
import org.junit.Test;
import Game.Intersection;
import Game.Board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class Intersectiontest {
    private Intersection intersection1;
    private Intersection intersection2;
    private Intersection intersection3;
    private Board board;
    

    @Before
    public void setUp() {
    	board = new Board();
        intersection1 = new Intersection(0,0,board);
        intersection2 = new Intersection(5,5,board);
        intersection3 = new Intersection(2,3,board);
    }
    
    
    @Test
    public void testgetRow() {
    	assertEquals(0, intersection1.getRow());
    	assertEquals(5, intersection2.getRow());
    	assertEquals(2, intersection3.getRow());
    }
    
    @Test
    public void testgetCol() {
    	assertEquals(0, intersection1.getCol());
    	assertEquals(5, intersection2.getCol());
    	assertEquals(3, intersection3.getCol());
    }
}
    