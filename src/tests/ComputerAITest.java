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
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Room;
import clueGame.Solution;


class ComputerAITest {
	
	private static Board board;

	@BeforeAll
	public static void setUp() {
		
		Board.resetInstance();
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
		testAI.addSeen(testAI.getUnseen().get(3));
		testAI.setPos(2, 1);
		Card room = board.getRoomCard(2, 1);
		Solution suggestion = testAI.createSuggestion(room);
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
		
		for (int i = 0; i < testAI.getWeap().size() - 1; i++) {
			testAI.addSeen(testAI.getWeap().get(i));
		}
		for (int i = 0; i < testAI.getPlayers().size() - 1; i++) {
			testAI.addSeen(testAI.getPlayers().get(i));
		}
		suggestion = testAI.createSuggestion(room);
		//asserts that when there is 1 weapon and player left the AI chooses that player
		Assert.assertTrue(testAI.getWeap().get(0).equals(suggestion.getWeaponSol()));
		Assert.assertTrue(testAI.getPlayers().get(0).equals(suggestion.getPersonSol()));
	}

	@Test
	public void testSelectTarget() {
		Player testAI = board.getTestPlayer();
		testAI.setPos(4, 4);
		BoardCell testRoom = ((ComputerPlayer) testAI).findTarget(3);
		Room comp = board.getRoom(testRoom);
		Room comp1 = board.getRoom('F');
		Room comp2 = board.getRoom('P');
		Assert.assertTrue((comp.equals(comp1) || comp.equals(comp2)));
		
		//removes all rooms but one to test that AI will pick unseen room when given options between rooms and walkways
		//when only one room is unseen that it goes to that room
		((ComputerPlayer) testAI).remRoom();
		testRoom = ((ComputerPlayer) testAI).findTarget(1);
		comp = board.getRoom(testRoom);
		comp1 = board.getRoom('F');
		Assert.assertTrue(comp.equals(comp1));
		
		
		//removes last room from unseen rooms list
		//then runs findtarget and confirms that out of 500 at least 100 times a room or walkway is chosen
		((ComputerPlayer) testAI).remRoomF();
		int roomcount = 0;
		int walkwaycount = 0;
		for (int i = 0; i < 500; i++) {
			testRoom = ((ComputerPlayer) testAI).findTarget(1);
			if (testRoom.isRoom()) {
				roomcount++;
			} else {
				walkwaycount++;
			}
		}
		Assert.assertTrue(roomcount > 100);
		Assert.assertTrue(walkwaycount > 100);
		
		//tests that when there are no rooms it randomly picks walkways making sure that it picks different walkways
		//randomly
		testAI.setPos(4, 5);
		BoardCell prevCell = board.getCell(0, 0);
		int count = 0;
		for (int i = 0; i < 100; i++) {
			testRoom = ((ComputerPlayer) testAI).findTarget(1);
			if (prevCell != testRoom) {
				count++;
			}
			prevCell = testRoom;
		}
		Assert.assertTrue(count > 1);
	}
}
