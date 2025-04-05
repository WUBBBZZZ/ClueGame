package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.Player;
import clueGame.HumanPlayer;
import clueGame.ComputerPlayer;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Solution;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;

class GameSolutionTest {
	private static Board board;

	@BeforeAll
	public static void setUp() {
		System.out.println("game solution test");
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	//Tests if solution matches accusation
	//Similar tests follow below:
	@Test
	public void testCheckAccusationCorrect() {
		Card card1 = new Card("Bar", CardType.ROOM);
		Card card2 = new Card("Genji", CardType.SUSPECT);
		Card card3 = new Card("NanoBlade", CardType.WEAPON);
		Solution testSolution = new Solution(card1, card2, card3);
		Assert.assertTrue(board.checkAccusation("Bar", "Genji", "NanoBlade", testSolution));
	}
	
	//solution with wrong room
	@Test
	public void testCheckAccusationRoom() {
		Card card1 = new Card("Bar", CardType.ROOM);
		Card card2 = new Card("Genji", CardType.SUSPECT);
		Card card3 = new Card("NanoBlade", CardType.WEAPON);
		Solution testSolution = new Solution(card1, card2, card3);
		Assert.assertFalse(board.checkAccusation("Garage", "Genji", "NanoBlade", testSolution));
	}
	
	//solution with wrong person
	@Test
	public void testCheckAccusationPerson() {
		Card card1 = new Card("Bar", CardType.ROOM);
		Card card2 = new Card("Genji", CardType.SUSPECT);
		Card card3 = new Card("NanoBlade", CardType.WEAPON);
		Solution testSolution = new Solution(card1, card2, card3);
		Assert.assertFalse(board.checkAccusation("Bar", "Sombra", "NanoBlade", testSolution));
	}
	
	//solution with wrong weapon
	@Test
	public void testCheckAccusationWeapon() {
		Card card1 = new Card("Bar", CardType.ROOM);
		Card card2 = new Card("Genji", CardType.SUSPECT);
		Card card3 = new Card("NanoBlade", CardType.WEAPON);
		Solution testSolution = new Solution(card1, card2, card3);
		Assert.assertFalse(board.checkAccusation("Bar", "Genji", "DaedricMace", testSolution));
	}
	
	/*Player disproves a suggestion, tests include:
If player has only one matching card it should be returned
If players has >1 matching card, returned card should be chosen randomly
If player has no matching cards, null is returned */
	@Test
	public void testDisproveSuggestion() {
		Card card1 = new Card("Bar", CardType.ROOM);
		Card card2 = new Card("Genji", CardType.SUSPECT);
		Card card3 = new Card("NanoBlade", CardType.WEAPON);
		Solution testSolution1 = new Solution(card1, card2, card3);
		Assert.assertTrue("Bar" == board.disproveSuggestion("Bar", "Reinhardt", "NanoBlade", testSolution1));
		Assert.assertTrue(null == board.disproveSuggestion("Bathroom", "Reinhardt", "DiamondSword", testSolution1));
	}
	
	/*Handle a suggestion made, tests include:
Suggestion no one can disprove returns null
Suggestion only suggesting player can disprove returns null
Suggestion only human can disprove returns answer (i.e., card that disproves suggestion)
Suggestion that two players can disprove, correct player (based on starting with next player in list) returns answer
 */
	
	@Test
	public void testHandleSuggestion() {
		
	}
}
