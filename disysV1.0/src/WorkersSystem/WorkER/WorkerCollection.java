package WorkersSystem.WorkER;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import Common.Item;



public class WorkerCollection {
	WorkerFactory workerFactory;
	LinkedList<AWorker<? extends Item,? extends Item>> WorkersList;
	LinkedList<Thread> Threads;
	@SuppressWarnings("unchecked")
	public WorkerCollection(AWorker<? extends Item,? extends Item> w,int numOfWorkers){
	workerFactory=new WorkerFactory(w);
	WorkersList=new LinkedList<AWorker<? extends Item,? extends Item>>();
	Threads=new LinkedList<Thread>();
	for(int i=0;i<numOfWorkers;i++){
		AWorker<? extends Item, ? extends Item> worker;
		try {
			worker = workerFactory.newWorker();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return;
		}
		WorkersList.add(worker);
		Threads.add(new Thread(worker));
	}
	}
	public void startWorking(){
		for(Thread workerThread:Threads) workerThread.start();
	}
	
	
	public void stopWorking(){
		for(AWorker<? extends Item,? extends Item> worker:WorkersList) {
			worker.halt();
		}
		for(Thread workerThread:Threads)
			try {
				workerThread.interrupt();
				workerThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		System.out.println("all workers stoped working");
	}
	
	public BlockingQueue<? extends Item> TaskQueue(){
		return workerFactory.MotherWorker.WorkItems;
	}
	
	public BlockingQueue<? extends Item> ResultQueue(){
		return workerFactory.MotherWorker.Results;
	}

}
