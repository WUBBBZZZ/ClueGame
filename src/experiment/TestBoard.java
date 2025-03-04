package experiment;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

public class TestBoard {
	private TestBoardCell[][] board;	//holds the board cells in a grid
	private Set<TestBoardCell> targets;			//holds the resulting targets from TargetCalc()
	private Set<TestBoardCell> visited;			//holds the visited list
	//Constants for grid size:
	private final static int COLS = 4;
	private final static int ROWS = 4;
	
	public TestBoard() {
		//Initializes testboard
		board = new TestBoardCell[ROWS][COLS];
		for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = new TestBoardCell(row, col);
            }
        }
		targets = new HashSet<>();
		visited = new HashSet<>();
	}
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		//calculates legal targets for a move from startCell of length pathLength
		visited.add(startCell);

	    for (TestBoardCell neighbor : startCell.getAdjList()) {
	        if (visited.contains(neighbor) || neighbor.getOccupied()) {
	            continue;
	        }
	        //room check
	        if (neighbor.getRoom()) {
	        	targets.add(neighbor);
	        }

	        if (pathLength == 1) {
	            targets.add(neighbor);
	        } else {
	            calcTargets(neighbor, pathLength - 1);
	        }
	    }

	    visited.remove(startCell);
		
	}
	public TestBoardCell getCell(int row, int col) {
		return board[row][col];
	}
	
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
	
	public TestBoardCell[][] getBoard() {
		return board;
	}

}
