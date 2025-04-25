package clueGame;
import java.awt.Color;
import java.awt.Graphics;
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
	public static final int CELL_SIZE = 30;
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
	
	public void reset() {
	    // clear card tracking
	    seenCards.clear();
	    unseenCards.clear();
	}
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
	
	public String getColor() {
		return color;
	}
	public int getRow() {
		return this.row;
	}
	public int getCol() {
		return this.col;
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
	
	public void move(BoardCell target) {
		this.row = target.getRow();
		this.col = target.getCol();
	}
	    
	public void draw(Graphics g, int row, int col, int width, int height) {
		int x = col * CELL_SIZE;
	    int y = row * CELL_SIZE;
	        
	    g.setColor(convertToColor());
	    g.fillOval(x, y, width, height);
	    g.setColor(Color.BLACK);
	    g.drawOval(x, y, width, height);
	    }
	    
	    // Helper method to convert string color to a Color object.
	    private Color convertToColor() {
	        switch (color.toLowerCase()) {
	            case "blue": return Color.BLUE;
	            case "purple": return Color.MAGENTA;
	            case "gray":   return Color.GRAY;
	            case "red":    return Color.RED;
	            case "white": return Color.WHITE;
	            case "black":  return Color.DARK_GRAY;
	            case "green":  return Color.GREEN;
	            default:       return Color.BLACK; // fallback
	        }
	    }
}
