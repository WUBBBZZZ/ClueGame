package tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import experiment.Board;

public class FileInitTests {
    private Board board;
    
    @BeforeEach
    public void setUp() {
        board = Board.getInstance();
        board.initialize();
    }
    
    @Test
    public void testLoadSetupConfig() {
        assertDoesNotThrow(() -> board.loadSetupConfig());
    }
    
    @Test
    public void testLoadLayoutConfig() {
        assertDoesNotThrow(() -> board.loadLayoutConfig());
    }
    
    @Test
    public void testCorrectNumberOfRooms() {
        // Example check, modify based on your actual config file
        assertEquals(9, board.getRoomCount());
    }
    
    @Test
    public void testDoorways() {
        // Example check for at least one doorway in each direction
        assertTrue(board.hasDoorwayInDirection("DOWN"));
        assertTrue(board.hasDoorwayInDirection("UP"));
    }
    
    @Test
    public void testBadFileHandling() {
        assertThrows(FileNotFoundException.class, () -> {
            Board badBoard = Board.getInstance();
            badBoard.loadSetupConfig("nonexistentFile.txt");
        });
    }
}
