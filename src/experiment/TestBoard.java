package experiment;

import java.util.Set;
import java.util.HashSet;

public class TestBoard {
	private TestBoardCell[][] board;	//holds the board cells in a grid
	private Set<TestBoardCell> targets;	//holds the resulting targets from TargetCalc()
	private Set<TestBoardCell> visited;	//holds the visited list
	//Constants for grid size:
	final static int COLS = 4;
	final static int ROWS = 4;
	
	public TestBoard() {
		board = new TestBoardCell[ROWS][COLS];
		for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = new TestBoardCell(row, col);
            }
        }
	}
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		//calculates legal targets for a move from startCell of length pathLength
		targets = new HashSet<>();
		visited = new HashSet<>();
		if (pathLength < 1) {
			System.out.println("Invalid path length. Must be greater than 0.");
		} 
		else if (pathLength == 1) {
			
		}
		return;
	}
	public TestBoardCell getCell(int row, int col) {
		return board[row][col];
	}
	public Set<TestBoardCell> getTargets(){
		return targets;
	}

}
