import java.util.Random;
public class Dragon extends Thread
{

	public static long time = System.currentTimeMillis();
	private MainThread mainThread;
	private int numGames;
	private Boolean[] winners;
	
	public void msg(String m)
	{
	System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+":"+m);
	}
	
	public Dragon(MainThread parentThread, int num_tables, int num_games)
	{
		winners = new Boolean[num_tables];
		numGames=num_games;
		mainThread=parentThread;
		setName("Dragon");
		for(int i =0;i<num_tables;i++){winners[i]=false;}
	}
	public void run()
	{
		msg("has been made");
		// need to change the while loop so that it will terminate once the other threads are done, 
		while(mainThread.checkForLivingAdventurers())
		{	
			if(mainThread.playGameCheck())
			{
				int players=mainThread.getHowManyPLayers();
				msg("is now playing a game");
				playGames(players);
				giveTreasureToWinners(players);
				emptyTable(players);
			}
			
		}
		msg(" has terminated because there are no more adventurers"+"\n");
	}
	private void emptyTable(int numPlayers)
	{
		mainThread.leaveTable(numPlayers);	
	}

	private void playGames(int numPlayers)
	{
		for(int i=0;i<numGames;i++)
		{
			for(int j=0; j<numPlayers;j++)
			{
				if(playGame())
				{
					winners[j]=true;
				}
			}
		}
		
	}

	private void giveTreasureToWinners(int numPlayers)
	{
		for(int i=0; i<numPlayers;i++)
		{
			if(winners[i])
			{
				mainThread.getAdvFromTable(i).giveTreasure(mainThread.getAdvFromTable(i));
				mainThread.getAdvFromTable(i).msg("has won and got treasure");
				winners[i]=false;
			}
		}
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