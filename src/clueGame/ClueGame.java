package clueGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.junit.jupiter.api.BeforeAll;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ClueGame extends JFrame {

	private static Board board;
	
	private DrawPanel drawPanel;
	private GameControlPanel controlPanel;
	private ClueCardsPanel cardsPanel;
	private int dx, dy;
	public static final int BOX_WIDTH = 30;
	public static final int BOX_HEIGHT = 30;
	public static final int BOX_MARGIN = 30;
	
	public ClueGame() {
		board = Board.getInstance();
		//board.initialize();
		
		setSize(920, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawPanel = new DrawPanel();
		// paintComponent will automatically be called 1 time
		add(drawPanel, BorderLayout.CENTER);
		 // Create and add the GameControlPanel to the bottom.
        controlPanel = new GameControlPanel();
        add(controlPanel, BorderLayout.SOUTH);
        
        // Create and add the ClueCardsPanel to the right.
        cardsPanel = new ClueCardsPanel();
        add(cardsPanel, BorderLayout.EAST);
	}
	
	//getters for frame components
	public GameControlPanel getControlPanel() {
		return controlPanel;
	}
	
	public ClueCardsPanel getCardsPanel() {
		return cardsPanel;
	}
	
	public DrawPanel getPanel() {
		return drawPanel;
	}
	
	//Clicking on game board
	//Also ensure the edge case of no possible moves is handled gracefully
	public void ClickerHelper() {
		
		//board clicked on. Ensure that all possible locations are highlighted on the board
		
		//if it is human player turn:
			//if clicked on target:
				//move player
			//else:
				//error message, end
		//else:
			//end
		
		//if in room:
			//handle suggestion
		//else:
			//end
		
		//update result, end
		
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
	
	public class DrawPanel extends JPanel {
		private int x, y;
		private static DrawPanel panel;
		
		public DrawPanel() {
			addMouseListener(new MouseAdapter() {
	            @Override public void mouseReleased(MouseEvent e) {
	                handleClick(e);
	            }
	        });
		}
		
		private void handleClick(MouseEvent e) {
		    int col = e.getX() / BOX_WIDTH;
		    int row = e.getY() / BOX_HEIGHT;

		    // guard against clicks outside the grid
		    if (row < 0 || row >= board.getNumRows()) return;
		    if (col < 0 || col >= board.getNumColumns()) return;

		    BoardCell clicked = board.getCell(row, col);
		    processClick(clicked);
		}
		
		private void processClick(BoardCell cell) {
		    //Player current = board.getPlayers().get(board.getCurrentPlayer());

		    Player current = GameControlPanel.nextPlayer;
		    

		    // 2. allowed moves are the cyan targets you flagged earlier
		    if (!cell.getHighlighted()) {
		        JOptionPane.showMessageDialog(this,
		                "You must click one of the highlighted squares.",
		                "Illegal Move", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    // 3. perform the move
		    BoardCell old = board.getCell(current.getRow(), current.getCol());
		    old.setOccupied(false);

		    current.setPos(cell.getRow(), cell.getCol());
		    cell.setOccupied(true);

		    board.clearHighlights();          // turn off cyan
		    board.setIsFinished(true);        // mark turn done
		    repaint();                        // one repaint after state changes
		    
		    GameControlPanel control = Board.getFrame().getControlPanel();
		    SwingUtilities.invokeLater(() -> control.button2.setEnabled(true));
		    
		    //handle suggestion if in room
		    
		}

		
		// Do this second
		public void translate(int dx, int dy) {
			x += dx;
			y += dy;
			
			// Must include this to see changes
			repaint();
		}
		
		public void highlightTargets(Set<BoardCell> targets) {
	        board = Board.getInstance();
			board.clearHighlights();             // reset every cell’s flag
			repaint();
	        for (BoardCell c : targets) c.setHighlighted(true);
	        repaint();                           // ask Swing to redraw the panel
	    }
		
		public void paintTargets(Graphics g) {
			super.paintComponent(g);
			Board board = Board.getInstance();
			
			for (BoardCell cell : board.getTargets()) {
				cell.drawTarget(g, cell.getRow(), cell.getCol(), BOX_WIDTH, BOX_HEIGHT);
			}
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
						ArrayList<Player> players = board.getPlayers();
						for (Player player : players) {
							if (player.getRow() == row && player.getCol() == col) {
								player.draw(g, row, col, BOX_WIDTH, BOX_HEIGHT);
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