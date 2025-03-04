package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import experiment.TestBoardCell;
import experiment.TestBoard;

public class BoardTestsExp {
	TestBoard grid;

    @BeforeEach
    public void setUp() {
        grid = new TestBoard();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (row > 0) grid.getBoard()[row][col].addAdjacency(grid.getBoard()[row - 1][col]); // Up
                if (row < 3) grid.getBoard()[row][col].addAdjacency(grid.getBoard()[row + 1][col]); // Down
                if (col > 0) grid.getBoard()[row][col].addAdjacency(grid.getBoard()[row][col - 1]); // Left
                if (col < 3) grid.getBoard()[row][col].addAdjacency(grid.getBoard()[row][col + 1]); // Right
            }
        }
    }

    @Test
    public void testAdjacencyTopLeftCorner() {
        TestBoardCell cell = grid.getBoard()[0][0];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(2, adjacencyList.size());
        assertTrue(adjacencyList.contains(grid.getBoard()[0][1]));
        assertTrue(adjacencyList.contains(grid.getBoard()[1][0]));
    }

    @Test
    public void testAdjacencyBottomRightCorner() {
        TestBoardCell cell = grid.getBoard()[3][3];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(2, adjacencyList.size());
        assertTrue(adjacencyList.contains(grid.getBoard()[3][2]));
        assertTrue(adjacencyList.contains(grid.getBoard()[2][3]));
    }

    @Test
    public void testAdjacencyRightEdge() {
        TestBoardCell cell = grid.getBoard()[3][1];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(3, adjacencyList.size());
        assertTrue(adjacencyList.contains(grid.getBoard()[3][0]));
        assertTrue(adjacencyList.contains(grid.getBoard()[2][1]));
        assertTrue(adjacencyList.contains(grid.getBoard()[3][2]));
    }

    @Test
    public void testAdjacencyLeftEdge() {
        TestBoardCell cell = grid.getBoard()[0][2];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(3, adjacencyList.size());
        assertTrue(adjacencyList.contains(grid.getBoard()[0][1]));
        assertTrue(adjacencyList.contains(grid.getBoard()[1][2]));
        assertTrue(adjacencyList.contains(grid.getBoard()[0][3]));
    }

    @Test
    public void testEmptyBoardTargets() {
        //Test for behavior on empty 4x4 board.
    	grid.calcTargets(grid.getBoard()[1][1], 2);
    	Set<TestBoardCell> result = grid.getTargets();
        assertTrue(result.contains(grid.getBoard()[1][3]));
        assertTrue(result.contains(grid.getBoard()[3][1]));
        assertEquals(6, result.size()); 
    }
    
    @Test
    public void testOccupiedCellRestriction() {
        //Test for behavior with at least one cell being flagged as occupied. 
    	//A player cannot move into an occupied cell.
    	grid.getBoard()[2][2].setOccupied(true);
    	grid.calcTargets(grid.getBoard()[1][1], 2);
    	Set<TestBoardCell> result = grid.getTargets();
        assertFalse(result.contains(grid.getBoard()[2][2])); 
    }
    @Test
    public void testRoomCellBehavior() {
        //Test for behavior with at least one cell being flagged as a room.  
    	//A player used up all movement points upon entering a room.
    	grid.getBoard()[1][2].setRoom(true);
    	grid.calcTargets(grid.getBoard()[1][1], 2);
    	Set<TestBoardCell> result = grid.getTargets();
        assertTrue(result.contains(grid.getBoard()[1][2])); 
        assertEquals(7, result.size()); 
    }

}
