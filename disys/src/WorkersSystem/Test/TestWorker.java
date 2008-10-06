package WorkersSystem.Test;
import java.util.concurrent.BlockingQueue;

import WorkersSystem.WorkER.AWorker;
public class TestWorker extends AWorker<TestTask,TestResult> {

	public TestWorker(BlockingQueue<TestTask> qt,BlockingQueue<TestResult> qr){
		super(qt, qr);
	}
	@Override
	public TestResult doItem(TestTask task) {
		TestResult r=new TestResult(task.getId());
		System.out.println("$$$$ "+ task.y+"+"+task.x);
		r.result=task.y+task.x;
		return r;
	}

	
}
