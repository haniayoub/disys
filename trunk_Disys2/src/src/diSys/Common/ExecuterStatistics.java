package diSys.Common;

import java.io.Serializable;
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
	@Override
	public String toString() {
		return "ExecuterStatistics [BefferSize=" + BefferSize
				+ ", BufferCapacity=" + BufferCapacity + ", ExecutedTasks="
				+ ExecutedTasks + ", ExecutionStartTime=" + ExecutionStartTime
				+ ", LastExecutionTime=" + LastExecutionTime
				+ ", numOfWorkerThreads=" + numOfWorkerThreads + ", ri=" + ri
				+ "]";
	}
	public Date LastExecutionTime = null;
	public int numOfWorkerThreads = 0; 

}
