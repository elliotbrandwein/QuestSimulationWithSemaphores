
public class Main{
	public static void main(String[] args) throws Exception
	{
		int num_adv=0;
		int num_fortuneSize=0;
		int num_clerk=0;
		int num_table=0;
		// first we see there are no arguments and if so use the default values for our number of adventurers and fortune size
		if (args.length == 0)
		{
			
			num_adv = 8;
			num_fortuneSize = 3;
			num_clerk = 2;
			num_table = 3;
		}
		// then we check to see if we have the right amount of arguments, and if they are in the right format
		else
		{
	        //if there are the wrong number of arguments we just quit
			if(args.length != 4)System.exit(0);
			
			//if there are arguments we check them and then use them if they are ints, and if they arn't we quit
			try
			{
				num_adv = Integer.parseInt(args[0]);
				num_fortuneSize = Integer.parseInt(args[1]);
		        num_clerk = Integer.parseInt(args[2]);
		        num_table = Integer.parseInt(args[3]);
			}
			catch (Exception e)
			{
				System.out.println("the argumets weren't integers, the program will now be quitting");
				System.exit(0);
			}
			// then we make sure they are positive, and if they arn't we just quit
			if (num_adv < 1 || num_fortuneSize < 1 || num_clerk<1 || num_table<1)
			{
				System.out.println("the arguments weren't positive, the program will now be quitting");
				System.exit(0);	
			}
		}
		 MainThread questAdventure = new MainThread(num_adv,num_fortuneSize,num_clerk,num_table);
		 System.out.println("we made a MainThread object and are about to start all the threads");
		 questAdventure.start();
	}
	
}
