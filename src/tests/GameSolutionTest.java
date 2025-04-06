package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

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
	public void testDisproveSuggestionNull() {
		Card card1 = new Card("Bar", CardType.ROOM);
		Card card2 = new Card("Genji", CardType.SUSPECT);
		Card card3 = new Card("NanoBlade", CardType.WEAPON);
		Solution testSolution1 = new Solution(card1, card2, card3);
		
		Card card4 = new Card("Kitchen", CardType.ROOM);
		Card card5 = new Card("Sombra", CardType.SUSPECT);
		Card card6 = new Card("BFG-10000", CardType.WEAPON);
		HumanPlayer me = new HumanPlayer("n", "c", 0, 0);
		me.updateHand(card4);
		me.updateHand(card5);
		me.updateHand(card6);
		
		
		Assert.assertTrue(null == me.disproveSuggestion(testSolution1));
		}
	
	@Test
	public void testDisproveSuggestionOne() {
		Card card1 = new Card("Bar", CardType.ROOM);
		Card card2 = new Card("Genji", CardType.SUSPECT);
		Card card3 = new Card("NanoBlade", CardType.WEAPON);
		Solution testSolution1 = new Solution(card1, card2, card3);
		
		Card card4 = new Card("Bar", CardType.ROOM);
		Card card5 = new Card("Sombra", CardType.SUSPECT);
		Card card6 = new Card("BFG-10000", CardType.WEAPON);
		HumanPlayer me = new HumanPlayer("n", "c", 0, 0);
		me.updateHand(card4);
		me.updateHand(card5);
		me.updateHand(card6);
		
		
		Assert.assertTrue(card1.getCardName().equals(me.disproveSuggestion(testSolution1).getCardName()));
		}
	
	//This one has all suggestion cards in players deck.
	//So, a loop iterates through the method 1000 times.
	//All cards should be found roughly 333 times each, so each will
	//be checked at least 100 times
	@Test
	public void testDisproveSuggestionMany() {
		Card card1 = new Card("Bar", CardType.ROOM);
		Card card2 = new Card("Genji", CardType.SUSPECT);
		Card card3 = new Card("NanoBlade", CardType.WEAPON);
		Solution testSolution1 = new Solution(card1, card2, card3);
		
		Card card4 = new Card("Bar", CardType.ROOM);
		Card card5 = new Card("Genji", CardType.SUSPECT);
		Card card6 = new Card("NanoBlade", CardType.WEAPON);
		HumanPlayer me = new HumanPlayer("n", "c", 0, 0);
		me.updateHand(card4);
		me.updateHand(card5);
		me.updateHand(card6);
		
		int a = 0;
		int b = 0;
		int c = 0;
		
		for (int i = 0; i < 1000; i++) {
			if (card1.getCardName().equals(me.disproveSuggestion(testSolution1).getCardName())) {
				a++;
			} else if (card2.getCardName().equals(me.disproveSuggestion(testSolution1).getCardName())) {
				b++;
			} else if (card3.getCardName().equals(me.disproveSuggestion(testSolution1).getCardName())) {
				c++;
			}
		}
		Assert.assertTrue(a > 100 && b > 100 && c > 100);
		}
	
	/*Handle a suggestion made, tests include:
Suggestion no one can disprove returns null
Suggestion only suggesting player can disprove returns null
Suggestion only human can disprove returns answer (i.e., card that disproves suggestion)
Suggestion that two players can disprove, correct player (based on starting with next player in list) returns answer
 */
	
	@Test
	public void testHandleSuggestion() {
		ArrayList<Player> players = board.getPlayers();
		for (Player player : players) {
			player.deleteHand();
		}
		Card card1 = new Card("Bar", CardType.ROOM);
		Card card2 = new Card("Genji", CardType.SUSPECT);
		Card card3 = new Card("NanoBlade", CardType.WEAPON);
		Card card4 = new Card("Bathroom", CardType.ROOM);
		Card card5 = new Card("Sombra", CardType.SUSPECT);
		Card card6 = new Card("BFG-10000", CardType.WEAPON);
		Card card7 = new Card("Mancave", CardType.ROOM);
		Card card8 = new Card("Doomfist", CardType.SUSPECT);
		Card card9 = new Card("DiamondSword", CardType.WEAPON);

		players.get(0).updateHand(card1);
		players.get(1).updateHand(card2);
		players.get(2).updateHand(card3);
		players.get(3).updateHand(card4);
		players.get(4).updateHand(card5);
		players.get(5).updateHand(card6);
		Solution sol1 = new Solution(card7, card8, card9);
		Assert.assertTrue(board.handleSuggestion(sol1, players.get(0)) == (null));//test no one can prove
		sol1 = new Solution(card1, card8, card9);
		Assert.assertTrue(board.handleSuggestion(sol1, players.get(0)) == (null));//test that only accuser can disprove
		
		Assert.assertTrue(board.handleSuggestion(sol1, players.get(1)).equals(card1));//test that human player can disprove
		sol1 = new Solution(card7, card2, card9);
		Assert.assertTrue(board.handleSuggestion(sol1, players.get(3)).equals(card2));//test that 1 computer can disprove
		
		sol1 = new Solution(card7, card5, card6);
		Assert.assertTrue(board.handleSuggestion(sol1, players.get(3)).equals(card5));//test that 2 computers can disprove returns first computers card
	}
}
