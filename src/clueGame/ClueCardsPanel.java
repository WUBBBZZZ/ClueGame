package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.Player;
import clueGame.HumanPlayer;
import clueGame.ComputerPlayer;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Solution;

public class ClueCardsPanel extends JPanel {

	//JTextField variables for updating card information
	
	private JTextField peopleInHand;
	private JTextField peopleInSeen;
	private JTextField roomsInHand;
	private JTextField roomsInSeen;
	private JTextField weaponsInHand;
	private JTextField weaponsInSeen;
	
	//For managing the seen cards to avoid repeats
	private static ArrayList<Card> checkSeen = new ArrayList<Card>();
	
	private JPanel peoplePanel;
	private JPanel roomsPanel;
	private JPanel weaponsPanel;
	
	private static Board board;
	
	public ClueCardsPanel() {
		
		//Outer JPanel ("Known Cards"), (3x1)
		setLayout(new BorderLayout());
		JPanel outerPanel = new JPanel(new GridLayout(3, 1));
	    outerPanel.setBorder(BorderFactory.createTitledBorder("Known Cards"));
	   // outerPanel.setSize(200, 500);
		
			//"People" JPanel. 2 JLabels and 7 possible JTextFields
		    peoplePanel = new JPanel(new GridLayout(9,1));
		    peoplePanel.setBorder(BorderFactory.createTitledBorder("People"));
		    //peoplePanel.setSize(200, 200);
		    
		    JLabel peopleHand = new JLabel("In Hand:");
		    peopleInHand = new JTextField("None", 20);
		    
		    JLabel peopleSeen = new JLabel("Seen:");
		    peopleInSeen = new JTextField("None", 20);
		    
		    peoplePanel.add(peopleHand);
		    peoplePanel.add(peopleInHand);
		    peoplePanel.add(peopleSeen);
		    peoplePanel.add(peopleInSeen);
		    
		    outerPanel.add(peoplePanel, BorderLayout.NORTH);
			
			//"Rooms" JPanel. 2 JLabels and 9 possible JTextFields
		    roomsPanel = new JPanel(new GridLayout(11,1));
		    roomsPanel.setBorder(BorderFactory.createTitledBorder("Rooms"));
		   //roomsPanel.setSize(200, 200);
		    
		    JLabel roomsHand = new JLabel("In Hand:");
		    roomsInHand = new JTextField("None", 20);
		    
		    JLabel roomsSeen = new JLabel("Seen:");
		    roomsInSeen = new JTextField("None", 20);
		    
		    roomsPanel.add(roomsHand);
		    roomsPanel.add(roomsInHand);
		    roomsPanel.add(roomsSeen);
		    roomsPanel.add(roomsInSeen);
		    
		    outerPanel.add(roomsPanel, BorderLayout.CENTER);
		
			//"Weapons" JPanel. 2 JLabels and 7 possible JTextFields
		    weaponsPanel = new JPanel(new GridLayout(9,1));
		    weaponsPanel.setBorder(BorderFactory.createTitledBorder("Weapons"));
		    //weaponsPanel.setSize(200, 200);
		    
		    JLabel weaponsHand = new JLabel("In Hand:");
		    weaponsInHand = new JTextField("None", 20);
		    
		    JLabel weaponsSeen = new JLabel("Seen:");
		    weaponsInSeen = new JTextField("None", 20);
		    
		    weaponsPanel.add(weaponsHand);
		    weaponsPanel.add(weaponsInHand);
		    weaponsPanel.add(weaponsSeen);
		    weaponsPanel.add(weaponsInSeen);
		    
		    outerPanel.add(weaponsPanel, BorderLayout.SOUTH);
		    
		this.add(outerPanel, BorderLayout.CENTER);
				
	}
	
	public static void main(String[] args) {
		
		ClueCardsPanel.checkSeen.clear();
		ClueCardsPanel panel = new ClueCardsPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(300, 600);  // size the frame
		//frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		//Test hands in deck
		HumanPlayer Genji = new HumanPlayer("Genji", "Green", 0, 0);
		Card card1 = new Card("Mercy", CardType.SUSPECT);
		Card card2 = new Card("NanoBlade", CardType.WEAPON);
		Card card3 = new Card("Closet", CardType.ROOM);
		Genji.updateHand(card1);
		Genji.updateHand(card2);
		Genji.updateHand(card3);
		ClueCardsPanel.checkSeen.add(card1);
		ClueCardsPanel.checkSeen.add(card2);
		ClueCardsPanel.checkSeen.add(card3);
		
		panel.setUp(Genji);
		
		//Test seen cards from two other players
		ComputerPlayer Sombra = new ComputerPlayer("Sombra", "Purple", 1, 0);
		Card card4 = new Card("Moira", CardType.SUSPECT);
		Card card5 = new Card("SawCleaver", CardType.WEAPON);
		Card card6 = new Card("Bar", CardType.ROOM);
		Sombra.updateHand(card4);
		Sombra.updateHand(card5);
		Sombra.updateHand(card6);
		
		ComputerPlayer Mercy = new ComputerPlayer("Mercy", "Yellow", 2, 0);
		Card card7 = new Card("Genji", CardType.SUSPECT);
		Card card8 = new Card("DaedricMace", CardType.WEAPON);
		Card card9 = new Card("Mancave", CardType.ROOM);
		Mercy.updateHand(card7);
		Mercy.updateHand(card8);
		Mercy.updateHand(card9);
		
		//Sombra makes a suggestion, that Genji did it with a daedric mace in the Bar.
		//Mercy disproves the suggestion by showing her two disproving cards.
		Genji.addSeen(card7);
		Genji.addSeen(card8);
		panel.addToPanel(Genji, Mercy);
		
		//Mercy makes a suggestion, that Reinhardt did it with a Saw Cleaver in the Bar.
		//Sombra disproves the suggestion by showing her two disproving cards.
		Genji.addSeen(card5);
		Genji.addSeen(card6);
		panel.addToPanel(Genji, Sombra);
		
	}
	
