
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class ClientThread implements Runnable {
	
	private BufferedReader br;
	private PrintWriter pw;
	private Socket clientSocket;
	private ArrayList<ClientThread> clients;
	public boolean finishedGuessing = false;
	private ArrayList<String> wrong = new ArrayList<String>();
	private ArrayList<String> correct  = new ArrayList<String>();
	private int id;
	private int round = 0;
	//private String username;
	
	private player clientPlayer;
	private Game game;
	Semaphore s = new Semaphore(1);
	int[] record = new int[3];
	int yourid = 0;
	int oppositionid = 0;
	
	public ClientThread(int id, Socket socket, ArrayList<ClientThread> clients)
	{
		//this.username = Client.getUsername();
		this.id = id; 
		finishedGuessing = false;
		this.clientSocket = socket;
		this.clients = clients;
		try {
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			pw = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/*public String getUsername() {
		return username;
	}*/
	
	public void setGame(Game g)
	{
		this.game = g;
	}
	
	public void setPlayer(player p)
	{
		this.clientPlayer = p;
	}
	
	public player getPlayer()
	{
		return clientPlayer;
	}
	
	public int getScore() {
		return this.correct.size();
	}
	
	public boolean allFinished()
	{
		return this.finishedGuessing;
	}
	
	public int[] getRecord() {
		return record;
	}
	
	public void setRecord(int[] record) {
		this.record = record;
	}
	/*public void record() {
		try {
			String username1 = clients.get(yourid).getUsername();
			if (username1==null) {
				return;
			}
			String db = "jdbc:mysql://localhost/201_Final_Project";
			String user = "root";
			String pwd = "PasswordforJae123";
			Connection conn = DriverManager.getConnection(db, user, pwd);
			PreparedStatement ps = conn.prepareStatement("SELECT Win_Record FROM General_Info WHERE Username = ?");
			ps.setString(1,username1);
			ResultSet rs = ps.executeQuery();
			String n = "";
			if ( rs.next() ) {
				n = rs.getString(1);
			}
			String s = n;
			String[] inputArray = s.split("-");
			for (int i=0; i<inputArray.length; i++) {
				pw.println(Integer.parseInt(inputArray[i]));
				record[i] = Integer.parseInt(inputArray[i]);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}*/
	
	
	@Override
	public void run() {
		//pw.println("Welcome" + username);
		try
		{
			//to wait for turn 
			while (true) {
			if (Server.getID()==0 && this.id==1) {
					System.out.println("Player A goes first (10 seconds to go)");

			}
			else {
				pw.println("Waiting for opposition (10 seconds)");
				for (int i=0; i<=10; i++) {
					pw.println(i+ "seconds");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
				boolean x = Server.setID(id);
				pw.println("PLEASE ATTACK ON A CHOSEN LOCATION! Format (character integer) e.g [a 3]");
				int roundleft = 10 - round;
				pw.println("YOU HAVE " + roundleft + " ROUNDS LEFT!");
				String clientInput = "";
				boolean guessResult;
				//check if input is correct 
				while (true) {
					try {
						clientInput = br.readLine();
						String[] inputArray = clientInput.split(" ");
						char rawVer = inputArray[0].charAt(0);
						int ver = rawVer - 97;
						int hor = Integer.valueOf(inputArray[1]);
						if (ver<0 || ver>10 || hor<0 || hor>10) {
							pw.println("Input out of bound");
						}
						else if (correct.contains(clientInput) || wrong.contains(clientInput)) {
							pw.println("Already been inputted");
						}
						else {
							guessResult = this.clientPlayer.attackShip(ver, hor);
							break;
						}
					}
					catch (NumberFormatException e) {
						pw.println("Not a number");
					} catch (StringIndexOutOfBoundsException e) {
						pw.println("Invalid input (No space before coordinate");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						pw.println("Invalid input");
						continue;
					} 
				}
				
				//number of rounds
				round++;
				
				System.out.println(this.clientPlayer.getPlayerName() + " GUESSED " + clientInput);
				try {
					
					if (guessResult==true) {
						pw.println("Shot on target");
						correct.add(clientInput);
					}
					else {
						pw.println("Miss");
						wrong.add(clientInput);
					}
					pw.println("Correct hits: " + correct.toString()) ;
					pw.println("Missed hits: " + wrong.toString());
					pw.println("Score: " + correct.size() +  "/4");
					oppositionid = 1;
					yourid = 0;
					if (this ==clients.get(1)) {
						oppositionid = 0;
						yourid = 1;
					}
					pw.println("Opponent Score: " + clients.get(oppositionid).getScore());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (round==10) 
				{
					finishedGuessing = true;
					pw.println("YOU HAVE RUN OUT YOUR GUESSES. WAIT FOR GAME RESULT.");
					break;
				}
				if (correct.size()==4) {
					finishedGuessing = true;
					pw.println("You have completed the game in " + round + " rounds");
					break;
				}

			}
			while (true) {
				//record();
				if (clients.get(oppositionid).allFinished()) {
					int winner = game.getWinner();
					int [] temprecord1 = clients.get(0).getRecord();
					int[] temprecord2 = clients.get(1).getRecord();
					if (winner == 1)
					{
						temprecord1[0]++;
						temprecord2[1]--;
						clients.get(0).pw.println("CONGRATULATIONS! YOU ARE THE WINNER!");
						clients.get(1).pw.println("SORRY! YOU LOST!");
						//break;
					}
					else if (winner == 2)
					{
						temprecord1[0]--;
						temprecord2[1]++;
						clients.get(1).pw.println("CONGRATULATIONS! YOU ARE THE WINNER!");
						clients.get(0).pw.println("SORRY! YOU LOST!");
						//break;
					}
					else
					{
						temprecord1[2]++;
						temprecord2[2]++;
						clients.get(0).pw.println("IT'S A DRAW!");
						clients.get(1).pw.println("IT'S A DRAW!");
						//break;
					}
					
					/*clients.get(0).setRecord(temprecord1);
					String username1 = clients.get(0).getUsername();
					String username2 = clients.get(1).getUsername();
					String temps1 = clients.get(0).getRecord()[0] + "-" + clients.get(0).getRecord()[1] + "-" + clients.get(0).getRecord()[2]; 
					String temps2 = clients.get(1).getRecord()[0] + "-" + clients.get(1).getRecord()[1] + "-" + clients.get(1).getRecord()[2];
					clients.get(1).setRecord(temprecord2);
					String db = "jdbc:mysql://localhost/201_Final_Project";
					String user = "root";
					String pwd = "PasswordforJae123";
					String sql = "UPDATE General_Info SET Win_Record = ? WHERE Username = ?";

					try (Connection conn = DriverManager.getConnection(db, user, pwd);
						  PreparedStatement ps = conn.prepareStatement(sql);) {
						ps.setString(1, temps1);
						ps.setString(2, username1);
					} catch (SQLException sqle) {
						System.out.println ("SQLException: " + sqle.getMessage());
					}
					try (Connection conn = DriverManager.getConnection(db, user, pwd);
							  PreparedStatement ps = conn.prepareStatement(sql);) {
							ps.setString(1, temps2);
							ps.setString(2, username2);
						} catch (SQLException sqle) {
							System.out.println ("SQLException: " + sqle.getMessage());
						}
					//getting record
					System.out.println(username1 + ": " + temps1);
					System.out.println(username2 + ": " + temps2);
					try {
						if (!username1.substring(0, 6).equals("Guest")) {
							//show record
							clients.get(0).pw.println("Your record is " + temps1 + " !");
						}
						else {
							clients.get(0).pw.println("Guest cannot see record!");
						}
						if (!username2.substring(0, 6).equals("Guest")) {
							//show record
							clients.get(0).pw.println("Your record is " + temps1 + " !");
						}
						else {
							clients.get(0).pw.println("Guest cannot see record!");
						}
					}catch (Exception e) {
						clients.get(0).pw.println("Your record is " + temps1 + " !");
					}*/
					clients.get(0).pw.println("gameover");
					clients.get(1).pw.println("gameover");
					break;
				}
			}
			clientSocket.close();
		}
		catch (IOException e) //| InterruptedException e
		{
			e.printStackTrace();
		} /*catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		finally
		{
			// server outputs who's winner?
			try
			{
				br.close();
				pw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}