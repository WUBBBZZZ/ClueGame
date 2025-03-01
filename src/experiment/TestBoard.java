package experiment;

import java.util.Set;

public class TestBoard {
	public TestBoardCell[][] board;
	
	public TestBoard() {
		board = new TestBoardCell[4][4];
	}
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		return;
	}
	public TestBoardCell getCell(int row, int col) {
		TestBoardCell bad = new TestBoardCell(100000, 10000);
		return bad;
	}
	public Set<TestBoardCell> getTargets(){
		return null;
	}

}
