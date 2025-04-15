package clueGame;

import java.util.HashSet;
import java.util.Set;

//For the draw() method
import java.awt.Color;
import java.awt.Graphics;

import experiment.TestBoardCell;

public class BoardCell {
	private int row, col;
	private Boolean isRoom, isOccupied, doorway, roomCenter, label;
	private char type, roomInitial;
	private Set<BoardCell> adjList;
	private DoorDirection direction;
	private char secretPassage;
	private String name;
	public static final char NO_SECRET_PASSAGE = 0;

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
	
	// Sets the direction variable to be what the direction symbol means for the specified door
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
	public void addAdjacency(BoardCell cell) {
		//System.out.println(cell);
	    this.adjList.add(cell);
	}
	public void addAdjacencyCheck(BoardCell cell) {
		int r = cell.getRow();
		int c = cell.getCol();
		System.out.println(String.format("Cell: %s, %s added Cell %s, %s", row, col, r, c));
	    this.adjList.add(cell);
	}
	// Returns the adjacency list for the cell
	public Set<BoardCell> getAdjList(){
		return adjList;
	}
	
	// Setter for creating a room cell
	public void setRoom(boolean cell) {
		this.isRoom = cell;
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
	
	// Setter for the center of room
	public void setCenter(boolean x) {
		this.roomCenter = x;
	}
	
	// Setter for a label
	public void setLabel(boolean x) {
		this.label = x;
	}
	
	// setter for doorway type of walkway
	public void setDoorway(boolean x) {
		this.doorway = x;
	}
	
	// getter for whether a tile is a room center
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	// getter for if a walkway is a doorway
	public boolean isDoorway() {
		return doorway;
	}
	
	// getter for the direction of a door
	public DoorDirection getDoorDirection() {
		return this.direction;
	}
	
	// getter for a label
	public boolean isLabel() {
		return label;
	}
	
	// getter for a secretPassage tile
	public char getSecretPassage() {
		return this.secretPassage;
	}
	
	// getter for a specific room initial
	public char getInitial() {
		return this.roomInitial;
	}
	
	// setter for a room initial
	public void setInitial(char character, String name) {
		this.roomInitial = character;
		this.name = name;
	}
	
	// setter for a secret passage room tile type
	public void setSecretPassage(char s) {
		this.secretPassage = s;
	}
	
	// getter for the row of a tile
	public int getRow() {
		return this.row;
	}
	
	// getter for the column of a tile
	public int getCol() {
		return this.col;
	}
	
	// method for seeing if the board has a secret passage of the char defined 
	public boolean hasSecretPassage() {
	    return secretPassage != Board.NO_SECRET_PASSAGE;
	}
	
	// toString method, labeling a cell which the BoardCell object refers to
	@Override
	public String toString() {
		return String.format("Cell: %s, %s", row, col);
	}

	public void setRoomName(String string) {
		name = string;
		
	}
	
	//Draws BoardCells for the GUI
	public void draw(Graphics g, int row, int col, int width, int height) {
		//draw method for each board cell type
		//IMPORTANT: For some reason, rows and columns are inverted in
		//the GUI, so they need to be swapped when drawing.
		row = row * 30;
		col = col * 30;
		if (this.getInitial() == 'W') {
			g.setColor(Color.YELLOW);
			g.fillRect(col,  row,  width,  height);
			g.setColor(Color.BLACK);
			g.drawRect(col, row, width, height);
		} else if (this.getInitial() == 'X') {
			g.setColor(Color.BLACK);
			g.fillRect(col,  row,  width,  height);
			g.setColor(Color.BLACK);
			g.drawRect(col, row, width, height);
		} else if (this.isRoomCenter()){
			g.setColor(Color.GREEN);
			g.fillRect(col,  row,  width,  height);
			g.drawRect(col, row, width, height);
			g.setColor(Color.BLUE);
			g.drawString(name, col, row);
		}else {
			g.setColor(Color.GREEN);
			g.fillRect(col,  row,  width,  height);
			g.drawRect(col, row, width, height);
		}
		if (this.isDoorway()) {
	        g.setColor(Color.RED);
            int doorThickness = 4;  // Adjust the thickness as needed
            DoorDirection doorDirection = this.getDoorDirection();
            switch(doorDirection) {
                case UP:
                    // Draw a door on the top edge
                    g.fillRect(col, row, width, doorThickness);
                    break;
                case DOWN:
                    // Draw a door on the bottom edge
                    g.fillRect(col, row + height - doorThickness, width, doorThickness);
                    break;
                case LEFT:
                    // Draw a door on the left edge
                    g.fillRect(col, row, doorThickness, height);
                    break;
                case RIGHT:
                    // Draw a door on the right edge
                    g.fillRect(col + width - doorThickness, row, doorThickness, height);
                    break;
                default:
                    break;
            }
		}
	}
}
