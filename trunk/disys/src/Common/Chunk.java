package Common;

@SuppressWarnings("serial")
public class Chunk<T extends Item> extends Item{

	private final ClientRemoteInfo clientRemoteInfo;
	private final RemoteInfo executerRemoteInfo;
	private final T[] items;
	
	public Chunk(long id,final ClientRemoteInfo clientInfo,final RemoteInfo executerInfo, final T[] items) {
		super(id);
		this.clientRemoteInfo = clientInfo;
		this.executerRemoteInfo=executerInfo;
		this.items = items;
	}

	public ClientRemoteInfo getClientRemoteInfo() {
		return clientRemoteInfo;
	}

	public RemoteInfo getExecuterRemoteInfo() {
		return executerRemoteInfo;
	}
	public T[] getItems() {
		return items;
	}	
	public int numberOfItems() {
		return items.length;
	}
	public String toString(){
	String $="chunk:"+getId()+"ItemsSize="+items.length;
	for (Item i:items)
			 $+=i.toString();
	return $;
	}
	
}
