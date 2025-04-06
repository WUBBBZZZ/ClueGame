package clueGame;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {

	private static int numPlayers;
	private ArrayList<Card> compCards;
	private ArrayList<Card> compCardsRand;
	private ArrayList<Card> seenPlayers;
	private ArrayList<Card> seenWeap;
	private ArrayList<Card> seenRooms;
	private Set<Card>unseenWeap;
	private Set<Card>unseenRoom;
	private Set<Card>unseenPlayers;
	public ComputerPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		compCards = new ArrayList<Card>();
		compCardsRand = new ArrayList<Card>();
		seenPlayers = new ArrayList<Card>();
		seenWeap = new ArrayList<Card>();
		seenRooms = new ArrayList<Card>();
		unseenWeap = new HashSet<Card>();
		unseenRoom = new HashSet<Card>();
		unseenPlayers = new HashSet<Card>();
		numPlayers++;
	}
	
	@Override
	public void updateHand(Card card) {
		compCards.add(card);
	}
	
	public static int getNumPlayers() {
		return numPlayers;
	}
	public static void getNumPlayers(int x) {
		numPlayers = x;
	}
	
@Override
public Card disproveSuggestion(Solution solution) {
		
		Random rand = new Random();
		
		int n = 0; 
		
		for (Card c : compCards) {
			if (solution.getPersonSol().getCardName().equals(c.getCardName())) {
				n++;
				compCardsRand.add(c);
			} else if (solution.getRoomSol().getCardName().equals(c.getCardName())) {
				n++;
				compCardsRand.add(c);
			} else if (solution.getWeaponSol().getCardName().equals(c.getCardName())) {
				n++;
				compCardsRand.add(c);
			}
		}
		
		if (n == 0) {
			return null;
		} else {
			int randomNumber1 = rand.nextInt(n);
			return compCardsRand.get(randomNumber1);
		}
	}
	
	public ArrayList<Card> getCompCards() {
		return compCards;
	}
	public BoardCell selectTarget() {
		return null;
	}
	public void updateSeen(Card card) {
		if (card.getCardType() == CardType.ROOM) {
			seenRooms.add(card);
		}
		if (card.getCardType() == CardType.WEAPON) {
			seenWeap.add(card);
		}
		if (card.getCardType() == CardType.SUSPECT) {
			seenPlayers.add(card);
		}
	}

	public void setUnseenWeapons(Set<Card> unseenWeapons) {
		this.unseenWeap = unseenWeapons;
	}

	public void setUnseenPersons(Set<Card> unseenPersons) {
		this.unseenPlayers = unseenPersons;
	}

	public Solution createSuggestion() {
	    // Get current room from player's location
	    Board board = Board.getInstance();
	    BoardCell currentCell = board.getCell(this.row, this.col);
	    String currentRoomName = board.getRoom(currentCell).getName();
	    Card roomCard = new Card(currentRoomName, CardType.ROOM);

	    // Select a weapon randomly from unseen weapons
	    List<Card> weaponOptions = new ArrayList<Card>();
	    for (Card weapon : unseenWeap) {
	        if (!seenCards.contains(weapon)) {
	            weaponOptions.add(weapon);
	        }
	    }
	    Card weaponCard = weaponOptions.get(new Random().nextInt(weaponOptions.size()));

	    // Select a person randomly from unseen persons
	    List<Card> personOptions = new ArrayList<Card>();
	    for (Card person : unseenPlayers) {
	        if (!seenCards.contains(person)) {
	            personOptions.add(person);
	        }
	    }
	    Card personCard = personOptions.get(new Random().nextInt(personOptions.size()));

	    // Create and return the suggestion
	    return new Solution(personCard, roomCard, weaponCard);
	}

	public BoardCell selectTarget(Set<BoardCell> targets) {
		// TODO Auto-generated method stub
		return null;
	}





}