	public void setUp(Player player) {
		//sets up player's cards
		for (Card c : player.getHand()) {
		
			if (c.getCardType().equals(CardType.ROOM)) {
					JTextField newField = new JTextField(c.getCardName(), 20);
			        roomsPanel.add(newField, 1);
			        roomsPanel.remove(roomsInHand);
			        JTextFieldHelper.recolor(player, newField);
			        // Update the layout:
			        roomsPanel.revalidate();
			        roomsPanel.repaint();
				} else if (c.getCardType().equals(CardType.SUSPECT)) {
					JTextField newField = new JTextField(c.getCardName(), 20);
					peoplePanel.add(newField, 1);
					peoplePanel.remove(peopleInHand);
					JTextFieldHelper.recolor(player, newField);
					peoplePanel.revalidate();
					peoplePanel.repaint();
				} else if (c.getCardType().equals(CardType.WEAPON)) {
					JTextField newField = new JTextField(c.getCardName(), 20);
					weaponsPanel.add(newField, 1);
					weaponsPanel.remove(weaponsInHand);
					JTextFieldHelper.recolor(player, newField);
					weaponsPanel.revalidate();
					weaponsPanel.repaint();
				}
			ClueCardsPanel.checkSeen.add(c);
			
		}
		
	}
	/*
	 * player 1 is human player
	 * player 2 is computer player
	 */
	public void addToPanel(Player player1, Player player2) {
		boolean n;
		for (Card c : player1.getSeen()) {
			n = false;
			for (Card d : ClueCardsPanel.checkSeen) {
				if (c.getCardName().equals(d.getCardName())) {
					n = true;
				}
			}
			
				if (!n && c.getCardType().equals(CardType.ROOM)) {
					JTextField newField = new JTextField(c.getCardName(), 20);
			        roomsPanel.add(newField);
			        roomsPanel.remove(roomsInSeen);
			        JTextFieldHelper.recolor(player2, newField);
			        // Update the layout:
			        roomsPanel.revalidate();
			        roomsPanel.repaint();
				} else if (!n && c.getCardType().equals(CardType.SUSPECT)) {
					JTextField newField = new JTextField(c.getCardName(), 20);
					peoplePanel.add(newField);
					peoplePanel.remove(peopleInSeen);
					JTextFieldHelper.recolor(player2, newField);
					peoplePanel.revalidate();
					peoplePanel.repaint();
				} else if (!n && c.getCardType().equals(CardType.WEAPON)) {
					JTextField newField = new JTextField(c.getCardName(), 20);
					weaponsPanel.add(newField);
					weaponsPanel.remove(weaponsInSeen);
					JTextFieldHelper.recolor(player2, newField);
					weaponsPanel.revalidate();
					weaponsPanel.repaint();
				}
				ClueCardsPanel.checkSeen.add(c);
			
		}
    }
	
	public class JTextFieldHelper {
		
		private static void recolor(Player player, JTextField field) {
			
			String playerColor = player.getColor();
			
			if (playerColor.equals("Green")) {
				field.setBackground(Color.GREEN);
				field.setOpaque(true);
	        } else if (playerColor.equals("Purple")) {
	        	field.setBackground(Color.MAGENTA);
	        	field.setOpaque(true);
	        } else if (playerColor.equals("Gray")) {
	        	field.setBackground(Color.GRAY);
	        	field.setOpaque(true);
	        } else if (playerColor.equals("Red")) {
	        	field.setBackground(Color.RED);
	        	field.setOpaque(true);
	        } else if (playerColor.equals("Yellow")) {
	        	field.setBackground(Color.YELLOW);
	        	field.setOpaque(true);
	        } else if (playerColor.equals("Black")) {
	        	field.setBackground(Color.DARK_GRAY);
	        	field.setOpaque(true);
	        }
		}
		
	}
}
