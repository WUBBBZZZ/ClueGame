package clueGame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameControlPanel extends JPanel {

	private String playerName = "";
	private String playerColor = "";
	private int roll;
	private String guess = "";
	private String guessResult = "";
	private boolean suggestionMade = false;
	private JTextField playerNameField;
	private JTextField rollField;
	private JTextField guessField;
	private JTextField guessResultField;
	private static JTextField roomField;
	public Card newCard;
	
	private JButton button1;
	public JButton button2;
	private boolean end;
	
	private boolean next;
	private Board board;
	public static Player nextPlayer;
	
	public GameControlPanel() {
		
		//Outer JPanel (2x0) - GameControlPanel
		//setLayout(new GridLayout(2, 1));
		
		next = false;
		
		setLayout(new BorderLayout());
		JPanel outerPanel = new JPanel(new GridLayout(2, 1));
	    outerPanel.setBorder(BorderFactory.createTitledBorder("Game Control Panel"));
	   
			//Upper JPanel (1x4)			
			JPanel upperLongPanel = new JPanel(new GridLayout(1, 4));
			
				//JPanel 1 (includes JLabel and JTextField, 2x0)
				JPanel upperPanel1 = new JPanel(new GridLayout(2, 1));
					//"Whose Turn?"
					JLabel labelUpper1 = new JLabel("Whose Turn?");
					//Ex. "Colonel Mustard", and the box should be yellow
					playerNameField = new JTextField(playerName, 20);
					
					//Sets color
						playerNameField.setBackground(Color.WHITE);
						playerNameField.setOpaque(true);
					
				
				//JPanel 2 (includes JLabel and JTextfield, 0x2)
				JPanel upperPanel2 = new JPanel(new GridLayout(1, 2));
					//"Roll:"
					JLabel labelUpper2 = new JLabel("	Roll:");
					//Ex. 5
					rollField = new JTextField("0 ", 5);
		
				//JButton, "Make Accusation"
				button1 = new JButton("Make Accusation");
				button1.setBackground(Color.CYAN);
				button1.setOpaque(true);
				button1.setBorderPainted(true);
				button1.addActionListener(e -> {
					Player current = Board.getInstance().getPlayers().get(
						    Board.getInstance().getCurrentPlayer()
						);
						//if (!(current instanceof HumanPlayer)) {
						    // should never happen if setTurn is correct, but just in case:
						    //return;
						//}
				    //grab the board & the human
				    Board board = Board.getInstance();
				    Player human = board.getPlayers().get(board.getCurrentPlayer());
				    if (suggestionMade) {
				        JOptionPane.showMessageDialog(
				            this,
				            "You’ve already made a suggestion this turn!",
				            "Suggestion used",
				            JOptionPane.INFORMATION_MESSAGE
				        );
				        return;
				    }
				    /*
				    BoardCell loc = board.getCell(human.getRow(), human.getCol());
				    if (!loc.isRoom()) {
				        JOptionPane.showMessageDialog(
				            this,
				            "You must be in a room to make a suggestion!",
				            "Not in a room",
				            JOptionPane.WARNING_MESSAGE
				        );
				        return;
				    }
				     */
				    //collect the two lists for the combo-boxes
				    java.util.List<String> people  = board.getNames();
				    java.util.List<String> weapons = board.getWeaponNames();
				    java.util.List<String> rooms = board.getRoomList();

				    // show the modal dialog
				    AccusationDialog dlg = new AccusationDialog(
				        (Frame) SwingUtilities.getWindowAncestor(this),
				        people,
				        weapons,
				        rooms
				    );
				    dlg.setVisible(true);

				    // (e) pull back what they chose
				    if (dlg.getSolution() != null) {
				        setGuess(dlg.getSolution()[0] + " " + dlg.getSolution()[1] + " " + dlg.getSolution()[2]);

				        // 1) grab your Board singleton
				        board = Board.getInstance();

				        // 2) look up each Card by name
				        //Card personCard = board.getCard(s.getPerson());
				        //Card weaponCard = board.getCard(s.getWeapon());
				        //Card roomCard   = board.getCard( s.getRoom());
				        //Solution sol = board.getSolution();
				        end = board.checkAccusation(dlg.getSolution()[0], dlg.getSolution()[1], dlg.getSolution()[2], board.getSolution());
				        suggestionMade = true;
				        button1.setEnabled(false);
				        
				        //finish game
				        if (end == true) {
				        	JOptionPane.showMessageDialog(
									null,
									"Congratulations! You won!",
									"You Won!",
									JOptionPane.INFORMATION_MESSAGE
									);
				        } else {
				        	JOptionPane.showMessageDialog(
									null,
									"YOU DIED\n Answer was " + board.getSolution().getPersonSol().getCardName() + " " + board.getSolution().getWeaponSol().getCardName() + " " + board.getSolution().getRoomSol().getCardName() + " ",
									"Git Gud",
									JOptionPane.INFORMATION_MESSAGE
									);
				        }
				        
				        
						new javax.swing.Timer(1000, t -> System.exit(0)).start();
						return;
						
				        
				    }

				    // (f) re-enable NEXT
				    button2.setEnabled(true);
				});

				//JButton, "NEXT"
				button2 = new JButton("NEXT");
				button2.setBackground(Color.GRAY);
				button2.setOpaque(true);
				button2.setBorderPainted(true);
				button2.addActionListener(e -> {
					ButtonListenerHelper();
			    });
		
			//Lower JPanel (0x2)
			JPanel lowerLongPanel = new JPanel(new GridLayout(1, 2));
		
				//JPanel (1x0) with Border. Titled "Guess". Includes JTextField - "I have no guess"
				JPanel lowerPanel1 = new JPanel(new GridLayout(1, 1));
				lowerPanel1.setLayout(new BorderLayout());
				lowerPanel1.setBorder(BorderFactory.createTitledBorder("Guess"));
				guessField = new JTextField(guess, 20);
			
				//JPanel (1x0) with Border. Titles "Guess Result". Includes JTextField - "So you have nothing?"
				JPanel lowerPanel2 = new JPanel(new GridLayout(1, 1));
				lowerPanel2.setLayout(new BorderLayout());
				lowerPanel2.setBorder(BorderFactory.createTitledBorder("Guess Result"));
				guessResultField = new JTextField(guessResult,20);
				
		//Put it all together
		lowerPanel1.add(guessField, BorderLayout.CENTER);
		lowerPanel2.add(guessResultField, BorderLayout.CENTER);
		
		lowerLongPanel.add(lowerPanel1, BorderLayout.CENTER);
		lowerLongPanel.add(lowerPanel2, BorderLayout.CENTER);
		
		upperPanel1.add(labelUpper1);
		upperPanel1.add(playerNameField);
		upperPanel2.add(labelUpper2);
		upperPanel2.add(rollField);
		
		upperLongPanel.add(upperPanel1);
		upperLongPanel.add(upperPanel2);
		upperLongPanel.add(button1);
		upperLongPanel.add(button2);
		
		outerPanel.add(upperLongPanel, BorderLayout.NORTH);
		outerPanel.add(lowerLongPanel, BorderLayout.SOUTH);
		
		this.add(outerPanel, BorderLayout.CENTER);
	}
	
	public boolean buttonContinue() {
		return next;
	}
	
	//helper for dice
		public int diceRoll() {
			Random rand = new Random();
			int n = rand.nextInt(1,7);
			return n;
		}
		
		public static JTextField getRoomField() {
	    	return roomField;
	    }
	
	//really this is where the game is run
	public void ButtonListenerHelper() {
		
		SwingUtilities.invokeLater(() -> {
			
			board = Board.getInstance();
			

			//If Current human player finished: 
				//update current player
				
			//Else:
				//error message, end.
			if (Board.getIsFinished()) {	
				
				nextPlayer = board.getPlayers().get(board.getCurrentPlayer());
				int number = this.diceRoll();
				
				//updates player, rolls dice
				int nextIndex = (board.getCurrentPlayer() + 1) % board.getPlayers().size();
					
				board.setCurrentPlayer(nextIndex);
				this.setTurn(nextPlayer, number);
				
				//Calc targets and show possibilities
				BoardCell playerLocation = board.getCell(nextPlayer.getRow(), nextPlayer.getCol());
				board.calcTargets(playerLocation, number);
				if (nextPlayer.getName().equals("Genji")) {
					//highlighted squares are only visible to human player
					board.setIsFinished(false);   
					board.clearHighlights();
					for (BoardCell cell : board.getTargets()) {
						cell.setHighlighted(true);
					}
					button2.setEnabled(false); 
					Board.getFrame().repaint();
					
					

					
				} else {
					board.clearHighlights();
					
					//do accusation?
					if (nextPlayer.getSeen().size() == 15)  {
						JOptionPane.showMessageDialog(
								null,
								nextPlayer.getName() + " Knows the solution: "
										+ board.getSolution().toString(),
										"Game Over",
								
								JOptionPane.INFORMATION_MESSAGE
								);
						new javax.swing.Timer(1000, t -> System.exit(0)).start();
					}
					
					//move
					
					
					BoardCell oldCell = board.getCell(nextPlayer.getRow(), nextPlayer.getCol());
					
					int size = board.getTargets().size();
					Random rand = new Random();
					
					if (size != 0) {
						oldCell.setOccupied(false);
						int n = rand.nextInt(size);
						
						int count = 0;					
						
						for (BoardCell cell : board.getTargets()) {
							if (count == n) {
								cell.setOccupied(true);
								nextPlayer.setPos(cell.getRow(), cell.getCol());
								
							} 
							
							count++;
						}
						
						for (BoardCell cell : board.getTargets()) {
							if (cell.isDoorway()) {
								board.getCell(nextPlayer.getRow(), nextPlayer.getCol()).setOccupied(false);
								cell.setOccupied(true);
								nextPlayer.setPos(cell.getRow(), cell.getCol());
							} 
						}
						
						for (BoardCell cell : board.getTargets()) {
							if (cell.isRoom()) {														
								board.getCell(nextPlayer.getRow(), nextPlayer.getCol()).setOccupied(false);
								
								//handle room overlapping problem
								if (cell.getOccupied() && cell.getInitial() == 'P') {
									if (nextPlayer.getName().equals("Sombra")) {
										nextPlayer.setPos(2, 0);
						    		} else if (nextPlayer.getName().equals("Reinhardt")) {
						    			nextPlayer.setPos(3, 0);
						    		} else if (nextPlayer.getName().equals("Doomfist")) {
						    			nextPlayer.setPos(1, 2);
						    		} else if (nextPlayer.getName().equals("Mercy")) {
						    			nextPlayer.setPos(2, 2);
						    		} else if (nextPlayer.getName().equals("Moira")) {
						    			nextPlayer.setPos(3, 2);
						    		}
					    		} else if (cell.getOccupied() && cell.getInitial() == 'F') {
									if (nextPlayer.getName().equals("Sombra")) {
										nextPlayer.setPos(1, 9);
						    		} else if (nextPlayer.getName().equals("Reinhardt")) {
						    			nextPlayer.setPos(2, 9);
						    		} else if (nextPlayer.getName().equals("Doomfist")) {
						    			nextPlayer.setPos(3, 9);
						    		} else if (nextPlayer.getName().equals("Mercy")) {
						    			nextPlayer.setPos(3, 8);
						    		} else if (nextPlayer.getName().equals("Moira")) {
						    			nextPlayer.setPos(3, 7);
						    		}
					    		} else if (cell.getOccupied() && cell.getInitial() == 'T') {
									if (nextPlayer.getName().equals("Sombra")) {
										nextPlayer.setPos(1, 15);
						    		} else if (nextPlayer.getName().equals("Reinhardt")) {
						    			nextPlayer.setPos(2, 15);
						    		} else if (nextPlayer.getName().equals("Doomfist")) {
						    			nextPlayer.setPos(3, 14);
						    		} else if (nextPlayer.getName().equals("Mercy")) {
						    			nextPlayer.setPos(3, 13);
						    		} else if (nextPlayer.getName().equals("Moira")) {
						    			nextPlayer.setPos(2, 13);
						    		}
					    		} else if (cell.getOccupied() && cell.getInitial() == 'C') {
									if (nextPlayer.getName().equals("Sombra")) {
										nextPlayer.setPos(6, 20);
						    		} else if (nextPlayer.getName().equals("Reinhardt")) {
						    			nextPlayer.setPos(6, 19);
						    		} else if (nextPlayer.getName().equals("Doomfist")) {
						    			nextPlayer.setPos(5, 19);
						    		} else if (nextPlayer.getName().equals("Mercy")) {
						    			nextPlayer.setPos(4, 19);
						    		} else if (nextPlayer.getName().equals("Moira")) {
						    			nextPlayer.setPos(3, 20);
						    		}
					    		} else if (cell.getOccupied() && cell.getInitial() == 'O') {
									if (nextPlayer.getName().equals("Sombra")) {
										nextPlayer.setPos(11, 19);
						    		} else if (nextPlayer.getName().equals("Reinhardt")) {
						    			nextPlayer.setPos(12, 19);
						    		} else if (nextPlayer.getName().equals("Doomfist")) {
						    			nextPlayer.setPos(13, 19);
						    		} else if (nextPlayer.getName().equals("Mercy")) {
						    			nextPlayer.setPos(13, 18);
						    		} else if (nextPlayer.getName().equals("Moira")) {
						    			nextPlayer.setPos(13, 17);
						    		}
					    		} else if (cell.getOccupied() && cell.getInitial() == 'M') {
									if (nextPlayer.getName().equals("Sombra")) {
										nextPlayer.setPos(18, 19);
						    		} else if (nextPlayer.getName().equals("Reinhardt")) {
						    			nextPlayer.setPos(19, 19);
						    		} else if (nextPlayer.getName().equals("Doomfist")) {
						    			nextPlayer.setPos(20, 19);
						    		} else if (nextPlayer.getName().equals("Mercy")) {
						    			nextPlayer.setPos(20, 18);
						    		} else if (nextPlayer.getName().equals("Moira")) {
						    			nextPlayer.setPos(20, 17);
						    		}
					    		} else if (cell.getOccupied() && cell.getInitial() == 'G') {
									if (nextPlayer.getName().equals("Sombra")) {
										nextPlayer.setPos(18, 9);
						    		} else if (nextPlayer.getName().equals("Reinhardt")) {
						    			nextPlayer.setPos(19, 9);
						    		} else if (nextPlayer.getName().equals("Doomfist")) {
						    			nextPlayer.setPos(20, 9);
						    		} else if (nextPlayer.getName().equals("Mercy")) {
						    			nextPlayer.setPos(20, 8);
						    		} else if (nextPlayer.getName().equals("Moira")) {
						    			nextPlayer.setPos(20, 7);
						    		}
					    		} else if (cell.getOccupied() && cell.getInitial() == 'B') {
									if (nextPlayer.getName().equals("Sombra")) {
										nextPlayer.setPos(18, 2);
						    		} else if (nextPlayer.getName().equals("Reinhardt")) {
						    			nextPlayer.setPos(19, 2);
						    		} else if (nextPlayer.getName().equals("Doomfist")) {
						    			nextPlayer.setPos(19, 1);
						    		} else if (nextPlayer.getName().equals("Mercy")) {
						    			nextPlayer.setPos(18, 0);
						    		} else if (nextPlayer.getName().equals("Moira")) {
						    			nextPlayer.setPos(17, 0);
						    		}
					    		} else if (cell.getOccupied() && cell.getInitial() == 'A') {
									if (nextPlayer.getName().equals("Sombra")) {
										nextPlayer.setPos(10, 2);
						    		} else if (nextPlayer.getName().equals("Reinhardt")) {
						    			nextPlayer.setPos(11, 2);
						    		} else if (nextPlayer.getName().equals("Doomfist")) {
						    			nextPlayer.setPos(11, 1);
						    		} else if (nextPlayer.getName().equals("Mercy")) {
						    			nextPlayer.setPos(11, 0);
						    		} else if (nextPlayer.getName().equals("Moira")) {
						    			nextPlayer.setPos(10, 0);
						    		}
					    		} else {
					    			nextPlayer.setPos(cell.getRow(), cell.getCol());
					    		}
								cell.setOccupied(true);
								//suggestion code
								for (Card c : board.getCards()) {
									if (c.getCardName().equals(cell.getRoomName())) {
										newCard = c;
										
									}
								}
								
								Solution compSuggestion = nextPlayer.createSuggestion(newCard);
								nextPlayer.addSeen(board.handleSuggestion(compSuggestion, nextPlayer));
								   
					    		 
								   //move player to room
								   for (Player p : board.getPlayers()) {
									   if (p.getName().equals(compSuggestion.getPersonSol().getCardName())) {
										   nextPlayer = p;
									   }
								   }
								   if (cell.getInitial() == 'P') {
										if (nextPlayer.getName().equals("Sombra")) {
											nextPlayer.setPos(2, 0);
							    		} else if (nextPlayer.getName().equals("Reinhardt")) {
							    			nextPlayer.setPos(3, 0);
							    		} else if (nextPlayer.getName().equals("Doomfist")) {
							    			nextPlayer.setPos(1, 2);
							    		} else if (nextPlayer.getName().equals("Mercy")) {
							    			nextPlayer.setPos(2, 2);
							    		} else if (nextPlayer.getName().equals("Moira")) {
							    			nextPlayer.setPos(3, 2);
							    		} else if (nextPlayer.getName().equals("Genji")) {
							    			nextPlayer.setPos(1, 0);
							    		}
						    		} else if (cell.getInitial() == 'F') {
										if (nextPlayer.getName().equals("Sombra")) {
											nextPlayer.setPos(1, 9);
							    		} else if (nextPlayer.getName().equals("Reinhardt")) {
							    			nextPlayer.setPos(2, 9);
							    		} else if (nextPlayer.getName().equals("Doomfist")) {
							    			nextPlayer.setPos(3, 9);
							    		} else if (nextPlayer.getName().equals("Mercy")) {
							    			nextPlayer.setPos(3, 8);
							    		} else if (nextPlayer.getName().equals("Moira")) {
							    			nextPlayer.setPos(3, 7);
							    		} else if (nextPlayer.getName().equals("Genji")) {
							    			nextPlayer.setPos(1, 8);
							    		}
						    		} else if (cell.getInitial() == 'T') {
										if (nextPlayer.getName().equals("Sombra")) {
											nextPlayer.setPos(1, 15);
							    		} else if (nextPlayer.getName().equals("Reinhardt")) {
							    			nextPlayer.setPos(2, 15);
							    		} else if (nextPlayer.getName().equals("Doomfist")) {
							    			nextPlayer.setPos(3, 14);
							    		} else if (nextPlayer.getName().equals("Mercy")) {
							    			nextPlayer.setPos(3, 13);
							    		} else if (nextPlayer.getName().equals("Moira")) {
							    			nextPlayer.setPos(2, 13);
							    		} else if (nextPlayer.getName().equals("Genji")) {
							    			nextPlayer.setPos(1, 14);
							    		}
						    		} else if (cell.getInitial() == 'C') {
										if (nextPlayer.getName().equals("Sombra")) {
											nextPlayer.setPos(6, 20);
							    		} else if (nextPlayer.getName().equals("Reinhardt")) {
							    			nextPlayer.setPos(6, 19);
							    		} else if (nextPlayer.getName().equals("Doomfist")) {
							    			nextPlayer.setPos(5, 19);
							    		} else if (nextPlayer.getName().equals("Mercy")) {
							    			nextPlayer.setPos(4, 19);
							    		} else if (nextPlayer.getName().equals("Moira")) {
							    			nextPlayer.setPos(3, 20);
							    		} else if (nextPlayer.getName().equals("Genji")) {
							    			nextPlayer.setPos(4, 20);
							    		}
						    		} else if (cell.getInitial() == 'O') {
										if (nextPlayer.getName().equals("Sombra")) {
											nextPlayer.setPos(11, 19);
							    		} else if (nextPlayer.getName().equals("Reinhardt")) {
							    			nextPlayer.setPos(12, 19);
							    		} else if (nextPlayer.getName().equals("Doomfist")) {
							    			nextPlayer.setPos(13, 19);
							    		} else if (nextPlayer.getName().equals("Mercy")) {
							    			nextPlayer.setPos(13, 18);
							    		} else if (nextPlayer.getName().equals("Moira")) {
							    			nextPlayer.setPos(13, 17);
							    		} else if (nextPlayer.getName().equals("Genji")) {
							    			nextPlayer.setPos(11, 18);
							    		}
						    		} else if (cell.getInitial() == 'M') {
										if (nextPlayer.getName().equals("Sombra")) {
											nextPlayer.setPos(18, 19);
							    		} else if (nextPlayer.getName().equals("Reinhardt")) {
							    			nextPlayer.setPos(19, 19);
							    		} else if (nextPlayer.getName().equals("Doomfist")) {
							    			nextPlayer.setPos(20, 19);
							    		} else if (nextPlayer.getName().equals("Mercy")) {
							    			nextPlayer.setPos(20, 18);
							    		} else if (nextPlayer.getName().equals("Moira")) {
							    			nextPlayer.setPos(20, 17);
							    		} else if (nextPlayer.getName().equals("Genji")) {
							    			nextPlayer.setPos(18, 18);
							    		}
						    		} else if (cell.getInitial() == 'G') {
										if (nextPlayer.getName().equals("Sombra")) {
											nextPlayer.setPos(18, 9);
							    		} else if (nextPlayer.getName().equals("Reinhardt")) {
							    			nextPlayer.setPos(19, 9);
							    		} else if (nextPlayer.getName().equals("Doomfist")) {
							    			nextPlayer.setPos(20, 9);
							    		} else if (nextPlayer.getName().equals("Mercy")) {
							    			nextPlayer.setPos(20, 8);
							    		} else if (nextPlayer.getName().equals("Moira")) {
							    			nextPlayer.setPos(20, 7);
							    		} else if (nextPlayer.getName().equals("Genji")) {
							    			nextPlayer.setPos(18, 8);
							    		}
						    		} else if (cell.getInitial() == 'B') {
										if (nextPlayer.getName().equals("Sombra")) {
											nextPlayer.setPos(18, 2);
							    		} else if (nextPlayer.getName().equals("Reinhardt")) {
							    			nextPlayer.setPos(19, 2);
							    		} else if (nextPlayer.getName().equals("Doomfist")) {
							    			nextPlayer.setPos(19, 1);
							    		} else if (nextPlayer.getName().equals("Mercy")) {
							    			nextPlayer.setPos(18, 0);
							    		} else if (nextPlayer.getName().equals("Moira")) {
							    			nextPlayer.setPos(17, 0);
							    		} else if (nextPlayer.getName().equals("Genji")) {
							    			nextPlayer.setPos(17, 2);
							    		}
						    		} else if (cell.getInitial() == 'A') {
										if (nextPlayer.getName().equals("Sombra")) {
											nextPlayer.setPos(10, 2);
							    		} else if (nextPlayer.getName().equals("Reinhardt")) {
							    			nextPlayer.setPos(11, 2);
							    		} else if (nextPlayer.getName().equals("Doomfist")) {
							    			nextPlayer.setPos(11, 1);
							    		} else if (nextPlayer.getName().equals("Mercy")) {
							    			nextPlayer.setPos(11, 0);
							    		} else if (nextPlayer.getName().equals("Moira")) {
							    			nextPlayer.setPos(10, 0);
							    		} else if (nextPlayer.getName().equals("Genji")) {
							    			nextPlayer.setPos(9, 2);
							    		}
						    		} 
							} 
						}
						
						Board.getFrame().repaint();
					}
					
					
					
					
					
				}
				
			} else {
				JOptionPane.showMessageDialog(
				null,
				"Finish your turn!",
				"Turn not complete",
				JOptionPane.INFORMATION_MESSAGE
				);
			}
		
		});
		
		//Calc Targets
		
		//Update Game Control Panel
		
		//If new player human:
			//display targets
		//Else:
			//Do accusation? (dummy this out)
			//Do move
			//Make suggestion? (dummy this out)
			//end
		
		//Flag unfinished
		
		//End
		
	}
	
	public void enableNext(boolean ok) {
	    button2.setEnabled(ok);
	}

	
	public void setTurn(Player player, int n) {
		if (player instanceof HumanPlayer) {
			suggestionMade = false;
		    button1.setEnabled(true);
		} else {
	        suggestionMade = true;
	        button1.setEnabled(false);
		}
		playerName = player.getName();
		playerNameField.setText(playerName);
		
		playerColor = player.getColor();
		roll = n;
		playerNameField.setBackground(parseColor(playerColor));
		playerNameField.setOpaque(true);
        
        rollField.setText(String.valueOf(roll));
	}
	
	public void setGuess(String line) {
		guess = line;
		guessField.setText(guess);
	}
	
	public void setGuessResult(String line) {
		guessResult = line;
		guessResultField.setText(guessResult);
	}
	
	public static void main(String[] args) {
		
		GameControlPanel panel = new GameControlPanel();  // create the panel
		
		// test filling in the data
		
		panel.setTurn(new ComputerPlayer( "Genji", "Green", 0, 0), 5);
		panel.setGuess( "I have no guess!");
		panel.setGuessResult( "So you have nothing?");
		
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		//frame.setSize(750, 180);  // size the frame
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
	}
	private Color parseColor(String color) {
	    return switch(color.toLowerCase()) {
	        case "green"  -> Color.GREEN;
	        case "purple" -> Color.MAGENTA;
	        case "gray"   -> Color.GRAY;
	        case "red"    -> Color.RED;
	        case "white"  -> Color.YELLOW;
	        case "black"  -> Color.DARK_GRAY;
	        default        -> Color.WHITE;
	    };
	}
	/*
	public class Solution {
	    private String person, weapon, room;
	    public Solution(String person, String weapon, String room) {
	        this.person = person;
	        this.weapon = weapon;
	        this.room   = room;
	    }
	    public String getPerson() { return person; }
	    public String getWeapon() { return weapon; }
	    public String getRoom()   { return room;   }
	    @Override
	    public String toString() {
	        return String.format("%s in the %s with the %s", person, room, weapon);
	    }
	}
	*/


	public class SuggestionDialog extends JDialog {
	    private JComboBox<String> personBox, weaponBox;
	    
	    private String[] suggestion;

	    public SuggestionDialog(Frame owner,
	                            java.util.List<String> people,
	                            java.util.List<String> weapons,
	                            String currentRoom)
	    {
	        super(owner, "Make a Suggestion", true);
	        setLayout(new GridBagLayout());
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(4,4,4,4);
	        gbc.fill = GridBagConstraints.HORIZONTAL;

	        // Room (read-only)
	        gbc.gridx=0; gbc.gridy=0;
	        add(new JLabel("Room:"), gbc);
	        roomField = new JTextField(currentRoom);
	        roomField.setEditable(false);
	        gbc.gridx=1;
	        add(roomField, gbc);

	        // Person dropdown
	        gbc.gridx=0; gbc.gridy=1;
	        add(new JLabel("Person:"), gbc);
	        personBox = new JComboBox<>(people.toArray(new String[0]));
	        gbc.gridx=1;
	        add(personBox, gbc);

	        // Weapon dropdown
	        gbc.gridx=0; gbc.gridy=2;
	        add(new JLabel("Weapon:"), gbc);
	        weaponBox = new JComboBox<>(weapons.toArray(new String[0]));
	        gbc.gridx=1;
	        add(weaponBox, gbc);

	        // Buttons
	        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        JButton submit = new JButton("Submit");
	        JButton cancel = new JButton("Cancel");
	        buttonRow.add(submit);
	        buttonRow.add(cancel);

	        gbc.gridx=0; gbc.gridy=3;
	        gbc.gridwidth=2;
	        add(buttonRow, gbc);

	        // Button logic
	        submit.addActionListener(e -> {
	            suggestion = new String[3];
	            suggestion[0] = (String)personBox.getSelectedItem();
	            suggestion[1] = (String)weaponBox.getSelectedItem();
	            suggestion[2] = roomField.getText();
	            
	            dispose();
	        });
	        cancel.addActionListener(e -> dispose());

	        pack();
	        setLocationRelativeTo(owner);
	    }

	    /** @return the Solution chosen, or null if Cancel was hit. */
	    public String[] getSuggestion() {
	        return suggestion;
	    }
	}
	
	public class AccusationDialog extends JDialog {
	    private JComboBox<String> personBox, weaponBox, roomBox;
	    private String[] accusation;

	    public AccusationDialog(Frame owner,
	                            List<String> people,
	                            List<String> weapons,
	                            List<String> rooms)
	    {
	        super(owner, "Make an Accusation", true);
	        setLayout(new GridBagLayout());
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(4,4,4,4);
	        gbc.fill = GridBagConstraints.HORIZONTAL;

	        // Person dropdown
	        gbc.gridx = 0; gbc.gridy = 0;
	        add(new JLabel("Person:"), gbc);
	        personBox = new JComboBox<>(people.toArray(new String[0]));
	        gbc.gridx = 1;
	        add(personBox, gbc);

	        // Weapon dropdown
	        gbc.gridx = 0; gbc.gridy = 1;
	        add(new JLabel("Weapon:"), gbc);
	        weaponBox = new JComboBox<>(weapons.toArray(new String[0]));
	        gbc.gridx = 1;
	        add(weaponBox, gbc);

	        // Room dropdown
	        gbc.gridx = 0; gbc.gridy = 2;
	        add(new JLabel("Room:"), gbc);
	        roomBox = new JComboBox<>(rooms.toArray(new String[0]));
	        gbc.gridx = 1;
	        add(roomBox, gbc);

	        // Buttons row
	        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        JButton submit = new JButton("Submit");
	        JButton cancel = new JButton("Cancel");
	        buttonRow.add(submit);
	        buttonRow.add(cancel);

	        gbc.gridx = 0; gbc.gridy = 3;
	        gbc.gridwidth = 2;
	        add(buttonRow, gbc);

	        // Button logic
	        submit.addActionListener(e -> {
	            accusation = new String[3];
	            
	                accusation[0] = (String)personBox.getSelectedItem();
	                accusation[1] = (String)weaponBox.getSelectedItem();
	                accusation[2] =(String)roomBox.getSelectedItem();
	            
	            dispose();
	        });
	        cancel.addActionListener(e -> dispose());


	        pack();
	        setLocationRelativeTo(owner);
	    }

	    /** @return the chosen accusation, or null if Cancel was hit. */
	    public String[] getSolution() {
	        return accusation;
	    }
	}


}

