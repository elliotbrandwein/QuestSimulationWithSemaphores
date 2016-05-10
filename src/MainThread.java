import java.util.concurrent.Semaphore;

public class MainThread extends Thread{
	private Clerk[] clerks;
	private Adventurer[] adventurers;
	private int adventurersThatQuit;
	private Dragon dragon;
	private int num_adv=0;
	private int num_clerk=0;
	private int num_fortuneSize=0;
	private int num_table=0;
	private int num_games=3;
	private int firstClerksCount=0;
	public  Semaphore dragonSemaphore;
	public  Semaphore shopperSemaphore;
	public  Semaphore clerkSemaphore;
	public  Semaphore quittingSemaphore;
	private Semaphore quitCounterSemaphore;
	private Semaphore firstClerksSemaphore;
	private Semaphore clerksQuittingSemaphore;
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
		dragonSemaphore= new Semaphore(num_table,true);
		quittingSemaphore= new Semaphore(0,true);
		quitCounterSemaphore = new Semaphore(1,true);
		
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
		
		dragon=new Dragon(this);
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
		return adventurersThatQuit;
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
		try {clerksQuittingSemaphore.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		returnValue = clerksShouldQuit;
		clerksQuittingSemaphore.release();
		return returnValue;
	}
	public void clerksShouldQuit()
	{
		try {clerksQuittingSemaphore.acquire();} 
    	catch (InterruptedException e) {e.printStackTrace();}
		clerksShouldQuit=false;
		clerksQuittingSemaphore.release();
	}

	
}
