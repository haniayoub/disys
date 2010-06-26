import diSys.Common.ATask;
import diSys.Executor.DummyBenchmarkClass;

public class ConTask extends ATask<ConResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConTask(long id) {
		super(id);
	}

	@Override
	public ConResult Run() {
		//PowerCalculator pc = new PowerCalculator();
		System.out.println("My id is: " + this.getId());
		run();	
		return new ConResult(this.getId());
	}
	private static int numOfIterations = Integer.MAX_VALUE/300;

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

}
