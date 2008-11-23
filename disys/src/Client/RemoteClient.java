package Client;

import java.util.concurrent.TimeUnit;

import Common.Item;

public class RemoteClient<TASK extends Item, RESULT extends Item> {
private ClientSystem<TASK, RESULT> clientSystem;
	private int taskNum;
	
	public RemoteClient(String SysManagerAddress, int sysManagerport,int chunkSize) {
	clientSystem=new ClientSystem<TASK, RESULT>(SysManagerAddress,sysManagerport,chunkSize);
	taskNum=0;
	}
	
	public void Start(){
		clientSystem.Start();
	}
	
	public void Stop(){
		if(taskNum!=0){
			Common.Logger.TraceWarning("The user didn't read all the Results of the tasks", null);
		}
		clientSystem.Stop();
	}
	
	public void AddTask(TASK task){
		clientSystem.tasks.offer(task);
		taskNum++;
	}
	
	public RESULT GetResult() throws Exception{
		if(taskNum==0) {
			System.out.println("GetResult:No Tasks in execution / No Results available");
			return null;
		}
		RESULT r=clientSystem.results.take();
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
			RESULT r= clientSystem.results.poll(timeOut,timeUnit );
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
}
