package WorkersSystem.WorkER;



public class WorkerFactory {
	AWorker<? extends WorkItem,? extends WorkItem> MotherWorker;
	public WorkerFactory(AWorker<? extends WorkItem,? extends WorkItem> w){
		MotherWorker=w;
	}
	public AWorker<? extends WorkItem,? extends WorkItem> newWorker() throws CloneNotSupportedException {
		return MotherWorker.clone();
	}
}
