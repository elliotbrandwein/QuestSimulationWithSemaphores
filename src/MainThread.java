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
	public final Semaphore dragonSemaphore;
	public final Semaphore shopperSemaphore;
	public final Semaphore clerkSemaphore;
	public Semaphore[] quittingSemaphore;
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
		shopperSemaphore= new Semaphore(num_clerk,true);
		clerkSemaphore= new Semaphore(0,true);	
		dragonSemaphore= new Semaphore(num_table,true);
		quittingSemaphore= new Semaphore[num_adv];
		
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
			quittingSemaphore[i]= new Semaphore(0);
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
	public int getAdvQuit(){return adventurersThatQuit;}
    public void setAdvQuit()
    {
    	adventurersThatQuit++;
    }
	public boolean checkForAdvQuitters()
	{
		if (adventurersThatQuit==num_adv-1)return false;
		return true;
	}
	public boolean waitForClerks()
	{
		// im not sure why but this print line has to be here or the threads get stuck in a while loop. 
		//I was told this may be caused by the fact that the thread thats in the loop doesn't know this method can get it out so the compiler makes it get stuck
		System.out.print("");
		if(firstClerksCount==num_clerk)return false;
		return true;
	}

	public void firstClerks(Clerk clerk)
	{
		firstClerksCount++;
		clerk.waitForCustomer();
	}

	public int getNum_adv()
	{
		return num_adv;
	}

	public int getNum_clerk() 
	{
		return num_clerk;
	}
	public boolean clerkQuitCheck(){
		return clerksShouldQuit;
	}
	public void clerksShouldQuit() {
		clerksShouldQuit=false;
		
	}

	
}
