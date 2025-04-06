package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;


class ComputerAITest {
	
	private static Board board;

	@BeforeAll
	public static void setUp() {
		
		Board.resetInstance();
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
	public void testCreateSuggestion() {
		Player testAI = board.getTestPlayer();
		System.out.println(testAI.getName());
		testAI.addSeen(testAI.getUnseen().get(3));
		testAI.setPos(2, 1);
		Card room = board.getRoomCard(2, 1);
		Solution suggestion = testAI.createSuggestion(room);
		System.out.println(testAI.getSeen());
		System.out.println(testAI.getUnseen());
		System.out.println(testAI.getHand());
		System.out.println(suggestion.getPersonSol());
		System.out.println(suggestion.getRoomSol());
		System.out.println(suggestion.getWeaponSol());
		int a = 0;
		int b = 0;
		int c = 0;
		
		for (int i = 0; i < 1000; i++) {
			if (!suggestion.getRoomSol().equals(room)) { //room suggestion should always be the room we are in
				a++;
			}
			if (!testAI.getWeap().contains(suggestion.getWeaponSol())) { //never suggest from a card in our hand or one we have seen
				b++;
			}
			if (!testAI.getPlayers().contains(suggestion.getPersonSol())) {//never suggest from a card in our hand or one we have seen
				c++;
			}
		}
		Assert.assertTrue(a == 0 && b == 0 && c == 0);
	}

	@Test
	public void testSelectTarget() {
		
	}
}
