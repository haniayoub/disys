package WorkersSystem.WorkER;

import java.util.concurrent.BlockingQueue;

import Common.Item;

public abstract class AWorker<T extends Item, R extends Item> implements
		Runnable, Cloneable {
	//The Queue to poll items from an perform the doItem method on them
	public BlockingQueue<T> WorkItems;
	//the Queue to put results into
	public BlockingQueue<R> Results;
	private boolean stop = false;

	public AWorker(BlockingQueue<T> wi, BlockingQueue<R> rq) {
		WorkItems = wi;
		Results = rq;
	}

	//poll items from WorkItemsQueue and put result in results Queue if any
	public void RunWorker() {
		while (!stop) {
			R res;
			T task;
			try {
				task = WorkItems.take();
			} catch (InterruptedException e) {
				continue;
			}
			if (task == null)
				continue;

			res = doItem(task);
			if (res == null || Results == null)
				continue;
			while (true) {
				try {
					Results.put(res);
					break;
				} catch (InterruptedException e) {
					continue;
				}
			}
		}
	}

	public void run() {
		RunWorker();
	}

	public void halt() {
		stop = true;
	}

	//clone more workers to do the same work on workItems Queue
	@SuppressWarnings("unchecked")
	public AWorker<T, R> clone() throws CloneNotSupportedException {
		return (AWorker<T, R>) super.clone();
	}

	public abstract R doItem(T task);

}
