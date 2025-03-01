package experiment;

import java.util.Set;
import java.util.HashSet;

public class TestBoard {
	public TestBoardCell[][] board;
	
	public TestBoard() {
		board = new TestBoardCell[4][4];
		for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                board[row][col] = new TestBoardCell(row, col);
            }
        }
	}
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		return;
	}
	public TestBoardCell getCell(int row, int col) {
		TestBoardCell bad = new TestBoardCell(100000, 10000);
		return bad;
	}
	public Set<TestBoardCell> getTargets(){
		return new HashSet<>();
	}

}
