import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Client implements ActionListener{

	private static final String ip = "localhost";
    private static final int port = 4321;
    public static String username;  
    JLabel label;
	JFrame frame;
	JFrame frame2;
	JPanel panel;
	JTextField xcoord;
	JTextField ycoord;
	static String x;
	static String y;
	static boolean loggedin = false;
	static boolean input = false;	
	static Socket socket ;
	static ServerThread serverHandler;
	static BufferedReader br ;
	static PrintWriter pw ;
	static JTextArea jTextArea;
	JLabel label3;
	JLabel label4;	
	static int numGuess = 0;	
	static login screen;
	static int round = -1;
	
	public Client() {
		frame = new JFrame(); // for the coordinates
		JButton button = new JButton("Shoot!");
		button.addActionListener(this);
		//jTextArea = new JTextArea();
		//jTextArea.append("Welcome to bootleg battleships");
		label = new JLabel("Please type in CS 201 to start the game");
		label.setFont(new Font("Calibri", Font.PLAIN, 20));
		JLabel label2 = new JLabel("PLEASE ATTACK ON A CHOSEN LOCATION! Format (character integer) e.g [a 3]");
		label2.setFont(new Font("Calibri", Font.PLAIN, 14));
		label3 = new JLabel("Round: 0");
		label3.setFont(new Font("Calibri", Font.PLAIN, 14));
		panel = new JPanel();
		xcoord = new JTextField(10);
		ycoord = new JTextField(10);
		panel.setBackground(new Color(143, 170, 220));
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(0, 1));	
		panel.add(label);
		panel.add(label2);
		panel.add(label3);
		panel.add(xcoord);
		panel.add(ycoord);
		panel.add(button);
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Control Panel");
		frame.pack();
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		round++;
		x = xcoord.getText();
		y = ycoord.getText();
		if((x.toLowerCase().equals("cs")) && (Integer.parseInt(y) == 201)){
			label.setText("The game has started");
		} else {
			label.setText("Player has chose to shoot at " + x + " " + y);
			
		}
		label3.setText("Round: " + round);
		//label4.setText("Score: " + );
		pw.println(x + " " + Integer.parseInt(y));	
		numGuess += 1;
		if (numGuess == 10) 
		{
			pw.println("done");
		}
	}
	
    public static void main(String[] args)
    {
    	//screen = new login();

		new Client();
    	onto();
    	
    }
    public static void onto() {
    	try 
    	{
    		final Scanner scanner = new Scanner(System.in);
			socket = new Socket(ip, port);
			serverHandler = new ServerThread(socket);
			br = new BufferedReader(new InputStreamReader(System.in));
			pw = new PrintWriter(socket.getOutputStream(), true);
			//Map<String,String> mymap = new HashMap<String,String>();
			String fname;
			String lname;
			boolean login = true; 
			boolean complete = false;
			String db = "jdbc:mysql://localhost/201_Final_Project";
			String user = "root";
			String pwd = "PasswordforJae123";
			//2 choices: Create account or login
		/*	while (true) {
				if (complete==false) {
					System.out.println("Login or Create Account or Use Guest Account");
					String x = scanner.nextLine();
					if (x.equals("l")) {
						login=true;
					}
					else {
						login= false;
					}
					if (login==true) { 
						while (true) {
							System.out.println("Username:");
							String u = scanner.nextLine();
							System.out.println("Password:");
							String p = scanner.nextLine();
								try {
									Connection conn = DriverManager.getConnection(db, user, pwd);
									PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM General_Info WHERE Username = ?");
									ps.setString(1,u);
									ResultSet rs = ps.executeQuery();
									int n = 0;
									if ( rs.next() ) {
									    n = rs.getInt(1);
									}
									if ( n == 0 ) {
										//username does not exist
										System.out.println("Please create account");
										break;
									}
									else {
										PreparedStatement ps1 = conn.prepareStatement("SELECT Password FROM General_Info WHERE Username = ?");
										ps1.setString(1,u);
										ResultSet rs1 = ps1.executeQuery();
										String s = "";
										if ( rs1.next() ) {
									    	s = rs1.getString(1);
										}
										if (s.equals(p)) {
											System.out.println("Logging in!");
											complete = true;
											username = u;
											break;
										}
										else {
											System.out.println("Wrong Username and Password. Try again!");
										}
									}
								}
								catch (SQLException ex) {
									System.out.println ("SQLException: " + ex.getMessage());
								}
						}
					}
					else {
						if (x.equals("g")) {
							System.out.println("Creating guest account");
							try {
								Connection conn = DriverManager.getConnection(db, user, pwd);
								PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM General_Info WHERE Guest = 1");
								ResultSet rs = ps.executeQuery();
								int s = 0;
								if ( rs.next() ) {
							    	s = rs.getInt(1);
								}
								String name = "Guest" + s;
								username = null;
								try {
										String sql = " INSERT INTO General_Info"
										        + " VALUES (?, ?, ?, ?, ?, ?)";

										      // create the mysql insert preparedstatement
										      PreparedStatement preparedStmt = conn.prepareStatement(sql);
										      preparedStmt.setString (1, name);
										      prep aredStmt.setNull(2, java.sql.Types.INTEGER);
										      preparedStmt.setNull(3, java.sql.Types.INTEGER);
										      preparedStmt.setNull(4, java.sql.Types.INTEGER);
										      preparedStmt.setNull(5, java.sql.Types.INTEGER);
										      preparedStmt.setInt(6, 1);

										      // execute the preparedstatement
										      preparedStmt.execute();
										} catch (SQLException ex) {
											System.out.println ("SQLException: " + ex.getMessage());
										}
							} catch (SQLException ex) {
								System.out.println ("SQLException: " + ex.getMessage());
							}
							complete = true;
						}
						else {
							System.out.println("New Account Creation");
							System.out.println("First Name:");
							fname = scanner.nextLine();
							System.out.println("Last Name:");
							lname = scanner.nextLine();
							//needs to check if username already exists!
							String u;
							while (true) {
								System.out.println("Username:");
								u = scanner.nextLine();
								try {
									Connection conn = DriverManager.getConnection(db, user, pwd);
									PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM General_Info WHERE Username = ?");
									ps.setString(1,u);
									ResultSet rs = ps.executeQuery();
									int n = 0;
									if ( rs.next() ) {
									    n = rs.getInt(1);
									}
									if ( n > 0 ) {
									   System.out.println("Username already exists!");
									}
									else {
										username= u;
										break;
									}
								}
								catch (SQLException ex) {
									System.out.println ("SQLException: " + ex.getMessage());
								}
							}
							System.out.println("Password:");
							String p = scanner.nextLine();
							//mymap.put(u, p);
							try {
								Connection conn = DriverManager.getConnection(db, user, pwd);
								String sql = " INSERT INTO General_Info"
								        + " VALUES (?, ?, ?, ?, ?, ?)";

								      // create the mysql insert preparedstatement
								      PreparedStatement preparedStmt = conn.prepareStatement(sql);
								      preparedStmt.setString (1, u);
								      preparedStmt.setString (2, p);
								      preparedStmt.setString (3, fname);
								      preparedStmt.setString(4, lname);
								      preparedStmt.setString(5, "0-0-0");
								      preparedStmt.setInt(6, 0);

								      // execute the preparedstatement
								      preparedStmt.execute();
								} catch (SQLException ex) {
									System.out.println ("SQLException: " + ex.getMessage());
								}
							
						}
					}
				}
				else {
					break;
				}
			}
			
			*/
			//System.out.println(getUsername());
			
			//while(screen.moveon == false) {}
			
			new Thread(serverHandler).start();
			
			while (true)
			{
				String console = br.readLine();
				jTextArea.append(console);
			}
		} 
    	catch (UnknownHostException e) 
    	{
			e.printStackTrace();
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    	
    }
}