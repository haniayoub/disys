package diSys.SystemManager;

import diSys.Common.Item;
import diSys.Common.SystemUpdates;

@SuppressWarnings("serial")
public class AutoUpdateTask extends Item {
	public AutoUpdateTask(SystemUpdates updates, int version) {
		super(-2);
		this.version = version;
		this.updates=updates;
	}
	public final Integer version;
	public final SystemUpdates updates;
}
