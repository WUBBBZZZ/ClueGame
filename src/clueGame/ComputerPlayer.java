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
	private ArrayList<Card> unseenWeap;
	private ArrayList<Card> unseenPlayers;
	private ArrayList<Room> unseenRooms;

	public ComputerPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		compCards = new ArrayList<Card>();
		compCardsRand = new ArrayList<Card>();
		unseenWeap  = new ArrayList<Card>();
		unseenPlayers  = new ArrayList<Card>();
		unseenRooms = new ArrayList<Room>();
		numPlayers++;
	}
	
	@Override
	public void updateHand(Card card) {
		compCards.add(card);
		this.addSeen(card);
	}
	@Override
	public ArrayList<Card> getHand(){
		return compCards;
	}
	public static int getNumPlayers() {
		return numPlayers;
	}
	public static void getNumPlayers(int x) {
		numPlayers = x;
	}
	
@Override
public Card disproveSuggestion(Solution solution) {
		compCardsRand.clear();
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
	@Override
	public Solution createSuggestion(Card roomProp) {
	    Random rand = new Random();
	    // unseenPlayers and unseenWeap are already up-to-date
	    int x = rand.nextInt(unseenPlayers.size());
	    int y = rand.nextInt(unseenWeap.size());
	    return new Solution(roomProp, unseenPlayers.get(x), unseenWeap.get(y));
	}
	public void initUnseen(Room room) {
		unseenRooms.add(room);
	}

	@Override
	public void reset() {
	    super.reset(); // clears seen/unseen lists
	    // clear computer-specific card lists
	    compCards.clear();
	    compCardsRand.clear();
	    unseenPlayers.clear();
	    unseenWeap.clear();
	    unseenRooms.clear();
	    numPlayers = 0; // reset global count if desired
	}
	@Override
	public ArrayList<Card> getWeap(){
		return this.unseenWeap;
	}
	@Override
	public ArrayList<Card> getPlayers(){
		return this.unseenPlayers;
	}
	@Override
	public void deleteHand() {
		compCards.clear();
		compCardsRand.clear();
	}
	public BoardCell findTarget(int roll) {
		Random rand = new Random();
		Board board = Board.getInstance();
		board.calcTargets(board.getCell(this.row, this.col), roll);
		Set<BoardCell> targets = board.getTargets();
		if (unseenRooms.isEmpty()) {
			BoardCell checker = (BoardCell) targets.toArray()[(Math.abs(rand.nextInt() % (targets.size())))];
			if (checker.isDoorway()) {
				Room temp = board.doorToRoom(checker);
				return temp.getCenterCell();
			} else {
				return checker;
			}
		}
		ArrayList<BoardCell> unseen = new ArrayList<BoardCell>();
		ArrayList<BoardCell> seen = new ArrayList<BoardCell>();
		int roomCount = 0;
		for (BoardCell target : targets) {
			if (target.isDoorway()) {
				Room temp = board.doorToRoom(target);
				roomCount++;
				if(unseenRooms.contains(temp)) {
					unseen.add(temp.getCenterCell());
				} else {
					seen.add(temp.getCenterCell());
				}
			}
		}
		
		if (unseen.size() > 0) {
			return unseen.get(Math.abs(rand.nextInt() % (unseen.size())));
		} else  if (roomCount > 0){
			return seen.get(Math.abs(rand.nextInt() % (seen.size())));
		} else {
			ArrayList<BoardCell> tempCont = new ArrayList<>(targets);
			return tempCont.get(Math.abs(rand.nextInt() % (tempCont.size())));
		}
	}
	public ArrayList<Room> getRooms(){
		return unseenRooms;
	}
	public void remRoom() {
		for (int i = 8; i > 0; i--) {
			unseenRooms.remove(i);
		}
		//System.out.println(unseenRooms);
	} 
	public void remRoomF() {
		unseenRooms.clear();
	}
}
