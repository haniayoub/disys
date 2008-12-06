package Executor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
	private Object executerLock=new Object();
	private String myIP;
	private static PrintStream std=System.out;

	public TaskExecuter(E executor,String Ip, BlockingQueue<RemoteItem<TASK>> tasks,
			BlockingQueue<RemoteItem<RESULT>> results) {
		super(tasks, results);
		this.excutor = executor;
		myIP=Ip;
	}
	
	public void UpdateExecuter(E executer){
		String oldExecuterClass=null;
		if(this.excutor!= null) this.excutor.getClass().getName();
		String newExecuterClass=null;
		if(excutor!= null) excutor.getClass().getName();
		
		Common.Logger.TraceInformation("WrokerId ["+this.getId()+"]Updating Executer ["+oldExecuterClass+"] "+this.myIP+ " To :"+newExecuterClass);
	    synchronized(executerLock){
	    	oldExecuterClass=null;
			if(this.excutor!= null) this.excutor.getClass().getName();
	    	Common.Logger.TraceInformation("WrokerId ["+this.getId()+"]Updating To :"+oldExecuterClass+" has Started ...");
		this.excutor=executer;
		executerLock.notifyAll();
		}
		
		Common.Logger.TraceInformation("WrokerId ["+this.getId()+"]Update Succeeded : "+oldExecuterClass);
	}
	@SuppressWarnings("unchecked")
	@Override
	public RemoteItem<RESULT> doItem(RemoteItem<TASK> task) {
		Item Result;
	    Common.Logger.TraceInformation("WrokerId ["+this.getId()+"]:executing Task id["+task.getId()+"] for ["+task.getRemoteInfo()+"]");
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
		try{
			System.setOut(new PrintStream(out));	
			//synchronized(executerLock){
			Result = excutor.run(task.getItem());
			//}
			if(out.toString().length()!=0){
				StringBuilder sb=new StringBuilder();
				sb.append("Wroker Thread ["+this.getId()+"]");
				sb.append("___________ Executer:["+myIP+"] Wroker Thread ["+this.getId()+"] Task:["+task.getId()+"]__________\n");
				sb.append(out.toString());
				sb.append("_____________________________________________________\n");
				Result.setLog(sb.toString());
			}
			System.setOut(std);
		}catch(Exception e){
			System.setOut(std);
			Common.Logger.TraceWarning("Exception While : executing Task id["+task.getId()+"] for ["+task.getRemoteInfo()+"]",null);	
			Common.Logger.TraceError("User Code Exception",e);
			Common.Logger.TraceInformation("attaching exception to Result id["+task.getId()+"] for ["+task.getRemoteInfo()+"]");
			Result=new Item(task.getId());
			Result.setException(e);
		}
		return new RemoteItem(Result, task.getRemoteInfo());
	}

}
