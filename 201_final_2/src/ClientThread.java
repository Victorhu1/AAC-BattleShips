
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientThread implements Runnable {
	
	private BufferedReader br;
	private PrintWriter pw;
	private Socket clientSocket;
	private ArrayList<ClientThread> clients;
	private static int turns = 5;
	public boolean finishedGuessing;
	
	private player clientPlayer;
	private Game game;
	
	public ClientThread(Socket socket, ArrayList<ClientThread> clients)
	{
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
	
	public boolean allFinished()
	{
		for (ClientThread each : clients)
		{
			if (!each.finishedGuessing) 
				return false;
		}
		return true;
	}
	
	@Override
	public void run() {
		try
		{
			while (true)
			{
				pw.println("PLEASE ATTACK ON A CHOSEN LOCATION!");
				String clientInput = br.readLine();		
				System.out.println(this.clientPlayer.getPlayerName() + "GUESSED" + clientInput);
				if (clientInput.equals("done")) 
				{
					finishedGuessing = true;
					pw.println("YOU HAVE RUN OUT YOUR GUESSES. WAIT FOR GAME RESULT.");
					break;
				}
				// how to wait for all players to finish guessing
				try {
					String guessResult = this.clientPlayer.attackShip(clientInput);
					pw.println(guessResult);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			while (true)
			{
				if (allFinished())
				{
					int winner = game.getWinner();
					if (winner == 1)
					{
						clients.get(0).pw.println("CONGRADUATIONS! YOU ARE THE WINNER!");
						clients.get(1).pw.println("SORRY! YOU LOST!");
					}
					else if (winner == 2)
					{
						clients.get(1).pw.println("CONGRADUATIONS! YOU ARE THE WINNER!");
						clients.get(0).pw.println("SORRY! YOU LOST!");
					}
					else
					{
						clients.get(0).pw.println("IT'S A DRAW!");
						clients.get(1).pw.println("IT'S A DRAW!");
					}
					clients.get(0).pw.println("gameover");
					clients.get(1).pw.println("gameover");
					break;
				}
			}
			clientSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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