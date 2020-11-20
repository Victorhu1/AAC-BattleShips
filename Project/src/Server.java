import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	private static final int port = 4321;
	private static ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	// why wouldn't it stop accepting after getting 2 clients?
	private static ExecutorService es = Executors.newFixedThreadPool(2);	
	
	private static Game game;
	private static player playerA = new player();
	private static player playerB = new player();
	private static int guess = 0;
	
	
	public synchronized static boolean setID(int id) {
		guess =id;
		if (guess == id) {
			return true;
		}
		return false; 
	}
	
	public synchronized static int getID() {
		return guess;
	}
	
	public static void main(String[] args)
	{
		try 
		{
			int total = 0;
			ServerSocket ss = new ServerSocket(port);
			while (true)
			{
				System.out.println("[SERVER] waiting for player connection...");
				Socket client = ss.accept();
				total += 1;
				System.out.println("[SERVER] connected to client: " + total +  client.getRemoteSocketAddress());
				ClientThread clientHandler = new ClientThread(total, client, clients);
				clients.add(clientHandler);
				if (total == 2)
				{
					break;
				}		 
			}
			game = new Game(playerA, playerB);
			playerA.Name("PlayerA");
			playerB.Name("PlayerB");
			clients.get(0).setPlayer(playerA);
			clients.get(1).setPlayer(playerB);
			clients.get(0).setGame(game);
			clients.get(1).setGame(game);
			game.printMaps();
			for (ClientThread each : clients)
				es.execute(each);
			es.shutdown();
			while (!es.isTerminated())
			{
				Thread.yield();
			}
		} 
		catch (IOException e) 
		{
			System.out.println("IOException in server.main");
			e.printStackTrace();
		}
		// how to close ss?
	}
}
