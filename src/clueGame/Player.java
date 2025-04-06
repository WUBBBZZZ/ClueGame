package clueGame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public abstract class Player {

	protected String name;
	protected String color;
	protected int row, col;
	protected ArrayList<Card> seenCards;
	protected ArrayList<Card> unseenCards;
	
	public Player(String name, String color, int row, int col) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.col = col;
		seenCards = new ArrayList<Card>();
		unseenCards = new ArrayList<Card>();
	}
	
	public abstract void updateHand(Card card);
	public abstract ArrayList<Card> getHand();
	
	public abstract void reset();
	public void addUnseen(Card card) {
		unseenCards.add(card);
	}
	public void addSeen(Card card) {
		seenCards.add(card);
		unseenCards.remove(card);
	}
	@Override
	public String toString() {
		return name +  ", " + color;
	}
	public String getName() {
		return name;
	}
	
	public abstract Card disproveSuggestion(Solution solution);

	public ArrayList<Card> getUnseen() {
		return unseenCards;
	}
	public ArrayList<Card> getSeen() {
		return seenCards;
	}
	public void setPos(int r, int c) {
		row = r;
		col = c;
	}
	//public abstract Solution createSuggestion();

	public abstract Solution createSuggestion(Card room);

	public abstract ArrayList<Card> getWeap();

	public abstract ArrayList<Card> getPlayers();
	public abstract void deleteHand();
}
