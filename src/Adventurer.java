import java.util.ArrayList;
import java.util.Random;


public class Adventurer extends Thread
{
	// the variable list that each Adventurer needs
	public static long time = System.currentTimeMillis();
	private int fortuneSize = 0;
	private int adventurerId;
	private int stones;
	private int rings;
	private int chains;
	private int earrings;
	private int magicalRings,magicalNecklases,magicalEarrings;
	private static ArrayList<Boolean> need_assistance = new ArrayList<Boolean>();
	private MainThread mainThread;
	//constructor will set the fortuneSize,adventurerId,stones,rings,chains,earring,mainThread, and need_assitance variables
	public Adventurer(int id, int fortuneSize, MainThread parentThread) throws Exception
	{
		stones=getRandomInt()%4;
		rings=getRandomInt()%4;
		chains=getRandomInt()%4;
		earrings=getRandomInt()%4;
		adventurerId=id;				    
		need_assistance.add(false);// The assistance array initializes to false
		setName("Adventurer-"+(id+1));
		setfortuneSize(fortuneSize);
		mainThread= parentThread;
	}
	
	// This constructor is used in the mainThread so it can create an Adventurer pointer
	public Adventurer()
	{
	
	}
	
	private int getRandomInt()
	{
		Random randStone = new Random();
		int value = ((randStone.nextInt()));
		return value;
	}
	
	private void setfortuneSize(int fortune_Size) throws Exception
	{
		if(fortune_Size<1)
		{
			System.out.println("thats not a valid fortune size, the program will now throw an Exception");
			Exception e = new Exception("bad fortune size");
			throw e;
		}
		else fortuneSize=fortune_Size;
	}

	public void msg(String m)
	{
	System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+":"+m);
	}
	
	public void run()
	{
		msg("has started");
		while(checkFortune())
		{	
			if(checkForCraftableItems())
			{
				goToShop();
				//try {mainThread.customerSemaphore.acquire();}
				//catch (InterruptedException e) {e.printStackTrace();}
				shop();
			}
			// this if is so that we don't go to the dragon one last time after the adventurer makes his last piece of treasure
			//goToDragonsCave();
		}
		if(mainThread.checkForAdvQuitters()!=true)
		{
			mainThread.advQuit();
			try {mainThread.quittingSemaphore.acquire();}
			catch (InterruptedException e) {e.printStackTrace();}
		}
		mainThread.quittingSemaphore.release();
		msg("is done "+"\n");
	}
	
	private void goToDragonsCave()
	{
		msg("has gone to the dragon's cave ");
		try
		{
			sleep(100000);
		} 
		catch (InterruptedException e)
		{
			// the adventurer will only wake if they win.
			giveTreasure(this);	
		}
		
	}
	
	
	private boolean checkFortune()
	{
		if(magicalRings>=fortuneSize && magicalNecklases>=fortuneSize && magicalEarrings>=fortuneSize) return false;
		return true;
	}
	
	//this will only return true if the adventurer can craft treasure for something that he needs more of
	private boolean checkForCraftableItems()
	{
		if(stones>0 && rings>0 && magicalRings<fortuneSize) return true;	
		else if (stones>0 && chains>0 && magicalNecklases<fortuneSize) return true;		
		else if(stones>1 && earrings>1  && magicalEarrings<fortuneSize)return true;
		else return false;						
	}
	
	private void goToShop()
	{
		msg("is going to the shop");
		// this will make the thread sleep anywhere from 0 to 4 seconds
		Random randTime= new Random();
		Long sleeptime=Math.abs(randTime.nextLong());
		try
		{
			Thread.sleep(sleeptime%4000);
		} 
		catch (InterruptedException e) {
			System.out.println("the sleeptime for goToShop failed");
			e.printStackTrace();
		}	
	}

	private synchronized void shop() 
	{
		msg("has entered the shop and is about to try to aquire from the customer semaphore");
		try {mainThread.shopperSemaphore.acquire();}
		catch (InterruptedException e) {e.printStackTrace();}
		msg("has aquired from the customer semaphore, and will now release a clerk to help him");
		mainThread.clerkSemaphore.release();
		makeMagicItems();
		mainThread.shopperSemaphore.release();
		msg("has left the shop and has released the shopper semaphore");
	}
	
//	public void getAssistance()
//	{ 
//		need_assistance.set(adventurerId,false);
//	}
	
	private void makeMagicItems()
	{
		while(checkForCraftableItems())
		{
			if(stones>0 && rings>0 && magicalRings<fortuneSize)
			{
				magicalRings++;
				rings--;
				stones--;
				msg("now has "+magicalRings+" magical ring(s)");
			}
			if(stones>0 && chains>0 && magicalNecklases<fortuneSize)
			{
				stones--;
				chains--;
				magicalNecklases++;
				msg("now has "+magicalNecklases+" magical necklase(s)");
			}
		    if(earrings>1 && stones>1 && magicalEarrings<fortuneSize)
			{
				earrings=earrings-2;
				stones=stones-2;
				magicalEarrings++;
				msg("now has "+magicalEarrings+" set(s) of magical earrings");
			}
		}
	}
	
	private void giveTreasure(Adventurer adventurer)
	{
		int prize=getRandomInt()%4;
		switch(prize)
		{
		case 0: stones++;
		break;
		case 1: rings++;
		break;
		case 2: earrings++;
		break;
		case 3: chains++;
		break;
		}
		
	}
	
}