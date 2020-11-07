import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class player extends Thread{
	private String name;
	private map playerMap;
	private map enemyMap;
	public boolean playerTurn;
	private player enemy; ///
	transient int roundsLeft;
	
	public void Name(String n)
	{
		this.name = n;
	}
	
	public void setPlayerMap(map m)
	{
		this.playerMap = m;
	}
	
	public void setEnemy(player e) ///
	{
		this.enemy = e;
	}
	
	public void setEnemyMap(map m)
	{
		this.enemyMap = m;
	}
	
	/*
	public void run()
	{
		placeShip();
		attackShip();
	}
	*/
	public map getPlayerMap()
	{
		return this.playerMap;
	}
	
	public void placeShip()
	{
		try
		{
			this.playerMap.semaphore.acquire();
			while (!playerTurn) {} ///
			System.out.println(name + " Please place a ship down");
			boolean validInput = false;
			while (validInput == false)
			{
				try
				{
					Scanner scan = new Scanner(System.in);
					char rawVer = scan.next().charAt(0);
					if (rawVer >= this.playerMap.getHeight() + 97) 
					{
						throw new IndexOutOfBoundsException();
					}
					int hor = scan.nextInt();
					if (hor >= this.playerMap.getWidth()) 
					{
						throw new IndexOutOfBoundsException();
					}
					int ver = rawVer - 97;
					if (!playerMap.isOccupied(ver, hor))
					{
						playerMap.occupy(ver, hor);
						System.out.println("Successful placement!");
					}
					else throw new IndexOutOfBoundsException();		
					playerTurn = false; ///
					enemy.playerTurn = true; ///
					validInput = true;
				}
				catch(IndexOutOfBoundsException e)
				{
					System.out.println("input out of bound or position already occupied");
				}
				catch(InputMismatchException e1)
				{
					System.out.println("input mismatch");
				}
			}
		} 
		catch (InterruptedException e2) 
		{
			System.out.println("interruption during placing ship");
		}
		finally
		{
			this.playerMap.semaphore.release();
		}
	}
	
	public void attackShip()
	{
		try
		{
			this.playerMap.semaphore.acquire();
			while (!playerTurn) {} ///
			System.out.println(name + " Please try to attack");
			boolean validInput = false;
			while (validInput == false)
			{
				try
				{
					Scanner scan = new Scanner(System.in);
					char rawVer = scan.next().charAt(0);
					if (rawVer >= this.enemyMap.getHeight() + 97) 
					{
						throw new IndexOutOfBoundsException();
					}
					int hor = scan.nextInt();
					if (hor >= enemyMap.getWidth()) 
					{
						throw new IndexOutOfBoundsException();
					}
					int ver = rawVer - 97;
					if (enemyMap.isOccupied(ver, hor))
					{
						enemyMap.remove(ver, hor);
						System.out.println("Successful attack!");
					}
					else System.out.println("attack unsuccessful!");	
					playerTurn = false; ///
					enemy.playerTurn = true; ///
					validInput = true;
				}
				catch(IndexOutOfBoundsException e)
				{
					System.out.println("input out of bound or position already occupied");
				}
				catch(InputMismatchException e1)
				{
					System.out.println("input mismatch");
				}
			}
		}
		catch(InterruptedException e2) 
		{
			System.out.println("interruption during attacking ship");
		}
		finally
		{
			this.playerMap.semaphore.release();
		}		
	}
	
}
