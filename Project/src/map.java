import java.util.Random;
import java.util.concurrent.Semaphore;

public class map 
{
	private String player;
	private int[][] grids;
	private int height;
	private int width;
	
	public transient Semaphore semaphore;
	
	// int array : destroyed, empty, alive boat
	// make an empty map, no ship occupying
	public map(int height, int width)
	{
		this.height = height; 
		this.width = width;
		grids = new int[height][width];
		for (int i=0; i<height; i++)
		{
			for (int j=0; j<width; j++)
			{
				grids[i][j] = 0;
			} 
		}
		this.semaphore = new Semaphore(1);
	}
	
	// randomly place given number of ships on map
	public void randomPlacement(int num)
	{
		int placed = 0;
		Random rand = new Random(); 
		while (placed < num)
		{
			int rand_ver = rand.nextInt(this.height); 
	        int rand_hor = rand.nextInt(this.width); 
	        if (!isOccupied(rand_ver, rand_hor))
	        {
	        	occupy(rand_ver, rand_hor);
	        	placed += 1;
	        }
		}
	}
	
	public void printMap()
	{
		for (int i=0; i<height; i++)
		{
			for (int j=0; j<width; j++)
			{
				System.out.print(grids[i][j]);
				System.out.print(' ');
			}
			System.out.println();
		}
	}
	
	public String printMap2()
	{
		String s="";
		for (int i=0; i<height; i++)
		{
			for (int j=0; j<width; j++)
			{
				s= s+(grids[i][j]);
				s=s+(' ');
			}
			s= s+ '\n';
		}
		return s;
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
		return (grids[height][width] == 1);
	}
	
	public void occupy(int height, int width)
	{
		grids[height][width] = 1;
	}
	
	public void remove(int height, int width)
	{
		grids[height][width] = 2;
	}
	
	public int countShips()
	{
		int total = 0;
		for (int i=0; i<height; i++)
		{
			for (int j=0; j<width; j++)
			{
				if (grids[i][j] == 1) 
					total += 1;
			}
		}
		return total;
	}
}




