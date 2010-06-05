import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import diSys.Client.RemoteClient;
import diSys.Executor.DummyBenchmarkClass;

public class ConClient {
	private static int numOfIterations = Integer.MAX_VALUE/400;

	public static int run()
	{
		final long startTime = System.nanoTime();
		final long endTime;
		try {
			for(int i=0; i<numOfIterations ; i++)
			{
				new DummyBenchmarkClass();
			}
		} finally {
		  endTime = System.nanoTime();
		}
		return (int) ((endTime - startTime)/100000);
		
	}
	
	public static void main(String[] args)
	{
		
	//	run();
	//	System.out.println("done benchmark :" + formatter.format(new Date()));
	//	System.exit(10);
	/*	RemoteClient<ConTask, ConResult> ConClient =  new RemoteClient<ConTask, ConResult>("MTLLPT288", 5555, 1);
		ConClient.Start();
		int numOfTasks = 3;
		int i;
		for(i=0; i<numOfTasks; i++)
		{
			ConTask ct = new ConTask(i);
			ConClient.AddTask(ct);
			System.out.println("added " + i);
		}
		
		while(!ConClient.IsIdle())
			try {
				ConResult r=ConClient.GetResult();
				//				System.out.println(i);
				System.out.println("done " + r.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		ConClient.Stop();
*/
		RemoteClient<ConTask2, ConResult2> ConClient2 =  new RemoteClient<ConTask2, ConResult2>("MTLLPT288", 5555, 1);
		ConClient2.Start();
		int numOfTasks = 2;
		//int i;S
		for(int i=0; i<numOfTasks; i++)
		{
			ConTask2 ct = new ConTask2(i,"Task ?IDDDDDDDDDDDDDDDD" + i);
			ConClient2.AddTask(ct);
			System.out.println("added " + i);
		}
		int counter=0 ;
		while(!ConClient2.IsIdle())
			try {
				ConResult2 r=ConClient2.GetResult();
				counter++;	
				//				System.out.println(i);
				System.out.println("done " + r.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		ConClient2.Stop();
		
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+counter);
	
	}
}
