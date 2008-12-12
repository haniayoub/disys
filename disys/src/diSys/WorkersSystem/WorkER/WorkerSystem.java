package diSys.WorkersSystem.WorkER;

import java.util.LinkedList;

import diSys.Common.Item;


public class WorkerSystem {

	LinkedList<WorkerCollection> Workers;
	boolean started = false;

	public WorkerSystem() {
		Workers = new LinkedList<WorkerCollection>();
	}

	public synchronized void add(AWorker<? extends Item, ? extends Item> w,
			int numOfWorkers) {
		add(new WorkerCollection(w, numOfWorkers));
	}

	public synchronized void startWork() {
		for (WorkerCollection wc : Workers)
			wc.startWorking();
		started = true;
	}

	public synchronized void stopWork() {
		for (WorkerCollection wc : Workers)
			wc.stopWorking();
		started = false;
	}

	public void add(WorkerCollection workerCollection) {
		Workers.add(workerCollection);
		if (started)
			workerCollection.startWorking();
	}
}
