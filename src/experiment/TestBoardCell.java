package experiment;
import java.util.Set;
import java.util.HashSet;

public class TestBoardCell {

	private int row, col;
	private Set<TestBoardCell> adjacencyList;
	private boolean isRoom, isOccupied;
	
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
		return adjacencyList;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public boolean getRoom() {
		//gets isRoom
		return isRoom;
	}
	
	public void setRoom(boolean isRoom) {
		//sets isRoom
		this.isRoom = isRoom;
	}
	
	public boolean getOccupied() {
		//gets isOccupied
		return isOccupied;
	}
	
	public void setOccupied(boolean isOccupied) {
		//sets isOccupied
		this.isOccupied = isOccupied;
	}

	public boolean isOccupied() {
		return false;
	}
	
}
