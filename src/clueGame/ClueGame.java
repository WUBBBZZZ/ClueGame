package clueGame;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.Timer;

import org.junit.jupiter.api.BeforeAll;

import javax.swing.JPanel;

public class ClueGame extends JFrame {

	private static Board board;
	
	private DrawPanel drawPanel;
	private int dx, dy;
	public static final int BOX_WIDTH = 30;
	public static final int BOX_HEIGHT = 30;
	public static final int BOX_MARGIN = 30;
	
	public ClueGame() {
		setSize(880, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawPanel = new DrawPanel();
		// paintComponent will automatically be called 1 time
		add(drawPanel, BorderLayout.CENTER);
		 // Create and add the GameControlPanel to the bottom.
        GameControlPanel controlPanel = new GameControlPanel();
        add(controlPanel, BorderLayout.SOUTH);
        
        // Create and add the ClueCardsPanel to the right.
        ClueCardsPanel cardsPanel = new ClueCardsPanel();
        add(cardsPanel, BorderLayout.EAST);
	}
	
	// Do this second
	public void updateDrawing(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		//drawPanel.translate(dx, dy);
		// 1000 millisecond delay
		Timer t = new Timer(2000, new TimerListener());
		t.start();
	}
	
	private class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			drawPanel.translate(dx, dy);
		}
	}
	
	public static void main(String[] args) {
	//The entire board is drawn here. 
	board = Board.getInstance();
	// set the file names to use my config files
	board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
	// Initialize will load config files 
	board.initialize();
		
	ClueGame frame = new ClueGame();
	frame.setVisible(true);
	// This will cause rectangle to display in new location
	//frame.updateDrawing(100, 100);
	
	//Each BoardCell in grid will be iterated, and functions will take its
	//information and draw a correspNnding graphic.
	
	}
	
	private class DrawPanel extends JPanel {
		private int x, y;
		private DrawPanel panel;
		
		public DrawPanel() {
			x = 10;
			y = 15;
		}
		
		// Do this second
		public void translate(int dx, int dy) {
			x += dx;
			y += dy;
			
			// Must include this to see changes
			repaint();
		}
		
		// Do this first
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			//Need to calculate cell size
			//panel.getWidth();
			//panel.getHeight();
			
			//Loops through all board cells, drawing each
			for (int row = 0; row < board.getNumRows(); row++) {
				for (int col = 0; col < board.getNumColumns(); col++) {
					board.getCell(row, col).draw(g, row, col, BOX_WIDTH, BOX_HEIGHT);
					if (board.getCell(row, col).getOccupied()) {
						//System.out.println("this plyer exists");
						ArrayList<Player> players = board.getPlayers();
						for (Player player : players) {
							if (player.getRow() == row && player.getCol() == col) {
								player.draw(g, row, col, BOX_WIDTH, BOX_HEIGHT);
								//System.out.println("this player exists");
							}
						}
					}
					repaint();
				}
			}
			
			//example
			//g.setColor(Color.BLUE);
			//g.fillRect(x,  y,  BOX_WIDTH,  BOX_HEIGHT);
			//g.setColor(Color.RED);
			//g.drawRect(x, y, BOX_WIDTH, BOX_HEIGHT);
			
			//Need to use the BoardCell class for this
			//Drawing the walkways and doors:
			
			//Drawing the Rooms and secret passages, and using door information:
			
			//Drawing the empty spaces:
			
			//Drawing the players
		}
	}
	
}
