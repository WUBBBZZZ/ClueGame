package clueGame;

import java.util.ArrayList;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private boolean hasPass;
	private char connection;
	private BoardCell pass;
	private ArrayList<BoardCell> doors = new ArrayList<BoardCell>();
	
	
	
	public Room(String name) {
		this.name = name;
	}
	public void setPassBool(boolean bool, char con) {
		this.hasPass = true;
		this.connection = con;
	}
	public boolean getPassBool() {
		return this.hasPass;
	}
	public void setPass(BoardCell cell) {
		this.pass = cell;
	}
	public BoardCell getPass() {
		return this.pass;
	}
	
	public char getConnection() {
		return this.connection;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}
	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}
	public void setDoorCell(BoardCell cell) {
		this.doors.add(cell);
	}
	public ArrayList<BoardCell> getDoorCell(){
		return this.doors;
	}
	@Override
	public String toString() {
		return this.name;
	}
}

