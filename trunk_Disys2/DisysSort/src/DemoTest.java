import java.util.Arrays;
import diSys.Client.RemoteClient;

public class DemoTest {

	private static int numOfExecuters = 8;
	private static int subArrayLength = 8;
	private static int totalArrayLength = numOfExecuters * subArrayLength;
	
	private static String SystemManagerAddress = "hayoub-mobl2";
	private static int SystemManagerPort = 5555;
	private static RemoteClient<SortTask, SortResult> SortClient =  new RemoteClient<SortTask, SortResult>(SystemManagerAddress, SystemManagerPort, 1);
	private static RemoteClient<MergeTask, MergeResult> MergeClient =  new RemoteClient<MergeTask, MergeResult>(SystemManagerAddress, SystemManagerPort, 1);
	
	private static MergeTask[] generateMergeTasks(MergeResult[] mergedResults)
	{
		MergeTask[] mergeTasks = new MergeTask[mergedResults.length/2];
		for(int i=0, j=0; i<mergeTasks.length; i++, j+=2)
		{
			mergeTasks[i] = new MergeTask(i, mergedResults[j].arr, mergedResults[j+1].arr);
		}
		return mergeTasks;
	}
	
	private static MergeTask[] generateMergeTasks(SortResult[] sortedResults)
	{
		MergeResult[] mergedResults = new MergeResult[sortedResults.length];
		for(int i=0; i<mergedResults.length; i++)
			mergedResults[i] = new MergeResult(sortedResults[i]);
		return generateMergeTasks(mergedResults);
	}
	
	private static SortTask[] generateSortTasks()
	{
		int[] arr = getRandomArray(totalArrayLength);  
	    SortTask[] tasks = new SortTask[numOfExecuters];
	    
	    for(int i=0; i<numOfExecuters; i++)
	    	tasks[i] = 
	    		new SortTask(i,Arrays.copyOfRange(arr, i*subArrayLength, (i+1)*subArrayLength));
	    
	    return tasks;
	}
	
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
		SortClient.Start();
		MergeClient.Start();
		
		SortTask[] sortTasks = generateSortTasks();
		
		System.out.println("Sending " + sortTasks.length + " SortTasks...");
		for(SortTask st : sortTasks)
		{
			SortClient.AddTask(st);
		}
		System.out.println("Should receive " + sortTasks.length + " SortResults...");
		
		SortResult[] sortedResults = new SortResult[sortTasks.length];
		for(int i=0; i<sortedResults.length; i++)
		{
			SortResult sr = SortClient.GetResult();
			sortedResults[(int)sr.getId()] = sr;
		}
		System.out.println("Received: " + sortedResults.length + " SortResults");
		
		MergeTask[] mergeTasks = generateMergeTasks(sortedResults);
		MergeResult[] mergedResults;
		while(true)
		{
			System.out.println("Sending " + mergeTasks.length + " MergeTasks...");
			for(int i=0; i<mergeTasks.length; i++)
			{
				MergeClient.AddTask(mergeTasks[i]);
			}
			System.out.println("Should receive " + mergeTasks.length + " MergeTasks...");
			
			mergedResults = new MergeResult[mergeTasks.length];
			for(int i=0; i<mergedResults.length; i++)
			{
				MergeResult mr = MergeClient.GetResult();
				mergedResults[i] = mr;
			}
			System.out.println("Received: " + mergedResults.length + " MergeResults");
			
			if(mergedResults.length == 1)
				break;
			else
				mergeTasks = generateMergeTasks(mergedResults);
		}
		
		System.out.println("Finished Executing...");
		System.out.println("Final Sorted Array: ");
		for(int i=0; i<mergedResults[0].arr.length; i++)
		{
			System.out.println("Arr["+i+"] = "+mergedResults[0].arr[i]);
		}

		SortClient.Stop();
		MergeClient.Stop();
	}
}
