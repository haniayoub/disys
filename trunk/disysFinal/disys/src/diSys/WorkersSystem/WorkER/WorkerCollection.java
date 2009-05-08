package diSys.WorkersSystem.WorkER;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import diSys.Common.Item;
import diSys.Common.Logger;


//Handle a Collection of AWorkers 
public class WorkerCollection {
	//worker factory to create a worker when we need 
	private WorkerFactory workerFactory;
	//list of workers & threads
	private LinkedList<AWorker<? extends Item, ? extends Item>> workersList;
	private LinkedList<Thread> workersThreads;

	public WorkerCollection(AWorker<? extends Item, ? extends Item> w,
			int numOfWorkers) {
		this(w,numOfWorkers,null);
	}
	
	public WorkerCollection(AWorker<? extends Item, ? extends Item> w,
			int numOfWorkers,ClassLoader cl) {
		workerFactory = new WorkerFactory(w);
		workersList = new LinkedList<AWorker<? extends Item, ? extends Item>>();
		workersThreads = new LinkedList<Thread>();
		for (int i = 0; i < numOfWorkers; i++) {
			AWorker<? extends Item, ? extends Item> worker;
			try {
				worker = workerFactory.newWorker();
				worker.setId(i);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return;
			}
			workersList.add(worker);
			Thread t=new Thread(worker);
			if(cl!=null){
			t.setContextClassLoader(cl);
			Logger.TraceInformation("New thread worker with jcl"+cl.toString());
			}
			workersThreads.add(t);
		}
	}

	//start the threads
	public void startWorking() {
		for (Thread workerThread : workersThreads)
			workerThread.start();
	}

	//stop each thread and wait for it 
	public void stopWorking() {
		for (AWorker<? extends Item, ? extends Item> worker : workersList) {
			worker.halt();
		}
		for (Thread workerThread : workersThreads)
			try {
				workerThread.interrupt();
				workerThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		System.out.println("all workers stoped working");
	}

	public BlockingQueue<? extends Item> TaskQueue() {
		return workerFactory.MotherWorker.WorkItems;
	}

	public BlockingQueue<? extends Item> ResultQueue() {
		return workerFactory.MotherWorker.Results;
	}
	public  LinkedList<AWorker<? extends Item, ? extends Item>> getWorkerList(){
	return workersList;
	}
}
