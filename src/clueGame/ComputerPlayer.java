package clueGame;
import java.util.ArrayList;

public class ComputerPlayer extends Player {

	private static int numPlayers;
	private ArrayList<Card> compCards;
	
	public ComputerPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		compCards = new ArrayList<Card>();
		numPlayers++;
	}
	
	@Override
	public void updateHand(Card card) {
		compCards.add(card);
	}
	
	public static int getNumPlayers() {
		return numPlayers;
	}
	
	public ArrayList<Card> getCompCards() {
		return compCards;
	}
	public BoardCell selectTarget() {
		return null;
	}
	
}
