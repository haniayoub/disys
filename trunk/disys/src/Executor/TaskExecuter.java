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
	private String myIP;
	private static PrintStream std=System.out;

	public TaskExecuter(E executor,String Ip, BlockingQueue<RemoteItem<TASK>> tasks,
			BlockingQueue<RemoteItem<RESULT>> results) {
		super(tasks, results);
		this.excutor = executor;
		myIP=Ip;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RemoteItem<RESULT> doItem(RemoteItem<TASK> task) {
		Item Result;
	    Common.Logger.TraceInformation("executing Task id["+task.getId()+"] for ["+task.getRemoteInfo()+"]");
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
		try{
			System.setOut(new PrintStream(out));	
			Result = excutor.run(task.getItem());
			if(out.toString().length()!=0){
				StringBuilder sb=new StringBuilder();
				sb.append("___________ Executer:["+myIP+"] Task:["+task.getId()+"]__________\n");
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