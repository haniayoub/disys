package diSys.Common;

import java.io.Serializable;
import java.util.LinkedList;

public class SystemStatistics implements Serializable{

	@Override
	public String toString() {
		return "SystemStatistics [ExecutersData=" + ExecutersData
				+ ", numOfDisabledExecuters=" + numOfDisabledExecuters
				+ ", numOfEnabledExecuters=" + numOfEnabledExecuters
				+ ", numOfExecuters=" + numOfExecuters
				+ ", numOfFailedScheduleRequests="
				+ numOfFailedScheduleRequests + ", numOfScheduleRequests="
				+ numOfScheduleRequests + "]";
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int numOfExecuters = 0;
	public int numOfEnabledExecuters = 0;
	public int numOfDisabledExecuters = 0;
	public LinkedList<ExecuterStatistics> ExecutersData = new LinkedList<ExecuterStatistics>();
	public int numOfScheduleRequests = 0;
	public int numOfFailedScheduleRequests = 0;
	

}
