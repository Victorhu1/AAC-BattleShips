import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestGame 
{
	public static player playerA;
	public static player playerB;
	public static map mapA;
	public static map mapB;
	public static int roundsLeft;
	
	public static void main(String[] args) 
	{
		playerA = new player();
		playerB = new player();
		mapA = new map(5, 5);
		mapB = new map(5, 5);
		playerA.setPlayerMap(mapA);
		playerB.setPlayerMap(mapB);
		playerA.setEnemyMap(mapB); 
		playerB.setEnemyMap(mapA);
		
		playerA.setEnemy(playerB); ///
		playerB.setEnemy(playerA); ///
		playerA.Name("playerA");
		playerB.Name("playerB");
		
		roundsLeft = 3;
		
		playerA.playerTurn = true;
		mapA.randomPlacement(8);
		mapB.randomPlacement(8);
		mapA.printMap();
		System.out.println();
		mapB.printMap();
		/*
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(playerA);
		executor.execute(playerB);
		*/
		
		/*
		while (roundsLeft > 0)
		{
			playerA.placeShip();
			mapA.printMap();
			playerB.playerTurn = true;
			
			playerB.placeShip();
			mapB.printMap();
			playerA.playerTurn = true;
			
			playerA.attackShip();
			playerB.playerTurn = true;
			
			playerB.attackShip();
			playerA.playerTurn = true;
			
			roundsLeft -= 1;
		}	
		
		if (getWinner() == 1)
			System.out.println("playerA is the winner");
		else if (getWinner() == 2)
			System.out.println("playerB is the winner");
		else
			System.out.println("it's a draw");
		*/
	}
	
	public static int getWinner()
	{
		if (playerA.getPlayerMap().countShips() >
			playerB.getPlayerMap().countShips())
			return 1;
		else if (playerA.getPlayerMap().countShips() <
				playerB.getPlayerMap().countShips())
			return 2;
		else 
			return 3;
	}

	
}
