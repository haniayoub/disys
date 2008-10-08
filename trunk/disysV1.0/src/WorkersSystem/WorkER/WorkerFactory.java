package WorkersSystem.WorkER;

import Common.Item;



public class WorkerFactory {
	AWorker<? extends Item,? extends Item> MotherWorker;
	public WorkerFactory(AWorker<? extends Item,? extends Item> w){
		MotherWorker=w;
	}
	public AWorker<? extends Item,? extends Item> newWorker() throws CloneNotSupportedException {
		return MotherWorker.clone();
	}
}
