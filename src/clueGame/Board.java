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
		cell = this.getCellC(cell);
		Set<BoardCell> adjList = cell.getAdjList();
		for(BoardCell adj:adjList) {
			if(visited.contains(adj) || adj.getOccupied()){
				if (cell.isDoorway() && adj.isRoom() && !visited.contains(adj)) {//if room is occupied but not visited then we can enter it 
					targets.add(this.getRoom(adj).getCenterCell());
					//System.out.print("room added ");
					//System.out.println(adj);
				} else {
					//visited.add(adj);
					//System.out.println(adj);
					continue;
				}
			}
			if (cell.isDoorway() && adj.isRoom()) {
				targets.add(this.getRoom(adj).getCenterCell());
			}
			
			//walkways can only add other walkways to targets
			if (!cell.isRoom() && !cell.isDoorway() && !adj.isRoom()) {
				visited.add(adj);
				if (pathlength == 1){
					targets.add(adj);
					/*if (adj.isDoorway() && !adj.getOccupied()) { DO NOT DELETE THIS CODE
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
			if (adj.getSecretPassage() != 0) {
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
		//BoardCell cell = new BoardCell(-1, -1);
		for(int i = 0;i<numRows;i++) {
			for(int j = 0;j<numColumns;j++) {
				if (grid[i][j].isDoorway() == true) {
					//System.out.println(grid[i][j]);
					DoorDirection dir = grid[i][j].getDoorDirection();
					if (dir == DoorDirection.UP) {
						grid[i][j].addAdjacency(this.getRoom(grid[i-1][j]).getCenterCell());
					} else if (dir == DoorDirection.DOWN) {
						grid[i][j].addAdjacency(this.getRoom(grid[i+1][j]).getCenterCell());
					} else if (dir == DoorDirection.RIGHT) {
						grid[i][j].addAdjacency(this.getRoom(grid[i][j+1]).getCenterCell());
					} else if (dir == DoorDirection.LEFT) {
						grid[i][j].addAdjacency(this.getRoom(grid[i][j-1]).getCenterCell());
					}
				}
				if (grid[i][j].isRoom() == false) {
					if (i > 0){
						if (grid[i - 1][j].getInitial() == 'W' || grid[i - 1][j].isDoorway()) {
							grid[i][j].addAdjacency(grid[i-1][j]);
						}					
					}
					if (j > 0){
						if (grid[i][j-1].getInitial() == 'W' || grid[i][j-1].isDoorway()) {
							grid[i][j].addAdjacency(grid[i][j-1]);
						}
					}
					if (i < numColumns - 1){
						if (grid[i + 1][j].getInitial() == 'W' || grid[i + 1][j].isDoorway()) {
							grid[i][j].addAdjacency(grid[i+1][j]);
						}		
					}
					if (j < numColumns - 1){
						if (grid[i][j+1].getInitial() == 'W' || grid[i][j+1].isDoorway()) {
							grid[i][j].addAdjacency(grid[i][j+1]);
						}		
					}
				} else {
					ArrayList<BoardCell> cells = this.getRoom(grid[i][j]).getDoorCell();
					for (int k = 0; k < cells.size(); k++) {
						grid[i][j].addAdjacency(cells.get(k));
					}
					if (grid[i][j].getSecretPassage() != 0) {
						BoardCell adjac = this.getRoom(grid[i][j].getSecretPassage()).getCenterCell();
						//System.out.println(adjac);
						grid[i][j].addAdjacency(adjac);
					}
				}
			}
		}
	}

	
	
	public void loadLayoutConfig() throws BadConfigFormatException {
		String finishedLayout = "./data/" + layoutConfigFile;
		try(Scanner test = new Scanner(new File(finishedLayout))){
			ArrayList<String[]> boardLayout = new ArrayList<String[]>();
			int numCols = 0;
			int numRows = 0;
			boolean firstRow = true;
			while(test.hasNext()) {
				// Read the next line from the layout file
				String line = new String(test.nextLine().trim());
				if(line.isEmpty()) continue;
				// Split the line into parts using a comma as a delimiter
				String[] spaces = line.split(",\\s*");
				if(firstRow) { 
					numCols = spaces.length; 
					firstRow = false;
				} else {
					if(numCols != spaces.length) {
						
						// Check if the number of columns in each row is consistent
	                    String message = "Column mismatch. column numbers aren't correctly counted between rows. Error is on line " + (numRows + 1) + " of " + finishedLayout;
	                    throw new BadConfigFormatException(message);
					}
				}
				
				numRows++;
				boardLayout.add(spaces);
			}
			
			this.numColumns = numCols;
			this.numRows = numRows;
			this.setupBoard();
			for(int i = 0; i < boardLayout.size();i++) {
				for(int j = 0; j < boardLayout.get(i).length; j++) {
					String[] arr = boardLayout.get(i);
					char symbol = arr[j].charAt(0);
					if(roomMap.containsKey(symbol) && !cellMap.containsKey(symbol)) {
						this.getCell(i, j).setRoom(true);
						this.getCell(i, j).setInitial(symbol);
					}
					
					if(roomMap.containsKey(arr[j].charAt(0))) {
						this.getCell(i, j).setInitial(symbol);
						if(arr[j].length() != 1) {
							switch(arr[j].charAt(1)) {
							
							case '<':
								this.getCell(i, j).setDirection('L');
								this.getCell(i, j).setDoorway(true);
								break;
								
							case '^':
								this.getCell(i, j).setDirection('U');
								this.getCell(i, j).setDoorway(true);
								break;
								
							case '>':
								this.getCell(i, j).setDirection('R');
								this.getCell(i, j).setDoorway(true);
								break;
								
							case 'v':
								this.getCell(i, j).setDirection('D');
								this.getCell(i, j).setDoorway(true);
								break;
								
							case '*':
								this.getCell(i, j).setCenter(true);
								roomMap.get(symbol).setCenterCell(this.getCell(i, j));
								break;
								
							case '#':
								this.getCell(i, j).setLabel(true);
								roomMap.get(symbol).setLabelCell(this.getCell(i, j));
								break;
								
							default:
								if (roomMap.containsKey(arr[j].charAt(1))) {
									if (arr[j].charAt(0) != 'S') {
										//this.getCell(i, j).setSecretPassage(arr[j].charAt(1));
										this.getRoom(this.getCell(i, j)).setPassBool(true, arr[j].charAt(1));
									} else {
										//this.getCell(i, j).setSecretPassage(arr[j].charAt(1));
										this.getRoom(this.getCell(i, j)).setPassBool(true, arr[j].charAt(1));
									}
									break;
								}
								// Handle invalid second character in the layout
	                            String message = "Invalid second character on cell,  Row: " + i + " Column: " + j + " Character: " + (arr[j].charAt(1)) + ".";
	                            throw new BadConfigFormatException(message);
							}
						}

					} else {
						// Handle cases where the character in the layout is not in the setup
	                    String message = "Character is in our Layout and is not in our Setup  " + arr[j] + " .";
	                    throw new BadConfigFormatException(message);
					}
				}
			}
		}
		
		catch (FileNotFoundException e){
			System.out.println("File not found.");
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (this.getCell(i, j).isRoom() == true) {
					if(this.getRoom(this.getCell(i, j)).getPassBool() == true) {
						Room start = this.getRoom(this.getCell(i, j));
						//Room end = this.getRoom(start.getConnection());
						this.getCell(i, j).setSecretPassage(start.getConnection());
					}
				}
				
				if (this.getCell(i, j).isDoorway()) {
					//System.out.println(this.getCell(i, j));
					if (this.getCell(i, j).getDoorDirection() == DoorDirection.DOWN) {
						this.getRoom(this.getCell(i + 1, j)).setDoorCell(this.getCell(i, j));
					}
					if (this.getCell(i, j).getDoorDirection() == DoorDirection.UP) {
						this.getRoom(this.getCell(i - 1, j)).setDoorCell(this.getCell(i, j));
					}
					if (this.getCell(i, j).getDoorDirection() == DoorDirection.LEFT) {
						this.getRoom(this.getCell(i, j - 1)).setDoorCell(this.getCell(i, j));
					}
					if (this.getCell(i, j).getDoorDirection() == DoorDirection.RIGHT) {
						this.getRoom(this.getCell(i, j + 1)).setDoorCell(this.getCell(i, j));
					}
				}
			}
		}
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

	public BoardCell getCellC(BoardCell cell) {
		int r = cell.getRow();
		int c = cell.getCol();
		return this.grid[r][c];
	}


}
