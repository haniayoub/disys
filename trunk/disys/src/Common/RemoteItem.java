package Common;

@SuppressWarnings("serial")
public class RemoteItem<T extends Item> extends Item {
private RemoteInfo remoteInfo;
private T Item;
public RemoteItem( T item,RemoteInfo remoteInfo) {
	super(item.getId());
	this.remoteInfo = remoteInfo;
	Item = item;
}
public RemoteInfo getRemoteInfo() {
	return remoteInfo;
}

public T getItem() {
	return Item;
}
}
