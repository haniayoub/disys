package WorkersSystem.Test;

import java.util.concurrent.LinkedBlockingQueue;

import WorkersSystem.WorkER.WorkerSystem;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException{
		LinkedBlockingQueue<TestTask> tasks=new LinkedBlockingQueue<TestTask>();
		LinkedBlockingQueue<TestResult> results=new LinkedBlockingQueue<TestResult>();
		for (int i=0;i<1000;i++){
		TestTask t=new TestTask();
		t.x=i;
		t.y=i;
		tasks.add(t);
		}
		TestWorker tw=new TestWorker(tasks,results);
		printerWorker pw=new printerWorker(results,null);
		WorkerSystem ws=new WorkerSystem();
		ws.add(tw,2);
		ws.startWork();
		ws.add(pw,3);
		Thread.sleep(2000);
		ws.stopWork();
		}
}
