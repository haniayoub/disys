package Common;

import java.io.Serializable;

import WorkersSystem.WorkER.WorkItem;

public class RemoteItem<T extends Item> implements WorkItem {
private RemoteInfo remoteInfo;
private T Item;
public RemoteItem( T task,RemoteInfo remoteInfo) {
	super();
	this.remoteInfo = remoteInfo;
	Item = task;
}
public RemoteInfo getRemoteInfo() {
	return remoteInfo;
}

public T getItem() {
	return Item;
}
}
