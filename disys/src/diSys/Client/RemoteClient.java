package diSys.Client;

import java.util.concurrent.TimeUnit;

import diSys.Common.Exceptions;
import diSys.Common.Item;


public class RemoteClient<TASK extends Item, RESULT extends Item> {
	public ClientSystem<TASK, RESULT> clientSystem; //TODO: change to private
	private int taskNum;
	
	public RemoteClient(String SysManagerAddress, int sysManagerport,int chunkSize) {
	try {
		clientSystem=new ClientSystem<TASK, RESULT>(SysManagerAddress,sysManagerport,chunkSize);
	} catch (Exception e) {
		diSys.Common.Logger.TraceWarning("Client is not connected through RMI : this could interrupt system Clean Exit", e);
	}
	taskNum=0;
	}
	
	public RemoteClient(String SysManagerAddress, int sysManagerport,int chunkSize,String updateJarPath,String executerClassName) {
		try {
			clientSystem=new ClientSystem<TASK, RESULT>(SysManagerAddress,sysManagerport,chunkSize,updateJarPath,executerClassName);
		} catch (Exception e) {
			diSys.Common.Logger.TraceWarning("Client is not connected through RMI : this could interrupt system Clean Exit", e);
		}
		taskNum=0;
		}
	
	public void Start(){
		clientSystem.Start();
	}
	
	public void Stop(){
		if(taskNum!=0){
			diSys.Common.Logger.TraceWarning("The user didn't read all the Results of the tasks", null);
		}
		clientSystem.Stop();
	}
	
	public void AddTask(TASK task){
		clientSystem.AddTask(task);
		taskNum++;
	}
	
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
	
	public int GetTaskNum(){
		return taskNum;
	}
	
	
	public void Exit() throws Exception{
		if(taskNum!=0) throw new Exceptions.ClientException("The user didn't read all the Results of the tasks");
			
		clientSystem.Stop();
	}
}
