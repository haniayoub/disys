import java.util.Arrays;
import diSys.Client.RemoteClient;

public class Demo {
	
	private static int numOfExecuters = 512;
	private static int subArrayLength = 512;
	private static int totalArrayLength = numOfExecuters * subArrayLength;
	
	final static int[] arr = getRandomArray(totalArrayLength);
	
	private static String SystemManagerAddress = "hayoub-mobl2";
	private static int SystemManagerPort = 5555;
	private static RemoteClient<SortTask, SortResult> SortClient =  new RemoteClient<SortTask, SortResult>(SystemManagerAddress, SystemManagerPort, 1);
	private static RemoteClient<MergeTask, MergeResult> MergeClient =  new RemoteClient<MergeTask, MergeResult>(SystemManagerAddress, SystemManagerPort, 1);
	
	private static int[] getRandomArray(int length)
	{
		int[] arr = new int[length];
		java.util.Random rand =  new java.util.Random(System.currentTimeMillis());
	    for (int i=0; i<length; i++) 
	    { 
	    	arr[i] = rand.nextInt() % length; 
	    }
	    return arr;
	}
	
	public static void main(String[] args) throws Exception
	{
		Thread sortThread = new Thread(new Runnable() {
			@Override
			public void run() {
			    for(int i=0; i<numOfExecuters; i++)
			    {
			    	System.out.println("Adding sortTask id["+i+"]...");
			    	SortClient.AddTask(new SortTask(i,Arrays.copyOfRange(arr, i*subArrayLength, (i+1)*subArrayLength)));
			    }
			}
		});
		
		Thread sort2mergeThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true)
				{
					SortResult sr1;
					SortResult sr2;
					try 
					{
						sr1 = SortClient.GetResult(); 
						sr2 = SortClient.GetResult();
					} 
					catch (Exception e) { e.printStackTrace(); continue; }
					
					if(sr1 == null || sr2 == null)
						continue;
					
					System.out.println("Adding mergeTask from sortedResult ids: ["+sr1.getId()+"]["+sr2.getId()+"]");
					MergeClient.AddTask(new MergeTask(sr1, sr2));
				}
			}
		});
		
		Thread mergeThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true)
				{
					MergeResult mr1;
					MergeResult mr2;
					try {
						mr1 = MergeClient.GetResult();
						if(mr1 == null) continue;
						if(mr1.arr.length == totalArrayLength)
						{
							for(int i=0; i<mr1.arr.length; i++)
								System.out.println("FinalArr["+i+"] = " + mr1.arr[i]);
							return;
						}
							
						mr2 = MergeClient.GetResult();
					} catch (Exception e) { e.printStackTrace(); continue; }
					
					if(mr1 == null || mr2 == null)
						continue;
					MergeClient.AddTask(new MergeTask(mr1, mr2));
				}
			}
		});
		
		SortClient.Start();
		MergeClient.Start();

		sortThread.start();
		sort2mergeThread.start();
		mergeThread.start();
		
		while(true)
		{
			Thread.sleep(1000000);
		}
		//SortClient.Stop();
		//MergeClient.Stop();
	}
}
