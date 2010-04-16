package diSys.Executor;

public class SystemBenchmark {
	
	private static int numOfIterations = Integer.MAX_VALUE/100;

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
		System.out.println(run());
	}
}
