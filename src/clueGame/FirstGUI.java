package clueGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FirstGUI extends JFrame implements ActionListener {
	
	public FirstGUI() {
		//add components
		JLabel nameLabel = new JLabel("Name");
		JTextField nameTF = new JTextField(20);
		add(nameLabel, BorderLayout.NORTH);
		add(nameTF, BorderLayout.CENTER);
		
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
		
		String message = "Hello " + nameTF.getTExt();
		JOptionPanel.showMessageDialog(myFrame, message);
		
		
	}
	
}
