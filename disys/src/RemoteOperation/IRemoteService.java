package RemoteOperation;

import Common.IResult;
import Common.ITask;

public interface IRemoteService {
	/**
	 * starts the service 
	 */
	void 	Start();
	/**
	 * Wait until all waiting Tasks Are executed , and all results are ready 
	 */
	void 	Wait(boolean acceptNewJobs);
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
	 * Get the most recent ready result, 
	 * Blocking call if at least one task is being remotely executed.
	 * 
	 * @return IResult : the ready result if exists else 
	 * if no task is being remotely executed return null.
	 */
	IResult PopResult();//time out
	/**
	 * execute task , Blocking call
	 * @param task the task to execute 
	 * @return the result of the execution
	 */
	IResult ExecuteTask(ITask task);
	/**
	 * number of waiting tasks  
	 */
	int WaitingTasksNum();
	/**
	 * number of tasks being executed the instance  
	 */
	int ExecutingTasksNum();
	/**
	 * ExecutingTasksNum + WaitingTasksNum
	 */
	int TotalTasksNum();
	/**
	 * Ready Results number
	 */
	int ResultsNum();
}