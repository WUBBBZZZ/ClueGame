package clueGame;
import java.util.ArrayList;

public class HumanPlayer extends Player {

	private static int numPlayers;
	private ArrayList<Card> humanCards;
	
	public HumanPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		humanCards = new ArrayList<Card>();
		numPlayers++;
	}
	
	@Override
	public void updateHand(Card card) {
		humanCards.add(card);
	}
	
	public static int getNumPlayers() {
		return numPlayers;
	}
	
}
