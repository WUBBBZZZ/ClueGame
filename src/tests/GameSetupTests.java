package tests;

/*
 * This program tests a number of functionalities regarding players, the  
 * solution, and cards.
 */

import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

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

public class GameSetupTests {
	
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		System.out.println("GameSetupTests test");
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		//System.out.println(board.getPeople());
		// Initialize will load config files 
		board.initialize();
		//System.out.println(board.getPeople());
	}
	//Game plan: Board class reads in cluesetup for players and weapons. These go into 
	//Sets, from which they're used as string arguments for object instantiation via the
	//Player class. After instantiation, we also use the card class to instantiate weapons,
	//rooms, and players as cards. Then we work the deal() function to shuffle these cards
	//among all player. Players are either initialized as computer or human.
	
	// Test to see if people are loaded in properly
	@Test
	public void testLoadPeople() throws BadConfigFormatException {
		
		ArrayList<String> testPersons = new ArrayList<String>(List.of("Genji", "Sombra"));
		//System.out.println(board.getPeople());
		Assert.assertTrue(board.getPeople().get(1).equals(testPersons.get(1)));
		Assert.assertTrue(board.getPeople().size() == 6);
		
	}
	
	//Test to see if weapons are loaded in properly.
	@Test
	public void testLoadWeapons() throws BadConfigFormatException {
		ArrayList<String> testWeapons = new ArrayList<String>(List.of("SawCleaver", "NanoBlade"));
		Assert.assertTrue(board.getWeapons().get(1).equals(testWeapons.get(1)));
		Assert.assertTrue(board.getWeapons().size() == 6);
		
	}
	
	//Test to see if instantiated cards are initialized properly
	@Test
	public void testCards() throws BadConfigFormatException {
		Card testCard = new Card("Food Hall", CardType.ROOM);
		Assert.assertTrue(board.getCards().get(0).equals(testCard));
		Assert.assertTrue(board.getCards().size() == 21);
	}
	
	//Test proper initialization of 5 computer and 1 human object
	@Test
	public void testPlayers() throws BadConfigFormatException {
		System.out.println(ComputerPlayer.getNumPlayers());
		Assert.assertTrue(ComputerPlayer.getNumPlayers() == 5);
		Assert.assertTrue(HumanPlayer.getNumPlayers() == 1);
	}
	
	//Test to see if Solution has 3 cards of types room, person and weapon
	@Test
	public void testSolution() throws BadConfigFormatException {
		Card card1 = new Card("name1", CardType.ROOM);
		Card card2 = new Card("name2", CardType.SUSPECT);
		Card card3 = new Card("name3", CardType.WEAPON);
		Solution testSolution = new Solution(card1, card2, card3);
		Assert.assertTrue(testSolution.getRoomSol().getCardType().equals(CardType.ROOM));
		Assert.assertTrue(testSolution.getPersonSol().getCardType().equals(CardType.SUSPECT));
		Assert.assertTrue(testSolution.getWeaponSol().getCardType().equals(CardType.WEAPON));
	}
	
	//Test to see if players have a roughly equal number of cards, and no cards remain after being dealt. 
	@Test
	public void testCardLeftovers() throws BadConfigFormatException {
		Assert.assertTrue(board.getCardsCopy().size() == 0);
		
	}

}
