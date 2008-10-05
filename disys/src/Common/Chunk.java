package Common;

import java.io.Serializable;

import WorkersSystem.WorkER.WorkItem;

@SuppressWarnings("serial")
public class Chunk<T extends Item> implements WorkItem,Serializable{

	private final RemoteInfo clientRemoteInfo;
	private final RemoteInfo executerRemoteInfo;
	private final T[] items;
	
	public Chunk(final RemoteInfo clientInfo,final RemoteInfo executerInfo, final T[] items) {
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
	public String toString(){
	return "chunk:"+items.length;
	}
	
}
