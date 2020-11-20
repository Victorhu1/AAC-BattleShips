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
				System.out.println("[SERVER] connected to client: " + client.getRemoteSocketAddress());
				ClientThread clientHandler = new ClientThread(client, clients);
				clients.add(clientHandler);
				total += 1;
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
