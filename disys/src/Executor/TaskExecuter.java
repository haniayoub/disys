package Executor;

import java.util.concurrent.BlockingQueue;
import Common.IExecutor;
import Common.Item;
import Common.RemoteItem;
import WorkersSystem.WorkER.AWorker;

@SuppressWarnings("unchecked")
public class TaskExecuter<E extends IExecutor> extends
		AWorker<RemoteItem<? extends Item>, RemoteItem<? extends Item>> {
	private E excutor;
	public TaskExecuter(E executor,BlockingQueue<RemoteItem<? extends Item>> tasks,BlockingQueue<RemoteItem<? extends Item>> results) {
		// TODO Auto-generated constructor stub
		super(tasks,results);
		this.excutor=executor;
	}
	@SuppressWarnings("unchecked")
	@Override
	public RemoteItem<? extends Item> doItem(RemoteItem<? extends Item> task)
	{
		Item Result=excutor.run(task.getItem());
		return new RemoteItem(Result,task.getRemoteInfo());
	}

}
