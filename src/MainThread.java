import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class MainThread extends Thread{
	private Clerk[] clerks;
	private Adventurer[] adventurers;
	private ArrayList<Adventurer> dragonTable = new ArrayList<Adventurer>();
	private int adventurersThatQuit;
	private Dragon dragon;
	private int num_adv=0;
	private int num_clerk=0;
	private int num_fortuneSize=0;
	private int num_table=0;
	private int num_games=3;
	private int firstClerksCount=0;
	public  Semaphore dragonTableSemaphore;
	public  Semaphore shopperSemaphore;
	public  Semaphore clerkSemaphore;
	public  Semaphore quittingSemaphore;
	private Semaphore quitCounterSemaphore;
	private Semaphore firstClerksSemaphore;
	private Semaphore clerksQuittingSemaphore;
	private Semaphore useDragonTable;
	public Semaphore leaveGameSemaphore;
	private boolean clerksShouldQuit=true;
	
	
	// the run method calls the start method on all the other threads the main thread has created
	public void run()
	{
		System.out.println("we are in the MainThread run");
		initThreads();
		System.out.println("and the mainThread is done");
		return;
	}
	
	// this is the constructor for the Main class
	public MainThread(int num_advs, int fortune_size, int num_clerks, int num_tables) throws Exception
	{ 
		//  first we get the input that was passed to the constructor
		num_adv=num_advs;
		num_fortuneSize=fortune_size;
		num_clerk=num_clerks;
		num_table=num_tables;
		
		// next we make the semaphores
		// the amount of games played per round was not asked to be an input so i initialized it to 3.
		clerksQuittingSemaphore = new Semaphore(1,true);
		firstClerksSemaphore = new Semaphore(1,true);
		shopperSemaphore= new Semaphore(num_clerk,true);
		clerkSemaphore= new Semaphore(0,true);	
		dragonTableSemaphore= new Semaphore(num_table,true);
		quittingSemaphore= new Semaphore(0,true);
		quitCounterSemaphore = new Semaphore(1,true);
		useDragonTable = new Semaphore(1,true);
		leaveGameSemaphore = new Semaphore(0,true);
		
		// next we make the arrays where we will store the clerk and adventurer threads, as well as other shared variables
		clerks= new Clerk[num_clerk];
		adventurers = new Adventurer[num_adv];
		 
		/* now we create all the clerks,adventurers and the dragon
		 * the shared variables are stored in this class, so we pass a pointer to this class to all the threads we make 
		 * we start the clerks before the adventurers so that they will block before the adventurers have a chance to release them 
		 */
		for(int i =0; i<num_clerk;i++)
		{
			clerks[i]= new Clerk(i,this);
		}
		
		for(int i =0; i<num_adv;i++)
		{
			adventurers[i]= new Adventurer(i,num_fortuneSize,this);
		}
		
		dragon=new Dragon(this,num_games,num_tables);
	}
	public void initThreads()
	{	
		for(int i=0; i<num_adv;i++)
		{ 
			adventurers[i].start();
		}
		for(int i=0;i<num_clerk;i++)
		{
			clerks[i].start();
		}
		dragon.start();
	}
	public boolean checkForLivingAdventurers()
	{
		int deadCount=0;
		for(int i =0; i<num_adv;i++)
		{
			if(adventurers[i].isAlive()!=true)deadCount++;
		}
		if(deadCount==num_adv)return false;
		return true;
	}
	public int getAdvQuit()
	{
		int returnValue;
		try {quitCounterSemaphore.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		returnValue= adventurersThatQuit;
		quitCounterSemaphore.release();
		return returnValue;
	}
	
    public void setAdvQuit()
    {
    	try {quitCounterSemaphore.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
    	adventurersThatQuit++;
    	quitCounterSemaphore.release();
    	
    }
	public boolean checkForAdvQuitters()
	{
		Boolean returnValue=true;
		try {quitCounterSemaphore.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		if (adventurersThatQuit==num_adv-1)returnValue=false;
		quitCounterSemaphore.release();
		return returnValue;
	}
	public boolean waitForClerks()
	{
		Boolean returnValue =true;
		try {firstClerksSemaphore.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		if(firstClerksCount==num_clerk)returnValue= false;
		firstClerksSemaphore.release();
		return returnValue;
	}

	public void firstClerks(Clerk clerk)
	{
		try {firstClerksSemaphore.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		firstClerksCount++;
		firstClerksSemaphore.release();
	}

	public int getNum_adv()
	{
	
		return num_adv;
	}

	public int getNum_clerk() 
	{
		return num_clerk;
	}
	public boolean clerkQuitCheck()
	{
		Boolean returnValue =true;
		try {quitCounterSemaphore.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		returnValue = clerksShouldQuit;
		clerksQuittingSemaphore.release();
		return returnValue;
	}
	public void joinTable(Adventurer adv)
	{
		try {dragonTableSemaphore.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		try {useDragonTable.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		dragonTable.add(adv);
		adv.msg("has joined table "+dragonTable.size());
		useDragonTable.release();
	}
	public boolean startGame()
	{
		Boolean returnValue=false;
		try {useDragonTable.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		int advLeft=num_adv-adventurersThatQuit;
		if(dragonTable.size()==num_table)returnValue=true;
		if (advLeft<3){
			System.out.println("we know there are only 2 left");
			if(dragonTable.size()==advLeft) returnValue=true;
		}
		useDragonTable.release();
		return returnValue;
	}
	public ArrayList<Adventurer> getPlayers()
	{
		try {useDragonTable.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		ArrayList<Adventurer> DragonTable = dragonTable;
		useDragonTable.release();
		return DragonTable;
	}
	public void emptyTable()
	{
		try {useDragonTable.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		for(int i=dragonTable.size(); i>0;i--)
		{
			dragonTable.remove(i-1);
			leaveGameSemaphore.release();
		}
		dragonTableSemaphore.release(num_table);
		useDragonTable.release();
	}
	public void clerksShouldQuit()
	{
		try {clerksQuittingSemaphore.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		clerksShouldQuit=false;
		clerksQuittingSemaphore.release();
	}

	
}
