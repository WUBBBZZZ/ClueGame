package experiment;

import java.util.Map;


public class Board {
    private static Board theInstance = new Board();
    private BoardCell[][] grid;
    private int numRows;
    private int numColumns;
    private String layoutConfigFile;
    private String setupConfigFile;
    private Map<Character, Room> roomMap;

    private Board() {
        super();
    }

    public static Board getInstance() {
        return theInstance;
    }

    public void initialize() {
        // Implementation for initializing the board
    }
}

