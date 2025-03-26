package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	@Test
	public void adjWalkTest() {
		//Locations with only walkways as adjacent locations
		Set<BoardCell> testList = board.getAdjList(8, 14);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(8, 13)));
		assertTrue(testList.contains(board.getCell(8, 15)));
		assertTrue(testList.contains(board.getCell(7, 14)));
		assertTrue(testList.contains(board.getCell(9, 14)));
	}
	
	@Test
	public void adjRoomTest() {
		//Locations within rooms not center. Note, this test is allowed 
		//to pass even for failing test.  (Should have empty adjacency list) 
		Set<BoardCell> testList = board.getAdjList(20, 6);
		assertEquals(0, testList.size());
	}
	
	@Test
	public void adjEdgeTest() {
		//Locations that are at each edge of the board
		Set<BoardCell> testList = board.getAdjList(14, 0);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(14, 1)));
	}
	
	@Test
	public void adjWalkRoomTest() {
		//Locations that are beside a room cell that is not a doorway
		Set<BoardCell> testList = board.getAdjList(21, 3);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(20, 3)));
		assertTrue(testList.contains(board.getCell(21, 4)));
		assertFalse(testList.contains(board.getCell(21, 2)));
	}
	
	@Test
	public void adjDoorTest() {
		//Locations that are doorways
		Set<BoardCell> testList = board.getAdjList(2, 3);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(2, 4)));
		assertTrue(testList.contains(board.getCell(1, 3)));
		assertTrue(testList.contains(board.getCell(3, 3)));
		assertTrue(testList.contains(board.getCell(2, 1)));
	}
	
	@Test
	public void adjSecretTest() {
		//Locations that are connected by secret passage
		Set<BoardCell> testList = board.getAdjList(0, 0);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(19, 0)));

	}
	
	@Test
	public void targWalkTest() {
		//Targets along walkways, at various distances
		// test a roll of 1
		board.calcTargets(board.getCell(8, 14), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(8, 13)));
		assertTrue(targets.contains(board.getCell(8, 15)));	
		assertTrue(targets.contains(board.getCell(9, 14)));
		assertTrue(targets.contains(board.getCell(7, 14)));
		
		//Test a roll of 2
		board.calcTargets(board.getCell(8, 14), 2);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(6, 14)));
		assertTrue(targets.contains(board.getCell(7, 15)));	
		assertTrue(targets.contains(board.getCell(8, 16)));
		assertTrue(targets.contains(board.getCell(9, 15)));
		assertTrue(targets.contains(board.getCell(10, 14)));
		assertTrue(targets.contains(board.getCell(9, 13)));	
		assertTrue(targets.contains(board.getCell(7, 13)));
		
	}
	
	@Test
	public void targUserRoomTest() {
		//Targets that allow the user to enter a room
		
		//Test a roll of 1
		board.calcTargets(board.getCell(2, 3), 1);
		Set<BoardCell> targets= board.getTargets();
		//System.out.println(targets);
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(8, 13)));
		assertTrue(targets.contains(board.getCell(8, 15)));	
		assertTrue(targets.contains(board.getCell(9, 14)));
		assertTrue(targets.contains(board.getCell(7, 14)));
		
	}
	
	@Test
	public void targRoomNoSecretTest() {
		//Targets calculated when leaving a room without secret passage
		
		// Test a roll of 1
		board.calcTargets(board.getCell(2, 8), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(3, 11)));
		assertTrue(targets.contains(board.getCell(3, 4)));
	}
	
	@Test
	public void targRoomSecretTest() {
		//Targets calculated when leaving a room with secret passage
		
		//Test a roll of 1
		board.calcTargets(board.getCell(5, 20), 1);
		Set<BoardCell> targets= board.getTargets();
		//System.out.println(targets);
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(5, 18)));
		assertTrue(targets.contains(board.getCell(7, 20)));	
	}
	
	@Test
	public void targPlayerBlockTest() {
		//Targets that reflect blocking by other players
		//The tile above the selected tile is a player
		
		//Test a roll of 1
		board.calcTargets(board.getCell(11, 6), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(11, 5)));
		assertTrue(targets.contains(board.getCell(12, 6)));
	}
	
}
