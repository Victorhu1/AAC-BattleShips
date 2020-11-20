
public class Game {

	private static player playerA;
	private static player playerB;
	public static map mapA;
	public static map mapB;
	
	public Game(player A, player B)
	{
		this.playerA = A;
		this.playerB = B;
		mapA = new map(10, 10);
		mapB = new map(10, 10);
		mapA.randomPlacement(8);
		mapB.randomPlacement(8);
		playerA.setPlayerMap(mapA);
		playerB.setPlayerMap(mapB);
		playerA.setEnemyMap(mapB);
		playerB.setEnemyMap(mapA);
	}
	// for testing only
	public void printMaps()
	{
		mapA.printMap();
		System.out.println();
		mapB.printMap();
	}
	
	public player getPlayerA()
	{
		return this.playerA;
	}
	
	public player getPlayerB()
	{
		return this.playerB;
	}
	
	public int getWinner()
	{
		// player A wins
		if (playerA.getPlayerMap().countShips() >
			playerB.getPlayerMap().countShips())
			return 1;
		// player B wins
		else if (playerA.getPlayerMap().countShips() <
				playerB.getPlayerMap().countShips())
			return 2;
		// draw
		else 
			return 3;
	}
}
