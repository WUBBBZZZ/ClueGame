package clueGame;
import java.util.ArrayList;
import java.util.Random;

public class HumanPlayer extends Player {

	private static int numPlayers;
	private ArrayList<Card> humanCards;
	private ArrayList<Card> humanCardsRand;
	
	public HumanPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		humanCards = new ArrayList<Card>();
		humanCardsRand = new ArrayList<Card>();
		numPlayers++;
	}
	@Override
	public void updateHand(Card card) {
		this.addSeen(card);
		humanCards.add(card);
	}
	@Override
	public ArrayList<Card> getHand(){
		return humanCards;
	}
	public static int getNumPlayers() {
		return numPlayers;
	}
	public static void setNumPlayers(int x) {
		numPlayers = x;
	}
	
	@Override
	public Card disproveSuggestion(Solution solution) {
		humanCardsRand.clear();
		Random rand = new Random();
		
		int n = 0; 
		for (Card c : humanCards) {
			if (solution.getPersonSol().getCardName().equals(c.getCardName())) {
				n++;
				humanCardsRand.add(c);
			} else if (solution.getRoomSol().getCardName().equals(c.getCardName())) {
				n++;
				humanCardsRand.add(c);
			} else if (solution.getWeaponSol().getCardName().equals(c.getCardName())) {
				n++;
				humanCardsRand.add(c);
			}
		}
		
		if (n == 0) {
			return null;
		} else {
			int randomNumber1 = rand.nextInt(n);
			return humanCardsRand.get(randomNumber1);
		}
	}
	@Override
	public Solution createSuggestion(Card room) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void reset() {
		numPlayers = 0;
	}
	@Override
	public ArrayList<Card> getWeap() {
		ArrayList<Card> temp = new ArrayList<>();
		for (Card card : seenCards) {
			if (card.getCardType() == CardType.WEAPON) {
				temp.add(card);
			} else {
				continue;
			}
		}
		return temp;
	}
	@Override
	public ArrayList<Card> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void deleteHand() {
		humanCards.clear();
		humanCardsRand.clear();
	}
	
}
