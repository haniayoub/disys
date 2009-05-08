package diSys.Client;

import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

import diSys.Common.Item;


public class RemoteClient<TASK extends Item, RESULT extends Item> {
	public ClientSystem<TASK, RESULT> clientSystem; //TODO: change to private
	private int taskNum;
	
	/**
	 * Crate new remote client and connects it with the system manager
	 * @param SysManagerAddress : the system manager Address 
	 * @param sysManagerport System manager port
	 * @param chunkSize The chunk of tasks maximum size to be created and sent to executer
	 */
	public RemoteClient(String SysManagerAddress, int sysManagerport,int chunkSize) {
	try {
		clientSystem=new ClientSystem<TASK, RESULT>(SysManagerAddress,sysManagerport,chunkSize);
	} catch (Exception e) {
		diSys.Common.Logger.TraceWarning("Client is not connected through RMI : this could interrupt system Clean Exit", e);
	}
	taskNum=0;
	}
	
	/**
	 *  start the Remote client system
	 */
	public void Start(){
		if(clientSystem!=null)
		clientSystem.Start();
	}
	
	/**
	 * Stop the remote Client System
	 */
	public void Stop(){
		if(taskNum!=0){
			diSys.Common.Logger.TraceWarning("The user didn't read all the Results of the tasks", null);
		}
		if(clientSystem!=null)
		clientSystem.Stop();
	}
	
	/**
	 * add Task to the Queue to be sent to Executers
	 * @param task
	 */
	public void AddTask(TASK task){
		clientSystem.AddTask(task);
		taskNum++;
	}
	
	public RESULT DoTask(TASK task) throws Exception{
		RESULT r=clientSystem.DoTask(task);
		if(r.getLog()!=null) System.out.print(r.getLog());
		if(r.getException()!=null) throw r.getException();
		return r;
	}
	/**
	 *  Returns the least recent Result  
	 * 	Note : Blocking call if there is Tasks in waiting queue
	 * @return the least recent Result
	 * @throws Exception the exception thrown at the remote executer while executing the relevant task of the result , if exists 
	 */
	public RESULT GetResult() throws Exception{
		if(taskNum==0) {
			System.out.println("GetResult:No Tasks in execution / No Results available");
			return null;
		}
		RESULT r=clientSystem.Take();
		taskNum--;
		if(r.getLog()!=null) System.out.print(r.getLog());
		if(r.getException()!=null) throw r.getException();
		return r;
	}
	
	/**
	 * the Same as GetResult() with time constraints 
	 * @param timeOut the time to wait for a result to be ready
	 * @param timeUnit
	 * @return the least recent Result
	 * @throws Exception the exception thrown at the remote executer while executing the relevant task of the result , if exists 
	 */
	public RESULT GetResult(int timeOut,TimeUnit timeUnit) throws Exception{
		if(taskNum==0) {
		System.out.println("GetResult:No Tasks in execution / No Results available");
			return null;
		}
		try {
			RESULT r= clientSystem.Poll(timeOut,timeUnit );
			if(r==null) return null;
			taskNum--;
			if(r.getLog()!=null)System.out.print(r.getLog());
			if(r.getException()!=null) {
				System.out.println("Exception while executing Task :["+r.getId()+"]");
				throw r.getException();
			}
			return r;
		} catch (InterruptedException e) {
			return null;
		}
	}
	
	/**
	 * get the number of tasks in execution or waiting for execution
	 * @return
	 */
	public int GetTaskNum(){
		return taskNum;
	}
	
	/**
	 * Indicates all results were received and there is no more tasks to run  
	 * @return
	 */
	public boolean IsIdle(){
		try {
			return clientSystem.IsIdle();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
}
