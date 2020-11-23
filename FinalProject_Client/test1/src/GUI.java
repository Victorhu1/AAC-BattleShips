import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI extends JFrame {

	map A;
	map B;
	int spacing = 5;

	JTextField xcoord;
	JTextField ycoord;
	static String x;
	static String y;
	JPanel panel;
	int num;
	JLabel label;

	public void setmap(map A, map B) {
		this.A = A;
		this.B = B;
	}

	public void setmap(map A) {
		this.A = A;
	}

	public void initialize() {
		map emptymap = new map(8, 8);
		A = emptymap;
		B = emptymap;
	}

	public GUI(int num) {
		initialize();
		this.num = num;
		if (num == 1) {
			this.setTitle("Your Map");
		} else {
			this.setTitle("Hit Record");
		}
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		Board board = new Board();		
		this.setContentPane(board);
	}

	public class Board extends JPanel {

		public Board() {
			if (num == 1) {
				label = new JLabel("Score: " + B.countScore());
				label.setFont(new Font("Calibri", Font.PLAIN, 20));
				this.add(label);
			}
		}	

		public void paintComponent(Graphics g) {
			if (num == 1) {
				label.setText("Score: " + B.countScore());
				System.out.println("Score: " + B.countScore());
				if(B.countScore() == 5) {
					label.setText("CONGRATULATIONS: YOU HAVE DEFEATED YOUR OPPOSITION, please quit the application");
				} else if (A.countScore() == 5) {
					label.setText("YOU HAVE BEEN DEFEATED BY YOUR OPPOSITION, please quit the application");
				}
				g.setColor(new Color(97, 164, 167));
			} else if (num == 2) {
				g.setColor(new Color(255, 140, 105));
			}

			g.fillRect(0, 0, 800, 800);
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (A.isOccupied(j, i)) {
						if (num == 1) {
							g.setColor(new Color(146, 208, 80));
						}
					} else if (A.isDead(j, i)) {
						g.setColor(Color.orange);
					} else {
						g.setColor(Color.gray);
					}
					g.fillRect(spacing + i * 80, spacing + j * 80 + 80, 80 - 2 * spacing, 80 - 2 * spacing);
				}
			}
		}
	}
}
