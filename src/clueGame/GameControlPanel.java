package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameControlPanel extends JPanel {

	private String playerName = "";
	private String playerColor = "";
	private int roll;
	private String guess = "";
	private String guessResult = "";
	
	private JTextField playerNameField;
	private JTextField rollField;
	private JTextField guessField;
	private JTextField guessResultField;
	
	private JButton button1;
	private JButton button2;
	
	public GameControlPanel() {
		
		//Outer JPanel (2x0) - GameControlPanel
		//setLayout(new GridLayout(2, 1));
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
		
				//JButton, "NEXT"
				button2 = new JButton("NEXT");
				button2.setBackground(Color.GRAY);
				button2.setOpaque(true);
				button2.setBorderPainted(true);
				button2.addActionListener(new ActionListener() {
			        @Override
			        public void actionPerformed(ActionEvent e) {
			            // parent → the window that contains this panel:
			            Window parent = SwingUtilities.getWindowAncestor(GameControlPanel.this);
			            JOptionPane.showMessageDialog(
			                parent,
			                "You clicked NEXT!",
			                "Next Turn",
			                JOptionPane.INFORMATION_MESSAGE
			            );
			        }
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
	
	public void ButtonListenerHelper() {
		
		//Next Player Pressed
		
		//If Current human player finished: 
			//update current player
		//Else:
			//error message, end.
		
		//Roll dice
		
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
	
	public void setTurn(Player player, int n) {
		playerName = player.getName();
		playerNameField.setText(playerName);
		
		playerColor = player.getColor();
		roll = n;
        if (playerColor.equals("Green")) {
        	playerNameField.setBackground(Color.GREEN);
        	playerNameField.setOpaque(true);
        } else if (playerColor.equals("Purple")) {
        	playerNameField.setBackground(Color.MAGENTA);
        	playerNameField.setOpaque(true);
        } else if (playerColor.equals("Gray")) {
        	playerNameField.setBackground(Color.GRAY);
        	playerNameField.setOpaque(true);
        } else if (playerColor.equals("Red")) {
        	playerNameField.setBackground(Color.RED);
        	playerNameField.setOpaque(true);
        } else if (playerColor.equals("Yellow")) {
        	playerNameField.setBackground(Color.YELLOW);
        	playerNameField.setOpaque(true);
        } else if (playerColor.equals("Black")) {
        	playerNameField.setBackground(Color.DARK_GRAY);
        	playerNameField.setOpaque(true);
        } 
        
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
	
}

