package clueGame;
import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player {

	private static int numPlayers;
	private ArrayList<Card> compCards;
	private ArrayList<Card> compCardsRand;
	
	public ComputerPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		compCards = new ArrayList<Card>();
		compCardsRand = new ArrayList<Card>();
		numPlayers++;
	}
	
	@Override
	public void updateHand(Card card) {
		compCards.add(card);
	}
	
	public static int getNumPlayers() {
		return numPlayers;
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
	
}
