package WorkersSystem.WorkER;

import java.util.LinkedList;

import Common.Item;

public class WorkerSystem {

	LinkedList<WorkerCollection> Workers;
	boolean started=false;
	public WorkerSystem() {
		Workers=new LinkedList<WorkerCollection>();
	}
	
	public synchronized void add(AWorker<? extends Item,? extends Item> w,int numOfWorkers) {
		WorkerCollection wc=new WorkerCollection(w,numOfWorkers);
		Workers.add(wc);
		if(started) wc.startWorking();
	}
	
	public synchronized void startWork(){
		for(WorkerCollection wc:Workers) wc.startWorking();
		started=true;
	}
	
	public synchronized void stopWork(){
		for(WorkerCollection wc:Workers) wc.stopWorking();
		started=false;
	}
}
