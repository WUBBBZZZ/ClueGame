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
		this.row = row;
		this.col = col;
		this.adjacencyList = new HashSet<>();
	}
	
	public void addAdjacency(TestBoardCell cell) {
        adjacencyList.add(cell);
    }
	
	public Set<TestBoardCell> getAdjList() {
		return adjacencyList;
	}
	
	public boolean getRoom() {
		return isRoom;
	}
	
	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	public boolean getOccupied() {
		return isOccupied;
	}
	
	public void setOccupied(boolean isOccupied) {
		this.isOccupied= isOccupied;
	}
	
	
}
