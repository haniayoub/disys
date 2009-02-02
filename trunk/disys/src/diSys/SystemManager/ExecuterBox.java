package diSys.SystemManager;

import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;
import diSys.Networking.IItemCollector;
import diSys.Networking.IRemoteItemReceiver;


public class ExecuterBox<TASK extends Item,RESULT extends Item> {
	private IRemoteItemReceiver<TASK> ir;
	private IItemCollector<RESULT> rc;
	private ExecuterRemoteInfo ri;
	private int numOfTasks = 0;
	private String log = null;
	public boolean Blocked;
	public ExecuterBox(ExecuterRemoteInfo ri,IRemoteItemReceiver<TASK> ir, IItemCollector<RESULT> rc,
			boolean blocked) {
		super();
		this.ri=ri;
		this.ir = ir;
		this.rc = rc;
		Blocked = blocked;
	}
	public IRemoteItemReceiver<TASK> getItemReciever() {
		return ir;
	}
	public IItemCollector<RESULT> getResultCollector() {
		return rc;
	}
	public ExecuterRemoteInfo getRemoteInfo() {
		return ri;
	}
	public int getNumOfTasks() {
		return numOfTasks;
	}
	public void setNumOfTasks(int numOfTasks) {
		this.numOfTasks = numOfTasks;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
}
