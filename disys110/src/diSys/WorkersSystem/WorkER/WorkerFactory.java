package diSys.WorkersSystem.WorkER;

import diSys.Common.Item;

public class WorkerFactory {
	AWorker<? extends Item, ? extends Item> MotherWorker;

	public WorkerFactory(AWorker<? extends Item, ? extends Item> w) {
		MotherWorker = w;
	}

	public AWorker<? extends Item, ? extends Item> newWorker()
			throws CloneNotSupportedException {
		return MotherWorker.clone();
	}
}
