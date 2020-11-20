import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	private static final String ip = "localhost";
    private static final int port = 4321;
    
    
    
    public static void main(String[] args)
    {
    	try 
    	{
			Socket socket = new Socket(ip, port);
			ServerThread serverHandler = new ServerThread(socket);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			
			new Thread(serverHandler).start();
			
			int numGuess = 0;
			while (true)
			{
				
				String guess = br.readLine();
				pw.println(guess);	
				numGuess += 1;
				if (numGuess == 5) 
				{
					pw.println("done");
				}
			}
			/*
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			*/
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