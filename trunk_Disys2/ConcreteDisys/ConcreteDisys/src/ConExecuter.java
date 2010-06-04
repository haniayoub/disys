import diSys.Common.IExecutor;
import diSys.Executor.PowerCalculator;
public class ConExecuter implements IExecutor<ConTask, ConResult> {

	@Override
	public ConResult run(ConTask arg0) throws Exception {
		PowerCalculator pc = new PowerCalculator();
		System.out.println("My EP is: " + pc.getEP());
		return new ConResult(arg0.getId());
	}

}
