import java.util.Random;
public class Dragon extends Thread
{

	public static long time = System.currentTimeMillis();
	private MainThread mainThread;
	
	public void msg(String m)
	{
	System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+":"+m);
	}
	
	public Dragon(MainThread parentThread)
	{
		mainThread=parentThread;
		setName("Dragon");
	}
	public void run()
	{
		msg("has been made");
		// need to change the while loop so that it will terminate once the other threads are done, 
		while(mainThread.checkForLivingAdventurers())
		{	
			
			
		}
		msg(" has terminated because there are no more adventurers"+"\n");
	}
	private int getRandomInt()
	{
		Random randStone = new Random();
		int value = ((randStone.nextInt()));
		return value;
	}
	private boolean playGame()
	{
		boolean game=true;
		boolean output=true;
		while(game){
			int dragonDiceRoll=(getRandomInt()%6)+1;
			int humanDiceRoll =(getRandomInt()%6)+1;
			if(dragonDiceRoll>humanDiceRoll)
			{
				game=false;
				output=false;
			}
			if(dragonDiceRoll>humanDiceRoll)
			{
				game=false;
				output=true;
			}
		}
		return output;
	}
}
