package Executor;

import java.rmi.RemoteException;

import Common.IResult;
import Common.ITask;

public interface IExecutor {
	 public IResult run(ITask task) throws RemoteException;
}
