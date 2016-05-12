import java.util.Random;
public class Dragon extends Thread
{

	public static long time = System.currentTimeMillis();
	private MainThread mainThread;
	private int numGames;
	private int numTables;
	private Boolean[] winners;
	
	public void msg(String m)
	{
	System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+":"+m);
	}
	
	public Dragon(MainThread parentThread, int num_tables, int num_games)
	{
		numTables=num_tables;
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
				msg("is now playing a game");
				playGames();
				giveTreasureToWinners();
				emptyTable();
			}
			
		}
		msg(" has terminated because there are no more adventurers"+"\n");
	}
	private void emptyTable()
	{
		mainThread.leaveTable();	
	}

	private void playGames() {
		for(int i=0;i<numGames;i++)
		{
			for(int j=0; j<numTables;j++)
			{
				if(playGame())
				{
					winners[j]=true;
				}
			}
		}
		
	}

	private void giveTreasureToWinners()
	{
		for(int i=0; i<numTables;i++)
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