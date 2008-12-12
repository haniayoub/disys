package diSys.SystemManager;

import diSys.Common.Item;
import diSys.Networking.IItemCollector;
import diSys.Networking.IRemoteItemReceiver;


public class ExecuterBox<TASK extends Item,RESULT extends Item> {
	private IRemoteItemReceiver<TASK> ir;
	private IItemCollector<RESULT> rc;
	private int numOfTasks = 0;
	private String log = null;
	public boolean Blocked;
	public ExecuterBox(IRemoteItemReceiver<TASK> ir, IItemCollector<RESULT> rc,
			boolean blocked) {
		super();
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
