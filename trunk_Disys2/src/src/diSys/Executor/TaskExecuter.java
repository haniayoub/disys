package diSys.Executor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import diSys.Common.IExecutor;
import diSys.Common.Item;
import diSys.Common.RemoteItem;
import diSys.WorkersSystem.WorkER.AWorker;

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
	private static PrintStream std=System.out;
	private ExecuterSystem es;

	public TaskExecuter(E executor,String Ip, BlockingQueue<RemoteItem<TASK>> tasks,
			BlockingQueue<RemoteItem<RESULT>> results, ExecuterSystem es) {
		super(tasks, results);
		this.excutor = executor;
		this.es = es;
	}
	
	@Override
	public RemoteItem<RESULT> doItem(RemoteItem<TASK> task) {
		Item Result;
		es.setCurrentTask(task.getItem());
	    diSys.Common.Logger.TraceInformation("executing Task id["+task.getId()+"] for ["+task.getRemoteInfo()+"]");
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
		try{ 
			  System.setOut(new PrintStream(out));	
			  Item item = task.getItem();
			  
			 Result = excutor.run(item);
			 // Result = item.run();
			  System.setOut(std);
			  diSys.Common.Logger.TraceInformation("Finished executing Task id["+task.getId()+"]");
			if(out.toString().length()!=0){
				StringBuilder sb=new StringBuilder();
				sb.append("_____________________________________________________\n\r");
				sb.append("Task ["+task.getId()+"]Log :"+task.toString()+"\n\r");
				String[] lines=out.toString().split("\n");
				// Create the pattern
				String patternStr = "\\d\\d/\\d\\d/\\d\\d*";
		        Pattern pattern = Pattern.compile(patternStr);
		        Matcher matcher = pattern.matcher("");
		    
		        // Retrieve all lines that match pattern
		        for(String line:lines)
		        { 
		            matcher.reset(line);
		            if (matcher.find()) {
		            	System.out.println(line);
		            }else{
		            	sb.append(line);
		            }
		        }
				//sb.append(out.toString());
				sb.append("_____________________________________________________\n\r");
				Result.setLog(sb.toString());
			}
			
		}catch(Exception e){
			System.setOut(std);
			diSys.Common.Logger.TraceWarning("Exception While : executing Task id["+task.getId()+"] for ["+task.getRemoteInfo()+"]",null);	
			diSys.Common.Logger.TraceError("User Code Exception",e);
			diSys.Common.Logger.TraceInformation("attaching exception to Result id["+task.getId()+"] for ["+task.getRemoteInfo()+"]");
			Result=new Item(task.getId());
			Result.setException(e);
		}
		es.setCurrentTask(null);
		return new RemoteItem(Result, task.getRemoteInfo());
	}

}
