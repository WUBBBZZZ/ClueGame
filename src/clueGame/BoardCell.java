package clueGame;

import java.util.HashSet;
import java.util.Set;

import experiment.TestBoardCell;

public class BoardCell {
	private int row, col;
	private Boolean isRoom, isOccupied, doorway, roomCenter, label;
	private char type, roomInitial;
	private Set<BoardCell> adjList;
	private DoorDirection direction;
	private char secretPassage;
	// Constructor with row and col inputs
	public BoardCell(int r, int c) {
	    super();
	    this.row = r;
	    this.col = c;
	    adjList = new HashSet<BoardCell>();
	    isRoom = false;
	    isOccupied = false;
	    doorway = false;
	    roomCenter = false;
	    label = false;
	}
	
	public void setDirection(char c) {
		switch(c) {
			case 'U':
				this.direction = DoorDirection.UP;
				break;
			case 'D':
				this.direction = DoorDirection.DOWN;
				break;
			case 'L':
				this.direction = DoorDirection.LEFT;
				break;
			case 'R':
				this.direction = DoorDirection.RIGHT;
				break;
			default:
                this.direction = DoorDirection.NONE;
                break;
		}
		return;
	}
	
	// Adds an adjacent cell to this cells adjacency list
	public void addAdjacency(BoardCell grid) {
	    this.adjList.add(grid);
	}
	// Returns the adjacency list for the cell
	public Set<BoardCell> getAdjList(){
		return adjList;
	}
	
	// Setter for creating a room cell
	public void setRoom(boolean cell) {
		this.isRoom = cell;;
	}
	
	// Getter for if a space is a room cell
	public boolean isRoom() {
		return isRoom;
	}
	
	// Setter for indicating if a cell is occupied by another player
	public void setOccupied(boolean cell) {
		this.isOccupied = cell;;
	}
	
	// Getter for indicating if a cell is occupied by another player
	public boolean getOccupied() {
		return isOccupied;
	}
	
	public void setCenter(boolean x) {
		this.roomCenter = x;
	}
	
	public void setLabel(boolean x) {
		this.label = x;
	}
	
	public void setDoorway(boolean x) {
		this.doorway = x;
	}
	
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public boolean isDoorway() {
		return doorway;
	}
	
	public DoorDirection getDoorDirection() {
		return this.direction;
	}
	
	public boolean isLabel() {
		return label;
	}
	
	public char getSecretPassage() {
		return this.secretPassage;
	}
	
	public char getInitial() {
		return this.roomInitial;
	}
	
	public void setInitial(char character) {
		this.roomInitial = character;
	}
	public void setSecretPassage(char s) {
		this.secretPassage = s;
	}
	@Override
	public String toString() {
		return String.format("Cell: %s, %s", row, col);
	}
}
