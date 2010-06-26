/*package OldCode;
import ConTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import diSys.Common.IExecutor;
import diSys.Common.Item;
import diSys.Executor.DummyBenchmarkClass;
import diSys.Executor.PowerCalculator;
public class ConExecuter implements IExecutor<ConTask, ConResult> {

	@Override
	public ConResult run(ConTask arg0) throws Exception {
		//PowerCalculator pc = new PowerCalculator();
		System.out.println("My id is: " + arg0.getId());
		run();	
		return new ConResult(arg0.getId());
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
*/