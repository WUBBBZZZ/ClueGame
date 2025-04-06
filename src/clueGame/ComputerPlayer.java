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

	public ComputerPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		compCards = new ArrayList<Card>();
		compCardsRand = new ArrayList<Card>();
		unseenWeap  = new ArrayList<Card>();
		unseenPlayers  = new ArrayList<Card>();
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
		if (unseenPlayers.size() != 0 || unseenWeap.size() != 0) {
			unseenPlayers.clear();
			unseenWeap.clear();
		}
		for (Card card : this.unseenCards) {
			if (card.getCardType() == CardType.ROOM) {
				continue;
			}else if (card.getCardType() == CardType.SUSPECT) {
				unseenPlayers.add(card);
			}else if (card.getCardType() == CardType.WEAPON) {
				unseenWeap.add(card);
			}
		}
		int x = Math.abs(rand.nextInt() % unseenPlayers.size());
		int y = Math.abs(rand.nextInt() % unseenWeap.size());
		return new Solution(roomProp, unseenPlayers.get(x), unseenWeap.get(y));
	}

	@Override
	public void reset() {
		numPlayers = 0;
	}
	@Override
	public ArrayList<Card> getWeap(){
		return this.unseenWeap;
	}
	@Override
	public ArrayList<Card> getPlayers(){
		return this.unseenPlayers;
	}

}
