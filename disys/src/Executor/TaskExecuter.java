package Executor;

import java.util.concurrent.BlockingQueue;
import Common.IExecutor;
import Common.Item;
import Common.RemoteItem;
import WorkersSystem.WorkER.AWorker;

/**
 * Tasks Executer :polls tasks from tasks queue and put results in results queue
 * 
 * @author saeed
 * 
 * @param <TASK>
 * @param <RESULT>
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class TaskExecuter<TASK extends Item, RESULT extends Item, E extends IExecutor>
		extends AWorker<RemoteItem<TASK>, RemoteItem<RESULT>> {
	private E excutor;

	public TaskExecuter(E executor, BlockingQueue<RemoteItem<TASK>> tasks,
			BlockingQueue<RemoteItem<RESULT>> results) {
		super(tasks, results);
		this.excutor = executor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RemoteItem<RESULT> doItem(RemoteItem<TASK> task) {
		Item Result = excutor.run(task.getItem());
		return new RemoteItem(Result, task.getRemoteInfo());
	}

}
