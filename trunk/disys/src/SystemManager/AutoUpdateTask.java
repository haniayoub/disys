package SystemManager;

import Common.Item;

@SuppressWarnings("serial")
public class AutoUpdateTask extends Item {
	public AutoUpdateTask(byte[] jf, int version, String className) {
		super(-2);
		this.version = version;
		this.jf = jf;
		this.className = className;
	}
	public final Integer version;
	public final byte[] jf;
	public final String className;
}
