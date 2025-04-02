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
import clueGame.Solution;

public class PlayerTests {
	
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
	
	//Game plan: Board class reads in cluesetup for players and weapons. These go into 
	//Sets, from which they're used as string arguments for object instantiation via the
	//Player class. After instantiation, we also use the card class to instantiate weapons,
	//rooms, and players as cards. Then we work the deal() function to shuffle these cards
	//among all player. Players are either initialized as computer or human.
	
	// Test to see if people are loaded in properly
	// REMOVE THIS COMMENT FOR FINAL SUBMISSION: rework the file reading to Board class.
	@Test
	public void testLoadPeople() throws BadConfigFormatException {
		ArrayList<String> testPersons = new ArrayList<String>(List.of("Genji", "Sombra"));
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
	//Test to see if human and computer child objects can be instantiated. Instantiate 5 computer objects and 1 person object
	//Test to see if deck of cards instantiated works
	//Test card dealing functionality. See if players have a roughly equal number of cards, and no cards remain after being dealt. 
	//Test to see if there any any leftover cards after being dealt. Should be 0.
	

}
