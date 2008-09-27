package WorkersSystem.Test;

import java.util.concurrent.BlockingQueue;

import WorkersSystem.WorkER.AWorker;


public class printerWorker extends AWorker<TestResult, TestResult>{

	public printerWorker(BlockingQueue<TestResult> qr,BlockingQueue<TestResult> qt){
		super(qr, qt);
	}
	@Override
	public TestResult doItem(TestResult task) {
		System.out.println("----> " +task.result);
		return null;
	}

}
