import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class login implements ActionListener {

    JLabel label;
	JFrame frame;
	JFrame frame2;
	JPanel panel;
	JTextField enter1;
	JTextField enter2;
	static String x;
	static String y;
	
	static boolean moveon = false;
	
	public login() {
		frame = new JFrame(); // for the coordinates
		JButton button = new JButton("Play");
		button.addActionListener(this);
		//JButton button2 = new JButton("Create new Account");
		button.addActionListener(this);
		label = new JLabel("Please enter your credentials");
		panel = new JPanel();
		JLabel username = new JLabel("Username:");
		JLabel password = new JLabel("Password:");
		enter1 = new JTextField(10);
		enter2 = new JTextField(10);
		panel.setBackground(new Color(143, 170, 220));
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(0, 1));	
		panel.add(label);
		panel.add(username);
		panel.add(enter1);
		panel.add(password);
		panel.add(enter2);
		panel.add(button);
		//panel.add(button2);
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Login screen");
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		moveon = true;
		
	}
	
}
