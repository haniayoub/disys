package Client;

import Common.IResult;
import Common.ITask;

public class RemoteService implements IRemoteService{

	@Override
	public void DoTask(ITask task,int priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IResult ExecuteTask(ITask task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResult PopResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Stop() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int ExecutingTasksNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int ResultsNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int TotalTasksNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int WaitingTasksNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void Wait(boolean acceptNewJobs) {
		// TODO Auto-generated method stub
		
	}

}
