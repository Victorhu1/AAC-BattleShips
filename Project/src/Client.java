import java.io.BufferedReader;
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

public class Client {

	private static final String ip = "localhost";
    private static final int port = 4321;
    public static String username;
    
    public static void main(String[] args)
    {
    	login();
    }
    public static void login() {
    	try 
    	{
    		final Scanner scanner = new Scanner(System.in);
			Socket socket = new Socket(ip, port);
			ServerThread serverHandler = new ServerThread(socket);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			//Map<String,String> mymap = new HashMap<String,String>();
			String fname;
			String lname;
			boolean login = true; 
			boolean complete = false;
			String db = "jdbc:mysql://localhost/201_Final_Project";
			String user = "root";
			String pwd = "PasswordforJae123";
			//2 choices: Create account or login
			while (true) {
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
										      preparedStmt.setNull(2, java.sql.Types.INTEGER);
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
			
			//System.out.println(getUsername());
			new Thread(serverHandler).start();
			
			int numGuess = 0;
			while (true)
			{
				
				String guess = br.readLine();
				pw.println(guess);	
				numGuess += 1;
				if (numGuess == 10) 
				{
					pw.println("done");
				}
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