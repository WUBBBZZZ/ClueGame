//TODO add class string
package clueGame;
import java.util.*;

import experiment.TestBoardCell;

import java.io.*;	
public class Board {
	private BoardCell[][] grid;
	private int numRows, numColumns;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap; 
	private Map<Character, String> cellMap; 
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private static final char WALKWAY_INITIAL = 'W';
	private static final char SECRET_PASSAGE_IDENTIFIER = 'S';
	public static final int NO_SECRET_PASSAGE = 0;

	private static final char DOOR_LEFT = '<';
	private static final char DOOR_UP = '^';
	private static final char DOOR_RIGHT = '>';
	private static final char DOOR_DOWN = 'v';
	private static final char ROOM_CENTER = '*';
	private static final char ROOM_LABEL = '#';
	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();

	// Constructor is private to ensure only one can be created
	private Board() {
		super() ;
		roomMap = new HashMap<Character, Room>();
		cellMap = new HashMap<Character, String>();
	}

	// This method returns the only Board
	public static Board getInstance() {
		if(theInstance == null) {
			theInstance = new Board();
		}
		return theInstance;
	}
	
	//This method is only used for testing purposes, to reset the Board instance per successive test.
	public static void resetInstance() {
        theInstance = null;
    }
	
	public Set<BoardCell> getAdjList(int row, int col){
		BoardCell cell = this.getCell(row, col);
		
		return cell.getAdjList();
	}
	
	// Calculates targets for a move from startCell of length pathlength.
	public void calcTargets(BoardCell startCell, int pathlength) {
		targets.clear();
		visited.clear();
		visited.add(startCell);
		this.findAllTargets(startCell, pathlength);
	}
	// Calls calcTargets recursively
	public void findAllTargets(BoardCell cell, int pathlength){
		if (pathlength < 1) {return;}
		Set<BoardCell> adjList = cell.getAdjList();
		for(BoardCell adj:adjList) {
			if(visited.contains(adj) || adj.getOccupied()){
				if (cell.isDoorway() && adj.isRoom() && !visited.contains(adj)) {//if room is occupied but not visited then we can enter it 
					targets.add(this.getRoom(adj).getCenterCell());
				} else {continue;}
			}
			if (cell.isDoorway() && adj.isRoom()) {
				targets.add(this.getRoom(adj).getCenterCell());
			}
			
			//walkways can only add other walkways to targets
			if (!cell.isRoom() && !cell.isDoorway() && !adj.isRoom()) {
				visited.add(adj);
				if (pathlength == 1){
					targets.add(adj);
					/*if (adj.isDoorway() && !adj.getOccupied()) { DO NOT DELETE THIS CODE it is the logic for entering rooms but is not required yet
						DoorDirection temp = adj.getDoorDirection();
						int r = adj.getRow();
						int c = adj.getCol();
						if (temp == DoorDirection.UP) {
							targets.add(this.getRoom(this.getCell(r - 1, c).getInitial()).getCenterCell());
						}else if (temp == DoorDirection.DOWN) {
							targets.add(this.getRoom(this.getCell(r + 1, c).getInitial()).getCenterCell());
						}else if (temp == DoorDirection.RIGHT) {
							targets.add(this.getRoom(this.getCell(r, c + 1).getInitial()).getCenterCell());
						}else if (temp == DoorDirection.LEFT) {
							targets.add(this.getRoom(this.getCell(r, c - 1).getInitial()).getCenterCell());
						}
					}*/
				} else {
					this.findAllTargets(adj, pathlength - 1);
				}

				visited.remove(adj);
			} else if (!cell.isRoom() && !adj.isRoom()) {
				visited.add(adj);
				if (pathlength == 1){
					targets.add(adj);
				} else {
					this.findAllTargets(adj, pathlength - 1);
				}
				visited.remove(adj);
			} else if (cell.isRoom()){
				for (BoardCell door : this.getRoom(cell).getDoorCell()) {
					if(door.getOccupied()) continue; // Skip blocked doorways
					visited.add(door);
					visited.add(cell);
					if (pathlength == 1) {
						targets.add(door);
					} else {
						this.findAllTargets(door, pathlength - 1);
					}
					visited.remove(door);
					visited.remove(cell);
				}
			} else if (adj.isDoorway()) {
				visited.add(adj);
				if (pathlength == 1) {
					targets.add(adj);
				} else {
					this.findAllTargets(adj, pathlength - 1);
				}
				visited.remove(adj);
			}
			if (adj.getSecretPassage() != NO_SECRET_PASSAGE) {
				targets.add(this.getRoom(adj.getInitial()).getCenterCell());
			}
		}
	} 
	

