package Final_Project;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Menu {
	public static void main(String[] args) {
		final Scanner scanner = new Scanner(System.in);
		final Lock orderLock = new ReentrantLock();
		Condition c = orderLock.newCondition();
		System.out.println("Start Game!");
		
		while (true) {
			System.out.println("Enter o to play online, else play against BOT");
			String x = scanner.nextLine();
			if (x=="o") {
				//wait 30 seconds for new player 
				try {
					if (c.await(30, TimeUnit.SECONDS)) {
						System.out.println("Other player found! Lobby is being created");
						break;
					}
					else {
						//if no players online after 30 seconds return back to main page at the top. 
						System.out.println("No players rn");
					}
				}
				catch (InterruptedException e) {}
			}
			else {
				//Start bot game
				System.out.println("Starting Bot game!");
				break;
			}
		}
	}
}
