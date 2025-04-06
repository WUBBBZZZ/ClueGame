package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Solution;

class ComputerAITest {
	
	private static Board board;
	private ComputerPlayer tester = new ComputerPlayer("Genji", "blue", 2, 1);
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
	public void testCreateSuggestion() {

	    // Set player to a known room location
	    //board.getCell(board.getRow(1), tester.getColumn()).setRoom(true);
	    //board.getCell(player.getRow(), player.getColumn()).setRoomName("Kitchen");

	    // Set cards that have already been seen
	    tester.updateSeen(new Card("Sombra", CardType.SUSPECT));
	    tester.updateSeen(new Card("Revolver", CardType.WEAPON));

	    // Weapons and persons in deck
	    Set<Card> unseenWeapons = new HashSet<Card>(Arrays.asList(
	        new Card("Knife", CardType.WEAPON),
	        new Card("Candlestick", CardType.WEAPON)
	    ));

	    Set<Card> unseenPersons = new HashSet<Card>(Arrays.asList(
	        new Card("Colonel Mustard", CardType.SUSPECT)
	    ));

	    // Inject deck state into player (if needed)
	    tester.setUnseenWeapons(unseenWeapons);
	    tester.setUnseenPersons(unseenPersons);

	    Solution suggestion = tester.createSuggestion();

	    assertEquals("Kitchen", suggestion.getRoomSol().getCardName());
	    assertEquals("Colonel Mustard", suggestion.getPersonSol().getCardName());
	    assertTrue(unseenWeapons.contains(suggestion.getWeaponSol()));
	}

	@Test
	public void testSelectTarget() {


	    // Set up targets
	    Set<BoardCell> targets = new HashSet<>();
	    BoardCell roomSeen = new BoardCell(5, 5);
	    roomSeen.setRoom(true);
	    roomSeen.setRoomName("Lounge");

	    BoardCell roomUnseen = new BoardCell(2, 2);
	    roomUnseen.setRoom(true);
	    roomUnseen.setRoomName("Conservatory");

	    BoardCell walkway = new BoardCell(3, 3);

	    targets.add(roomSeen);
	    targets.add(roomUnseen);
	    targets.add(walkway);

	    // Mark the lounge as seen
	    tester.updateSeen(new Card("Lounge", CardType.ROOM));

	    // Test logic - unseen room should always be chosen if available
	    BoardCell selected = tester.selectTarget(targets);
	    assertEquals(roomUnseen, selected);

	    // Now mark Conservatory seen, test random selection among targets
	    tester.updateSeen(new Card("Conservatory", CardType.ROOM));

	    // Since both rooms are now seen, selection should be random
	    int roomSeenCount = 0;
	    int walkwayCount = 0;

	    for (int i = 0; i < 100; i++) {
	        selected = tester.selectTarget(targets);
	        if (selected.equals(roomSeen)) roomSeenCount++;
	        if (selected.equals(walkway)) walkwayCount++;
	    }

	    assertTrue(roomSeenCount > 0);
	    assertTrue(walkwayCount > 0);
	}
}
