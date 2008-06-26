package RemoteOperation;

import Common.IResult;
import Common.ITask;

public interface IRemoteOperation {
	/**
	 * starts the service 
	 */
	void 	Start();
	/**
	 * Wait until all waiting Tasks Are executed , and all results are ready 
	 */
	void 	Wait();
	/**
	 *Stop executing all waiting tasks and , and wait until all Results are ready . 
	 */
	void 	Stop();
	/**
	 * add new Task to be executed , nonBlocking call
	 * @param task - the task to be executed
	 * @param priority - of task the higher the better 
	 */
	void	DoTask(ITask task,int priority);
	/**
	 * Get the First most recent result.
	 * @return IResult
	 */
	IResult PopResult();
	/**
	 * execute task , Blocking call
	 * @param task the task to execute 
	 * @return the result of the execution
	 */
	IResult ExecuteTask(ITask task);
}
