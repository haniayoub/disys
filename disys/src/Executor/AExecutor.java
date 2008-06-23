package Executor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import Common.IResult;
import Common.ITask;

public abstract class AExecutor extends UnicastRemoteObject implements IExecutor  {

	Object lock;
	protected AExecutor() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		}
	abstract protected IResult execute(ITask task);

	@Override
	public IResult run(ITask task) throws RemoteException {
		// TODO Auto-generated method stub
		synchronized(lock){
		
		}
		return execute(task);
	}
	
	
}
