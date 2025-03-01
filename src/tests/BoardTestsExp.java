package tests;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import experiment.TestBoardCell;
import experiment.TestBoard;

public class BoardTestsExp {
	TestBoard board;

    @BeforeEach
    void setUp() {
        board = new TestBoard();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                board[row][col] = new TestBoardCell(row, col);
            }
        }

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (row > 0) board[row][col].addAdjacency(board[row - 1][col]); // Up
                if (row < 3) board[row][col].addAdjacency(board[row + 1][col]); // Down
                if (col > 0) board[row][col].addAdjacency(board[row][col - 1]); // Left
                if (col < 3) board[row][col].addAdjacency(board[row][col + 1]); // Right
            }
        }
    }

    @Test
    void testAdjacencyTopLeftCorner() {
        TestBoardCell cell = board[0][0];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(2, adjacencyList.size());
        assertTrue(adjacencyList.contains(board[0][1]));
        assertTrue(adjacencyList.contains(board[1][0]));
    }

    @Test
    void testAdjacencyBottomRightCorner() {
        TestBoardCell cell = board[3][3];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(2, adjacencyList.size());
        assertTrue(adjacencyList.contains(board[3][2]));
        assertTrue(adjacencyList.contains(board[2][3]));
    }

    @Test
    void testAdjacencyRightEdge() {
        TestBoardCell cell = board[1][3];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(3, adjacencyList.size());
        assertTrue(adjacencyList.contains(board[0][3]));
        assertTrue(adjacencyList.contains(board[2][3]));
        assertTrue(adjacencyList.contains(board[1][2]));
    }

    @Test
    void testAdjacencyLeftEdge() {
        TestBoardCell cell = board[3][0];
        Set<TestBoardCell> adjacencyList = cell.getAdjList();
        assertEquals(3, adjacencyList.size());
        assertTrue(adjacencyList.contains(board[2][0]));
        assertTrue(adjacencyList.contains(board[3][1]));
        assertTrue(adjacencyList.contains(board[3][1]));
    }

    @Test
    void testEmptyBoardTargets() {
        //Test for behavior on empty 4x4 board.
    	Set<TestBoardCell> targets = getTargets(board[1][1], 2);
        assertTrue(targets.contains(board[1][3]));
        assertTrue(targets.contains(board[3][1]));
        assertEquals(6, targets.size()); 
    }

    @Test
    void testOccupiedCellRestriction() {
        //Test for behavior with at least one cell being flagged as occupied. 
    	//A player cannot move into an occupied cell.
    	board[2][2].setOccupied(true);
        Set<TestBoardCell> targets = getTargets(board[1][1], 2);
        assertFalse(targets.contains(board[2][2])); 

    @Test
    void testRoomCellBehavior() {
        //Test for behavior with at least one cell being flagged as a room.  
    	//A player used up all movement points upon entering a room.
    	board[2][2].setRoom(true);
        Set<TestBoardCell> targets = getTargets(board[1][1], 2);
        assertTrue(targets.contains(board[2][2])); 
        assertFalse(targets.contains(board[3][2])); 
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
