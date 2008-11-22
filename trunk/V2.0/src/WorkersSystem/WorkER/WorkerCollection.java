package WorkersSystem.WorkER;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import Common.Item;

//Handle a Collection of AWorkers 
public class WorkerCollection {
	//worker factory to create a worker when we need 
	private WorkerFactory workerFactory;
	//list of workers & threads
	private LinkedList<AWorker<? extends Item, ? extends Item>> workersList;
	private LinkedList<Thread> workersThreads;

	@SuppressWarnings("unchecked")
	public WorkerCollection(AWorker<? extends Item, ? extends Item> w,
			int numOfWorkers) {
		workerFactory = new WorkerFactory(w);
		workersList = new LinkedList<AWorker<? extends Item, ? extends Item>>();
		workersThreads = new LinkedList<Thread>();
		for (int i = 0; i < numOfWorkers; i++) {
			AWorker<? extends Item, ? extends Item> worker;
			try {
				worker = workerFactory.newWorker();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return;
			}
			workersList.add(worker);
			workersThreads.add(new Thread(worker));
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

}