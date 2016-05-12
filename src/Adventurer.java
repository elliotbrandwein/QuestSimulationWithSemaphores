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
	private MainThread mainThread;
	//constructor will set the fortuneSize,adventurerId,stones,rings,chains,earring, and the mainThread
	public Adventurer(int id, int fortuneSize, MainThread parentThread) throws Exception
	{
		stones=getRandomInt()%4;
		rings=getRandomInt()%4;
		chains=getRandomInt()%4;
		earrings=getRandomInt()%4;
		adventurerId=id+1;				    
		setName("Adventurer-"+(adventurerId));
		setfortuneSize(fortuneSize);
		mainThread= parentThread;
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
		while(mainThread.waitForClerks()){}
		msg("sees that the clerks are now waiting for customers");
		while(checkFortune())
		{	
			if(checkForCraftableItems())
			{
				goToShop();
				shop();
			}
			
			goToDragonsCave();
		}
		
		// the if part of the run will make the thread block if there are still adventurers left. The else will call the endQuest() method and terminate all the running threads. 
		if(mainThread.checkForAdvQuitters())
		{
			System.out.println("");
			msg("is done, but is now waiting for the other threads to end"+"\n");
			mainThread.setAdvQuit();
			//enterQuittingSemaphore();
			enterQuittingSemaphore();
			releaseNextGuy();
			msg("is done "+"\n");
		}
		else{endQuest();}
		
	}
	
	private void releaseNextGuy()
	{
		mainThread.quittingSemaphore.release();
	}
	private void enterQuittingSemaphore()
	{
		try {mainThread.quittingSemaphore.acquire();} 
		catch (InterruptedException e) {e.printStackTrace();}
	}

	private void goToDragonsCave()
	{
		msg("has gone to the dragon to try to join a table ");
		mainThread.joinTable(this);
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

	private void shop() 
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
	
	public void giveTreasure(Adventurer adventurer)
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
	public void endQuest()
	{
		System.out.println("");
		msg("has terminated and will now be releasing the rest of the threads in the order that they finished"+"\n");
		releaseNextGuy();
		mainThread.clerksShouldQuit();
		mainThread.clerkSemaphore.release(mainThread.getNum_clerk());
	}
}