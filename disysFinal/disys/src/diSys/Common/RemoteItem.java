package diSys.Common;

@SuppressWarnings("serial")
public class RemoteItem<T extends Item> extends Item {
	private RemoteInfo remoteInfo;
	private T item;

	public RemoteItem(T item, RemoteInfo remoteInfo) {
		super(item.getId());
		this.remoteInfo = remoteInfo;
		this.item = item;
	}

	public RemoteInfo getRemoteInfo() {
		return remoteInfo;
	}

	public T getItem() {
		return item;
	}

	public String toString() {
		return item.toString();
	}
}
