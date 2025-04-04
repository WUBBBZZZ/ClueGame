package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class FirstGUI extends JFrame implements ActionListener {
	
	private FirstGUI gui;
	private JTextField nameTF;
	
	public FirstGUI() {
		//add components
		gui = this;
		
		JPanel panel = new JPanel();
		
		JLabel nameLabel = new JLabel("Name:");
		JTextField nameTF = new JTextField(20);
		//add(nameLabel, BorderLayout.NORTH);
		//add(nameTF, BorderLayout.CENTER);
		panel.add(nameLabel);
		panel.add(nameTF);
		add(panel, BorderLayout.CENTER);
		
		JButton nameButton = new JButton("OK");
		add(nameButton, BorderLayout.SOUTH);
		nameButton.addActionListener(this);
		
		setTitle("My First GUI");
		setSize(300, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		FirstGUI gui = new FirstGUI();
		gui.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Button pressed");
		
		String message = "Hello " + nameTF.getText();
		JOptionPane.showMessageDialog(gui, message);
		
		String ageStr = JOptionPane.showInputDialog("Enter your age: ");
		int num = Integer.parseInt(ageStr);
		JOptionPane.showMessageDialog(gui,  "You are " + num + " years old.");
		
		int ready = JOptionPane.showConfirmDialog(gui,  "Are you ready?");
		if (ready == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(gui,  "Here we go!");
		} else {
			JOptionPane.showMessageDialog(gui, "Ok, I'll wait!");
		}
		
	}
	
}
