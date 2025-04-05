package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;

class ComputerAITest {
	
	private static Board board;

	@BeforeAll
	public static void setUp() {
		System.out.println("ComputerAITest test");
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	/* Computer player create a suggestion, tests include:
Room matches current location
If only one weapon not seen, it's selected
If only one person not seen, it's selected (can be same test as weapon)
If multiple weapons not seen, one of them is randomly selected
If multiple persons not seen, one of them is randomly selected
*/
	@Test
	public void selectTargets()  {
		fail("Not yet implemented");
	}
	
	
	/*Computer player select a target, tests include:
if no rooms in list, select randomly
if room in list that has not been seen, select it
if room in list that has been seen, each target (including room) selected randomly */
	@Test
	public void createSuggestion() {
		
	}
}