	// Returns the cell from the board at row, col
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}


	// Gets the targets last created by calcTargets()
	public Set<BoardCell> getTargets(){
		return targets;
	}


	// Sets up the board and initializes the adjacency list
	public void setupBoard() {
		grid = new BoardCell[numRows][numColumns];
		for(int i = 0;i<numRows;i++) {
			for(int j = 0;j<numColumns;j++) {
				grid[i][j] = new BoardCell(i, j);
				grid[i][j].setOccupied(false);
				grid[i][j].setRoom(false);
			}
		}
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
	}


	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize(){
		Board board = Board.getInstance();
		try {
			board.loadSetupConfig();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			board.loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.makeAdjList();
	}

	
	public void loadSetupConfig() throws BadConfigFormatException{
		String finishedSetup = "./data/" + setupConfigFile;
		try(Scanner setupScanner = new Scanner(new File(finishedSetup))){
			
			// Read from setupConfigFile
			while(setupScanner.hasNext()) {
				String line = setupScanner.nextLine().trim();
				String[] parts = line.split("\\s*,\\s*");
				
				// Skip empty lines or comments
	            if (line.isEmpty() || line.startsWith("//")) {
	                continue;
	            }
				
				// Check each line for rooms and spaces
				if(line.startsWith("Room")) {
					if(parts.length != 3) {
						String message = "Invalidformat, expecting a name and symbol for each room, bad line: \" " + line + "\".";
						throw new BadConfigFormatException(message);
					}
					
					String roomName = parts[1].trim();
					Room newRoom = new Room(roomName);				
					String symbol = parts[2].trim();
					roomMap.put(symbol.charAt(0), newRoom);
					
					
				} else if(line.startsWith("Space")) {
					if(parts.length != 3) {
						String message = "Invalid format, expecting aname and symbol for each type of space, bad line: \" " + line + "\".";
						throw new BadConfigFormatException(message);
					}
					
					String spaceName = parts[1].trim();
					String spaceSymbol = parts[2].trim();
					Room newRoom = new Room(spaceName);
					roomMap.put(spaceSymbol.charAt(0), newRoom);
					cellMap.put(spaceSymbol.charAt(0), spaceName);
					
				} else {
					continue;
				}
			}
			
			
		} catch(FileNotFoundException e){
			System.out.println("File not found, please try again.");
		}
		
	}
	
	public void makeAdjList() {
	    for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numColumns; j++) {
	            BoardCell cell = getCell(i, j);

	            if (cell.isDoorway()) {
	                handleDoorwayAdjacency(cell, i, j);
	            }

	            if (!cell.isRoom()) {
	                handleWalkwayAdjacency(cell, i, j);
	            } else {
	                handleRoomAdjacency(cell);
	            }
	        }
	    }
	}
	private void handleDoorwayAdjacency(BoardCell cell, int row, int col) {
	    DoorDirection dir = cell.getDoorDirection();
	    switch (dir) {
	        case UP -> cell.addAdjacency(getRoom(getCell(row - 1, col)).getCenterCell());
	        case DOWN -> cell.addAdjacency(getRoom(getCell(row + 1, col)).getCenterCell());
	        case LEFT -> cell.addAdjacency(getRoom(getCell(row, col - 1)).getCenterCell());
	        case RIGHT -> cell.addAdjacency(getRoom(getCell(row, col + 1)).getCenterCell());
	    }
	}
	private void handleWalkwayAdjacency(BoardCell cell, int row, int col) {
	    if (row > 0) {
	        BoardCell up = getCell(row - 1, col);
	        if (up.getInitial() == WALKWAY_INITIAL || up.isDoorway()) cell.addAdjacency(up);
	    }
	    if (col > 0) {
	        BoardCell left = getCell(row, col - 1);
	        if (left.getInitial() == WALKWAY_INITIAL || left.isDoorway()) cell.addAdjacency(left);
	    }
	    if (row < numRows - 1) {
	        BoardCell down = getCell(row + 1, col);
	        if (down.getInitial() == WALKWAY_INITIAL || down.isDoorway()) cell.addAdjacency(down);
	    }
	    if (col < numColumns - 1) {
	        BoardCell right = getCell(row, col + 1);
	        if (right.getInitial() == WALKWAY_INITIAL || right.isDoorway()) cell.addAdjacency(right);
	    }
	}
	private void handleRoomAdjacency(BoardCell cell) {
	    Room room = getRoom(cell);
	    for (BoardCell door : room.getDoorCell()) {
	        cell.addAdjacency(door);
	    }

	    if (cell.hasSecretPassage()) {
	        BoardCell target = getRoom(cell.getSecretPassage()).getCenterCell();
	        cell.addAdjacency(target);
	    }
	}





	private List<String[]> readLayoutFile(String filePath) throws BadConfigFormatException {
	    List<String[]> layoutLines = new ArrayList<>();
	    try (Scanner scanner = new Scanner(new File(filePath))) {
	        int expectedCols = -1;
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();
	            if (line.isEmpty()) continue;

	            String[] tokens = line.split(",\\s*");
	            if (expectedCols == -1) expectedCols = tokens.length;
	            else if (tokens.length != expectedCols) {
	                throw new BadConfigFormatException("Column mismatch in layout file.");
	            }

	            layoutLines.add(tokens);
	        }
	    } catch (FileNotFoundException e) {
	        throw new BadConfigFormatException("Layout file not found: " + filePath);
	    }
	    return layoutLines;
	}
	private void buildGridFromLayout(List<String[]> layoutLines) throws BadConfigFormatException {
	    numRows = layoutLines.size();
	    numColumns = layoutLines.get(0).length;
	    setupBoard();

	    for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numColumns; j++) {
	            String code = layoutLines.get(i)[j];
	            initializeCell(code, i, j); // Already uses getCell(i, j) once internally
	        }
	    }
	}

	private void initializeCell(String code, int row, int col) throws BadConfigFormatException {
	    char symbol = code.charAt(0);
	    if (!roomMap.containsKey(symbol)) {
	        throw new BadConfigFormatException("Symbol not found in room map: " + symbol);
	    }

	    BoardCell cell = getCell(row, col);
	    cell.setInitial(symbol);
	    Room room = roomMap.get(symbol);

	    if (!cellMap.containsKey(symbol)) {
	        cell.setRoom(true);
	    }

	    if (code.length() > 1) {
	        char marker = code.charAt(1);
	        switch (marker) {
	            case DOOR_LEFT -> {
	                cell.setDirection('L');
	                cell.setDoorway(true);
	            }
	            case DOOR_RIGHT -> {
	                cell.setDirection('R');
	                cell.setDoorway(true);
	            }
	            case DOOR_UP -> {
	                cell.setDirection('U');
	                cell.setDoorway(true);
	            }
	            case DOOR_DOWN -> {
	                cell.setDirection('D');
	                cell.setDoorway(true);
	            }
	            case ROOM_CENTER -> {
	                cell.setCenter(true);
	                room.setCenterCell(cell);
	            }
	            case ROOM_LABEL -> {
	                cell.setLabel(true);
	                room.setLabelCell(cell);
	            }
	            default -> {
	                if (roomMap.containsKey(marker)) {
	                    room.setPassBool(true, marker);
	                } else {
	                    throw new BadConfigFormatException("Invalid marker in cell: " + code);
	                }
	            }
	        }
	    }
	}
	private void updateDoorwaysAndSecretPassages() {
	    for (int i = 0; i < getNumRows(); i++) {
	        for (int j = 0; j < getNumColumns(); j++) {
	            BoardCell cell = getCell(i, j);

	            // Secret Passage setup
	            if (cell.isRoom()) {
	                Room room = getRoom(cell);
	                if (room.getPassBool()) {
	                    cell.setSecretPassage(room.getConnection());
	                }
	            }

	            // Doorway to Room linkage
	            if (cell.isDoorway()) {
	                DoorDirection dir = cell.getDoorDirection();
	                switch (dir) {
	                    case UP -> getRoom(getCell(i - 1, j)).setDoorCell(cell);
	                    case DOWN -> getRoom(getCell(i + 1, j)).setDoorCell(cell);
	                    case LEFT -> getRoom(getCell(i, j - 1)).setDoorCell(cell);
	                    case RIGHT -> getRoom(getCell(i, j + 1)).setDoorCell(cell);
	                }
	            }
	        }
	    }
	}


	public void loadLayoutConfig() throws BadConfigFormatException {
	    String finishedLayout = "./data/" + layoutConfigFile;
	    List<String[]> layoutLines = readLayoutFile(finishedLayout);
	    buildGridFromLayout(layoutLines);
	    updateDoorwaysAndSecretPassages();
	}





	public Room getRoom(char c) {
		return roomMap.get(c);
	}


	public Room getRoom(BoardCell cell) {
		char character = cell.getInitial();
		return roomMap.get(character);
	}

	public int getNumRows() {
		return numRows;
	}


	public int getNumColumns() {
		return numColumns;
	}


	public void setConfigFiles(String layout, String setup) {
		this.layoutConfigFile = layout;
		this.setupConfigFile = setup;

	}


}
