package Common;

import WorkersSystem.WorkER.WorkItem;

public class RemoteItem<T extends Item> implements WorkItem {
private RemoteInfo remoteInfo;
private T Task;
public RemoteItem( T task,RemoteInfo remoteInfo) {
	super();
	this.remoteInfo = remoteInfo;
	Task = task;
}
public RemoteInfo getRemoteInfo() {
	return remoteInfo;
}

public T getTask() {
	return Task;
}
}
