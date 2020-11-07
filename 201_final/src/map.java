import java.util.concurrent.Semaphore;

public class map 
{
	private String player;
	private boolean[][] grids;
	private int height;
	private int width;
	
	public transient Semaphore semaphore;
	
	// make an empty map, no ship occupying
	public map(int height, int width)
	{
		this.height = height;
		this.width = width;
		grids = new boolean[height][width];
		for (int i=0; i<height; i++)
		{
			for (int j=0; j<width; j++)
			{
				grids[i][j] = false;
			}
		}
		this.semaphore = new Semaphore(1);
	}
	
	public void printMap()
	{
		for (int i=0; i<height; i++)
		{
			for (int j=0; j<width; j++)
			{
				if (!grids[i][j]) System.out.print('0');
				else System.out.print('1'); 
				System.out.print(' ');
			}
			System.out.println();
		}
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public String getPlayer()
	{
		return player;
	}
	
	public boolean isOccupied(int height, int width)
	{
		return grids[height][width];
	}
	
	public void occupy(int height, int width)
	{
		grids[height][width] = true;
	}
	
	public void remove(int height, int width)
	{
		grids[height][width] = false;
	}
	
	public int countShips()
	{
		int total = 0;
		for (int i=0; i<height; i++)
		{
			for (int j=0; j<width; j++)
			{
				if (grids[i][j] == true) 
					total += 1;
			}
		}
		return total;
	}
}




