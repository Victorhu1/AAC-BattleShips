import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable {

	private Socket server;
	private BufferedReader br;
	InputStream is;
	ObjectInputStream ois;
	map A;
	map B;

	Game gam;

	GUI gui = new GUI(1);
	GUI engui = new GUI(2);

	public ServerThread(Socket s) {
		server = s;
		try {
			is = server.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			ois = new ObjectInputStream(is);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				gui.repaint();
				engui.repaint();
				String serverResponse = br.readLine();
				if (serverResponse == null) {
					try {
						synchronized (this) {
							this.wait();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (serverResponse.equals("mapsend")) {
					A = null;
					B = null;
					A = (map) ois.readObject();
					B = (map) ois.readObject();
					gui.setmap(A, B);
					engui.setmap(B, A);
					System.out.println("Client Map");
					System.out.println(A.printMap2());
					System.out.println("Enemy Map");
					System.out.println(B.printMap2());

					System.out.println("HELO");

				} else if (serverResponse.equals("gameover")) {
					synchronized (this) {
						this.notifyAll();
					}
					break;
				} else {
					System.out.println("From SERVER: " + serverResponse);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				server.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}