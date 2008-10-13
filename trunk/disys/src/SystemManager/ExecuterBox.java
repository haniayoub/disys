package SystemManager;

import Common.Item;
import Networking.IItemCollector;
import Networking.IRemoteItemReceiver;


public class ExecuterBox<TASK extends Item,RESULT extends Item> {
	private IRemoteItemReceiver<TASK> ir;
	private IItemCollector<RESULT> rc;
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


}
