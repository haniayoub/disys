package Client;

import java.util.concurrent.TimeUnit;

import Common.Item;

public class RemoteClient<TASK extends Item, RESULT extends Item> {
private ClientSystem<TASK, RESULT> clientSystem;
	public RemoteClient(String SysManagerAddress, int sysManagerport,int chunkSize) {
	clientSystem=new ClientSystem<TASK, RESULT>(SysManagerAddress,sysManagerport,chunkSize);
	}
	
	public void AddTask(TASK task){
		clientSystem.tasks.add(task);
	}
	
	public RESULT GetResult() throws Exception{
		if(clientSystem.isStable()) return null;
		RESULT r=clientSystem.results.poll();
		if(r.getLog()!=null)System.out.print(r.getLog());
		if(r.getException()!=null) throw r.getException();
		return r;
	}
	
	public RESULT GetResult(int timeOut,TimeUnit timeUnit) throws Exception{
		try {
			RESULT r= clientSystem.results.poll(timeOut,timeUnit );
			if(r==null) return null;
			if(r.getLog()!=null)System.out.print(r.getLog());
			if(r.getException()!=null) throw r.getException();
			return r;
		} catch (InterruptedException e) {
			return null;
		}
	}
}
