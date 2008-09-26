package Common;

import java.io.Serializable;

public class Chunk<T extends Item> implements Serializable{

	private RemoteInfo remoteInfo;
	private T[] items;
	
	public Chunk(RemoteInfo remoteInfo, T[] items) {
		super();
		this.remoteInfo = remoteInfo;
		this.items = items;
	}
	 
}
