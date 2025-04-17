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
	//List of room names, for cards
	private ArrayList<String> rooms;
	//For Player and Card class
	private ArrayList<String> people;
	//colors that associate with respective (index based) person
	private ArrayList<String> colors;
	//For Card class
	private ArrayList<String> weapons;
	//The deck of cards itself
	private ArrayList<Card> cards;
	private ArrayList<Player> players;
	//copy of cards
	private ArrayList<Card> cardsCopy;
	private Solution theSolution;
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
		people = new ArrayList<String>();
		weapons = new ArrayList<String>();
		cards = new ArrayList<Card>();
		cardsCopy = new ArrayList<Card>();
		rooms = new ArrayList<String>();
		colors = new ArrayList<String>();
		players = new ArrayList<Player>();
	}

	// This method returns the only Board
	public static Board getInstance() {
		if(theInstance == null) {
			theInstance = new Board();
			//System.out.println("created new");
		} else {
			//System.out.println("returned old");
		}
		//System.out.println(theInstance.getPeople());
		return theInstance;
	}
	public void setSolution(Solution newSol) {
		theSolution = newSol;
	}
	//This method is only used for testing purposes, to reset the Board instance per successive test.
	public static void resetInstance() {
		theInstance.roomMap.clear();
		theInstance.cellMap.clear();
		theInstance.people.clear();
		theInstance.weapons.clear();
		theInstance.cards.clear();
		theInstance.cardsCopy.clear();
		theInstance.rooms.clear();
		theInstance.colors.clear();
		theInstance.players.clear();
		for (Player player : theInstance.players) {
			player.reset();
		}
        theInstance = null;
    }
	
	// getter for the adjacency list of a cell 
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
			// doorway case - allows the nearby room tile's center to be added to target list
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
		
		for (String name : rooms ) {
			cards.add(new Card(name, CardType.ROOM)) ;
			cardsCopy.add(new Card(name, CardType.ROOM)) ;
		}
		for (String name : people ) {
			cards.add(new Card(name, CardType.SUSPECT)) ;
			cardsCopy.add(new Card(name, CardType.SUSPECT)) ;
		}
		for (String name : weapons ) {
			cards.add(new Card(name, CardType.WEAPON)) ;
			cardsCopy.add(new Card(name, CardType.WEAPON)) ;
		}
		
		//Player objects are made below
		HumanPlayer player1 = new HumanPlayer(people.get(0), colors.get(0), 0, 3);
		this.getCell(0, 3).setOccupied(true);
		players.add(player1);
		ComputerPlayer player2 = new ComputerPlayer(people.get(1), colors.get(1), 6, 0);
		this.getCell(6, 0).setOccupied(true);
		players.add(player2);
		ComputerPlayer player3 = new ComputerPlayer(people.get(2), colors.get(2), 19, 3);
		this.getCell(19, 3).setOccupied(true);
		players.add(player3);
		ComputerPlayer player4 = new ComputerPlayer(people.get(3), colors.get(3), 19, 13);
		this.getCell(19, 13).setOccupied(true);
		players.add(player4);
		ComputerPlayer player5 = new ComputerPlayer(people.get(4), colors.get(4), 16, 20);
		this.getCell(16, 20).setOccupied(true);
		players.add(player5);
		ComputerPlayer player6 = new ComputerPlayer(people.get(5), colors.get(5), 0, 20);
		this.getCell(0, 20).setOccupied(true);
		players.add(player6);
		
		//deals deck to solution
		Random rand = new Random();
		
		//For random room card
		int randomNumber1 = rand.nextInt(9);
		//For random player card
		int randomNumber2 = rand.nextInt(9, 15);
		//For random weapon card
		int randomNumber3 = rand.nextInt(15, 21);
		
		theSolution = new Solution(cardsCopy.get(randomNumber1), cardsCopy.get(randomNumber2), cardsCopy.get(randomNumber3));
		cardsCopy.remove(randomNumber3);
		cardsCopy.remove(randomNumber2);
		cardsCopy.remove(randomNumber1);
		for (Player player : players) {
			if (player instanceof ComputerPlayer) {
				for (String room : rooms) {
					char temp = room.charAt(0);
					Room tempRoom = this.getRoom(temp);
					((ComputerPlayer) player).initUnseen(tempRoom);
				}
			} else {
				continue;
			}
		}
		
		//deals rest of deck to players
		for (int j = 0; j < 18; j++) {
			randomNumber1 = rand.nextInt(18-j);
			if (j == 0 || j == 1 || j == 2) {
				player1.updateHand(cardsCopy.get(randomNumber1));
				cardsCopy.remove(randomNumber1);
			} else if (j == 3 || j == 4 || j == 5) {
				player2.updateHand(cardsCopy.get(randomNumber1));
				cardsCopy.remove(randomNumber1);
			} else if (j == 6 || j == 7 || j == 8) {
				player3.updateHand(cardsCopy.get(randomNumber1));
				cardsCopy.remove(randomNumber1);
			} else if (j == 9 || j == 10 || j == 11) {
				player4.updateHand(cardsCopy.get(randomNumber1));
				cardsCopy.remove(randomNumber1);
			} else if (j == 12 || j == 13 || j == 14) {
				player5.updateHand(cardsCopy.get(randomNumber1));
				cardsCopy.remove(randomNumber1);
			} else if (j == 15 || j == 16 || j == 17) {
				player6.updateHand(cardsCopy.get(randomNumber1));
				cardsCopy.remove(randomNumber1);
			}
		}
		
		for (Card card : cards) {
			if (card.equals(theSolution.getPersonSol())) {
				continue;
			} else if (card.equals(theSolution.getRoomSol())) {
				continue;
			} else if (card.equals(theSolution.getWeaponSol())) {
				continue;
			}
			
			for (Player player : players){
				if (card.getCardName() == player.getName()) {
					continue;
				}
				boolean inHand = false;
				for(Card hand : player.getHand()) {
					if (card.equals(hand)) {
						inHand = true;
					}
				}
				if (inHand == false) {
					player.addUnseen(card);
				}
			}
		}
	}

	// Translates a given config file for a board to assist with associating symbols to room types.
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
					rooms.add(roomName);
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
					
				} else if(line.startsWith("Person")) {
					if(parts.length != 3) {
						String message = "Invalidformat, expecting a name and color for each Person, bad line: \" " + line + "\".";
						throw new BadConfigFormatException(message);
					}
					
					String playerName = parts[1].trim();
					String playerColor = parts[2].trim();
					people.add(playerName);
					colors.add(playerColor);
					
					
				
					} else if(line.startsWith("Weapon")) {
						if(parts.length != 2) {
							String message = "Invalidformat, expecting a name after weapon, bad line: \" " + line + "\".";
							throw new BadConfigFormatException(message);
						}
						
						String weaponName = parts[1].trim();
						weapons.add(weaponName);
						
						} else {
					continue;
				}
			}
			
			
		} catch(FileNotFoundException e){
			System.out.println("File not found, please try again.");
		}
		
	}
	
	// Handles creation of the adjacency list of a cell.
	// Needs information about the board, which is why it's created here, and not on BoardCell.java
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
	
	//helper method for makeAdjList. 
	//handles the adj lists for doorways.
	private void handleDoorwayAdjacency(BoardCell cell, int row, int col) {
	    DoorDirection dir = cell.getDoorDirection();
	    switch (dir) {
	        case UP -> cell.addAdjacency(getRoom(getCell(row - 1, col)).getCenterCell());
	        case DOWN -> cell.addAdjacency(getRoom(getCell(row + 1, col)).getCenterCell());
	        case LEFT -> cell.addAdjacency(getRoom(getCell(row, col - 1)).getCenterCell());
	        case RIGHT -> cell.addAdjacency(getRoom(getCell(row, col + 1)).getCenterCell());
	    }
	}
	
	//helper method for makeAdjList. 
	//handles the adj lists for walkways.
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
	
	//helper method for makeAdjList. 
	//handles the adj lists for rooms.
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

	// Helper function for loadLayoutConfig()
	// reads the rows of a csv layout file and translates to a String[] ArrayList
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
	
	// Helper function for loadLayoutConfig()
	// initializes every cell from data from layoutLines().
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

	// Helper function for buildGridFromLayout()
	// associates data (the variable "code") into some cell
	private void initializeCell(String code, int row, int col) throws BadConfigFormatException {
	    char symbol = code.charAt(0);
	    if (!roomMap.containsKey(symbol)) {
	        throw new BadConfigFormatException("Symbol not found in room map: " + symbol);
	    }

	    BoardCell cell = getCell(row, col);
	    cell.setInitial(symbol, this.getRoom(symbol).getName());
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
	
	// helper function for loadLayoutConfig()
	// provides functionality for connecting doorways to room centers and connecting secret passages
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

	//Translates the csv file into a board.
	public void loadLayoutConfig() throws BadConfigFormatException {
	    String finishedLayout = "./data/" + layoutConfigFile;
	    List<String[]> layoutLines = readLayoutFile(finishedLayout);
	    buildGridFromLayout(layoutLines);
	    updateDoorwaysAndSecretPassages();
	}

	// getter method for a room letter
	public Room getRoom(char c) {
		return roomMap.get(c);
	}

	// getter method for a room cell
	public Room getRoom(BoardCell cell) {
		char character = cell.getInitial();
		return roomMap.get(character);
	}

	// getter method for number of rows
	public int getNumRows() {
		return numRows;
	}

	// getter method for number of columns
	public int getNumColumns() {
		return numColumns;
	}
	
	// getter for people set
	public ArrayList<String> getPeople() {
		return people;
	}
	
	// getter for weapon set
		public ArrayList<String> getWeapons() {
			return weapons;
		}
	
	//getter for room names set
		public ArrayList<String> getRoomList() {
			return rooms;
		}

	// Setter method for config file Strings
	public void setConfigFiles(String layout, String setup) {
		this.layoutConfigFile = layout;
		this.setupConfigFile = setup;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public ArrayList<Card> getCardsCopy() {
		return cardsCopy;
	}
	
	public boolean checkAccusation(String string1, String string2, String string3, Solution solution) {
		if (!string1.equals(solution.getRoomSol().getCardName())) {
			return false;
		} else if (!string2.equals(solution.getPersonSol().getCardName())) {
			return false;
		} else if (!string3.equals(solution.getWeaponSol().getCardName())) {
			return false;
		} else {
			return true;
		}
		
	}
	public String disproveSuggestion(String string1, String string2, String string3, Solution solution) {
		ArrayList<String> disprovable = new ArrayList<String>();
		if (string1.equals(solution.getRoomSol().getCardName())) {
			disprovable.add(string1);
		}
		if (string2.equals(solution.getPersonSol().getCardName())) {
			disprovable.add(string2);
		}
		if (string3.equals(solution.getWeaponSol().getCardName())) {
			disprovable.add(string3);
		}
		if(disprovable.size() > 0) {
			return disprovable.get(0);
		} else {
			return null;
		}
	}
	public Player getTestPlayer() {
		return players.get(2);
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public Card getRoomCard(int r, int c) {
		String name = this.getRoom(this.getCell(r, c).getInitial()).getName();
		for (Card card : this.cards) {
			if (card.getCardName().equals(name)) {
				return card;
			}
		}
		return null;
	}
	
	
	public Card handleSuggestion(Solution suggestion, Player accuser) {
		for (Player player : players) {
			if (player.equals(accuser)) {
				continue;
			}
			if (player.disproveSuggestion(suggestion) == null) {
				continue;
			} else {
				return player.disproveSuggestion(suggestion);
			}
		}
		return null;
	}
	public Room doorToRoom(BoardCell cell) {
		DoorDirection dir = cell.getDoorDirection();
		int i = cell.getRow();
		int j = cell.getCol();
        switch (dir) {
            case UP -> {return this.getRoom(getCell(i - 1, j));}
            case DOWN -> {return this.getRoom(getCell(i + 1, j));}
            case LEFT -> {return this.getRoom(getCell(i, j - 1));}
            case RIGHT -> {return this.getRoom(getCell(i, j + 1));}
        }
		return null;
	}
}
