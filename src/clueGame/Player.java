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
	protected Set<Card> seenCards = new HashSet<Card>();
	
	public Player(String name, String color, int row, int col) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.col = col;
	}
	
	public abstract void updateHand(Card card);
	@Override
	public String toString() {
		return name +  ", " + color;
	}
	
	public abstract Card disproveSuggestion(Solution solution);
}
