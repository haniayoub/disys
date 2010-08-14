package diSys.Common;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExecuterStatistics  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ExecuterRemoteInfo ri;
	public int BefferSize=0;
	public int BufferCapacity = 0;
	public int ExecutedTasks = 0;
	public Date ExecutionStartTime = null;
	private DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
	@Override
	public String toString() {
		return "-- ExecuterStatistics : "
		        + "\nremote info=" + ri
		        + "\nBefferSize=" + BefferSize
				+ "\nBufferCapacity=" + BufferCapacity 
				+ "\nExecutedTasks="+ ExecutedTasks 
				+ "\nExecutionStartTime=" + formatter.format(ExecutionStartTime)
				+ "\nLastExecutionTime=" + formatter.format(LastExecutionTime)
				+ "\nnumOfWorkerThreads=" + numOfWorkerThreads
				+ "\nRejectedChunks=" + RejectedChunks
				+ "\n";
	}
	public Date LastExecutionTime = null;
	public int numOfWorkerThreads = 0;

	public int RejectedChunks = 0; 

}
