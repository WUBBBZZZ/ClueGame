package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;

class GameSolutionTest {
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
	/* Check an accusation, tests include:
	solution that is correct
	solution with wrong person
	solution with wrong weapon
	solution with wrong room*/
	@Test
	public void checkAccusation() {
		Assert.assertTrue(0 == 1);
	}
	
	/*Player disproves a suggestion, tests include:
If player has only one matching card it should be returned
If players has >1 matching card, returned card should be chosen randomly
If player has no matching cards, null is returned */
	@Test
	public void disproveSuggestion() {
		Assert.assertTrue(0 == 1);
	}
	
	/*Handle a suggestion made, tests include:
Suggestion no one can disprove returns null
Suggestion only suggesting player can disprove returns null
Suggestion only human can disprove returns answer (i.e., card that disproves suggestion)
Suggestion that two players can disprove, correct player (based on starting with next player in list) returns answer
 */
	
	@Test
	public void handleSuggestion() {
		Assert.assertTrue(0 == 1);
	}
}
