package clueGame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public abstract class Player {

	private String name;
	private String color;
	private int row, col;
	private Set<Card> seenCards = new HashSet<Card>;
	
	public Player(String name, String color, int row, int col) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.col = col;
	}
	
	public abstract void updateHand(Card card);
	
}
