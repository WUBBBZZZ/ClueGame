package experiment;
import java.util.Set;
import java.util.HashSet;

public class TestBoardCell {

	private int row;
	private int col;
	private Set<TestBoardCell> adjacencyList;
	private boolean isRoom;
	private boolean isOccupied;
	
	public TestBoardCell(int row, int col) {
		//initialize row, col, and adjacencyList
		this.row = row;
		this.col = col;
		adjacencyList = new HashSet<>();
	}
	
	public void addAdjacency(TestBoardCell cell) {
        //adds cell to adjacencyList
		
    }
	
	public Set<TestBoardCell> getAdjList() {
		//gets adjacency list of cell
		return null;
	}
	
	public boolean getRoom() {
		//gets isRoom
		return true;
	}
	
	public void setRoom(boolean isRoom) {
		//sets isRoom
		return;
	}
	
	public boolean getOccupied() {
		//gets isOccupied
		return true;
	}
	
	public void setOccupied(boolean isOccupied) {
		//sets isOccupied
		return;
	}

	public boolean isOccupied() {
		return false;
	}
	
}
