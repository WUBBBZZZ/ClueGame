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
		humanCards.add(card);
	}
	
	public static int getNumPlayers() {
		return numPlayers;
	}
	
	@Override
	public Card disproveSuggestion(Solution solution) {
		
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
	
}
