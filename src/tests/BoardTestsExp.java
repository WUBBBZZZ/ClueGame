package tests;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import experiment.TestBoardCell;
import experiment.TestBoard;

public class BoardTestsExp extends TestBoard{
	TestBoard board;

    @BeforeEach
    public void setUp() {
        board = new TestBoard();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                board.board[row][col] = new TestBoardCell(row, col);
            }
        }

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (row > 0) board.board[row][col].addAdjacency(board.board[row - 1][col]); // Up
                if (row < 3) board.board[row][col].addAdjacency(board.board[row + 1][col]); // Down
                if (col > 0) board.board[row][col].addAdjacency(board.board[row][col - 1]); // Left
                if (col < 3) board.board[row][col].addAdjacency(board.board[row][col + 1]); // Right
            }
        }
    }

    @Test
    public void testAdjacencyTopLeftCorner() {
        TestBoardCell cell = board.board[0][0];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(2, adjacencyList.size());
        assertTrue(adjacencyList.contains(board.board[0][1]));
        assertTrue(adjacencyList.contains(board.board[1][0]));
    }

    @Test
    public void testAdjacencyBottomRightCorner() {
        TestBoardCell cell = board.board[3][3];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(2, adjacencyList.size());
        assertTrue(adjacencyList.contains(board.board[3][2]));
        assertTrue(adjacencyList.contains(board.board[2][3]));
    }

    @Test
    public void testAdjacencyRightEdge() {
        TestBoardCell cell = board.board[1][3];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(3, adjacencyList.size());
        assertTrue(adjacencyList.contains(board.board[0][3]));
        assertTrue(adjacencyList.contains(board.board[2][3]));
        assertTrue(adjacencyList.contains(board.board[1][2]));
    }

    @Test
    public void testAdjacencyLeftEdge() {
        TestBoardCell cell = board.board[3][0];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(3, adjacencyList.size());
        assertTrue(adjacencyList.contains(board.board[2][0]));
        assertTrue(adjacencyList.contains(board.board[3][1]));
        assertTrue(adjacencyList.contains(board.board[3][1]));
    }

    @Test
    public void testEmptyBoardTargets() {
        //Test for behavior on empty 4x4 board.
    	Set<TestBoardCell> targets = getTargets(board.board[1][1], 2);
        assertTrue(targets.contains(board.board[1][3]));
        assertTrue(targets.contains(board.board[3][1]));
        assertEquals(6, targets.size()); 
    }
    
    @Test
    public void testOccupiedCellRestriction() {
        //Test for behavior with at least one cell being flagged as occupied. 
    	//A player cannot move into an occupied cell.
    	board.board[2][2].setOccupied(true);
        Set<TestBoardCell> targets = getTargets(board.board[1][1], 2);
        assertFalse(targets.contains(board.board[2][2])); 
    }
    @Test
    public void testRoomCellBehavior() {
        //Test for behavior with at least one cell being flagged as a room.  
    	//A player used up all movement points upon entering a room.
    	board.board[2][2].setRoom(true);
        Set<TestBoardCell> targets = getTargets(board.board[1][1], 2);
        assertTrue(targets.contains(board.board[2][2])); 
        assertFalse(targets.contains(board.board[3][2])); 
    }

    //helper functions
    private Set<TestBoardCell> getTargets(TestBoardCell startCell, int steps) {
        Set<TestBoardCell> visited = new HashSet<>();
        exploreTargets(startCell, steps, visited);
        return visited;
    }

    private void exploreTargets(TestBoardCell cell, int steps, Set<TestBoardCell> visited) {
        if (steps == 0 || cell.isOccupied()) return;
        visited.add(cell);
        for (TestBoardCell adj : cell.getAdjList()) {
            if (!visited.contains(adj) && !adj.isOccupied()) {
                exploreTargets(adj, steps - 1, visited);
            }
        }
    }
}
