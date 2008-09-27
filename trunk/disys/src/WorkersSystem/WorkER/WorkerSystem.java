package WorkersSystem.WorkER;

import java.util.LinkedList;

public class WorkerSystem {

	LinkedList<WorkerCollection> Workers;
	boolean started=false;
	public WorkerSystem() {
		Workers=new LinkedList<WorkerCollection>();
	}
	
	public synchronized void add(AWorker<? extends WorkItem,? extends WorkItem> w,int numOfWorkers) {
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
