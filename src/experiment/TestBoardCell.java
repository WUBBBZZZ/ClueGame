package experiment;
import java.util.Set;
import java.util.HashSet;

public class TestBoardCell {

	private int row;
	private int col;
	private Set<TestBoardCell> adjacencyList;
	private Set<TestBoardCell> exList = new HashSet<>();
	private boolean isRoom;
	private boolean isOccupied;
	
	public TestBoardCell(int row, int col) {
		//initialize row, col, and adjacencyList
		this.row = row;
		this.col = col;
		adjacencyList = new HashSet<>();
		this.isRoom = false;
		this.isOccupied = false;
	}
	
	public void addAdjacency(TestBoardCell cell) {
        //adds cell to adjacencyList
		adjacencyList.add(cell);
		
    }
	
	public Set<TestBoardCell> getAdjList() {
		//gets adjacency list of cell
		return exList;
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
		
	}

	public boolean isOccupied() {
		return false;
	}
	
}
