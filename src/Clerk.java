public class Clerk extends Thread
{
	public static long time = System.currentTimeMillis();
	private MainThread mainThread;
	
	public void msg(String m)
	{
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+":"+m);
	}
	
	public Clerk(int id, MainThread parentThread) throws Exception
	{
		setName("Clerk-"+(id+1));
		mainThread=parentThread;
	}
	
	public void run()
	{
		msg("has been made");
		// fix the while loop to terminate when the other threads are done, or maybe it doesn't end. look into this 
		mainThread.firstClerks(this);
		while(mainThread.checkForLivingAdventurers())
		{   
			if(mainThread.clerkQuitCheck())waitForCustomer();
			if(mainThread.clerkQuitCheck())helpCustomers();
		}
		
		msg("has terminated because there are no more adventurers"+"\n");
	}
		
	// this method will need to use semaphores. 
	private void helpCustomers()
	{
		msg("has helped a customer");
		//mainThread.clerkSemaphore.release();
	}
	public void waitForCustomer()
	{
		try {mainThread.clerkSemaphore.acquire();}
		catch (InterruptedException e) {e.printStackTrace();}
	}
}