import java.util.ArrayList;
import java.util.Random;
public class Dragon extends Thread
{

	public static long time = System.currentTimeMillis();
	private MainThread mainThread;
	private int numGames;
	private int numTables;
	public void msg(String m)
	{
	System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+":"+m);
	}
	
	public Dragon(MainThread parentThread, int num_games, int num_tables)
	{
		numGames=num_games;
		numTables=num_tables;
		mainThread=parentThread;
		setName("Dragon");
	}
	public void run()
	{
		msg("has been made");
		// need to change the while loop so that it will terminate once the other threads are done, 
		while(mainThread.checkForLivingAdventurers())
		{	
			if(mainThread.startGame())
			{
				msg("has started a round of gaming with players");
				Boolean playerWon=false;
				ArrayList<Adventurer> players= mainThread.getPlayers();
				for(int i=0; i<numGames;i++)
				{
					for(int j=0;j<players.size();j++)
					{
						playerWon=playGame();
						if(playerWon)
						{
							players.get(j).msg("has beat the dragon durring round"+ (j+1));
							players.get(j).hasWon();
						}
					}
				}
				msg("has finished a round of gaming with players");
				mainThread.emptyTable();
			}	
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
			if(dragonDiceRoll<humanDiceRoll)
			{
				game=false;
				output=true;
			}
		}
		return output;
	}
}
