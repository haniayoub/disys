package Common;

import java.io.Serializable;

import WorkersSystem.WorkER.WorkItem;

public class Chunk<T extends Item> implements WorkItem,Serializable{

	private RemoteInfo clientRemoteInfo;
	private RemoteInfo executerRemoteInfo;
	private T[] items;
	
	public Chunk(RemoteInfo clientInfo,RemoteInfo executerInfo, T[] items) {
		super();
		this.clientRemoteInfo = clientInfo;
		this.executerRemoteInfo=executerInfo;
		this.items = items;
	}

	public RemoteInfo getClientRemoteInfo() {
		return clientRemoteInfo;
	}

	public RemoteInfo getExecuterRemoteInfo() {
		return executerRemoteInfo;
	}
	public T[] getItems() {
		return items;
	}	 
}
