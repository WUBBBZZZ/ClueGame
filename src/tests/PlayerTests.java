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
	
	@BeforeEach
	public void setUp() {
	    Board.resetInstance();
	}
	
	// Test to see if people are loaded in properly
	@Test
	public void testLoadPeople() {
		ArrayList<String> testPersons = new ArrayList<String>(List.of("Genji", "Sombra", "Reinhardt", "Doomfist", "Mercy", "Moira"));
		Player.loadPeople();
		Assert.assertTrue(Player.getPeople().equals(testPersons));
		
	}

}
