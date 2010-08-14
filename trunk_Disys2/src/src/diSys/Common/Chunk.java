package diSys.Common;

import java.util.LinkedList;

/**
 * Chunk of items Holds array of items , used for network overhead optimizations
 * 
 * @author saeed
 * 
 * @param <T>
 */
@SuppressWarnings("serial")
public class Chunk<T extends Item> extends Item {

	private final ClientRemoteInfo clientRemoteInfo;
	private final RemoteInfo executerRemoteInfo;
	private final T[] items;

	public Chunk(long id, final ClientRemoteInfo clientInfo,
			final RemoteInfo executerInfo, final T[] items) {
		super(id);
		this.clientRemoteInfo = clientInfo;
		this.executerRemoteInfo = executerInfo;
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

	@Override
	public String toString() {
		String $ = "chunk:" + getId() + "ItemsSize=" + items.length;
		for (Item i : items)
			$ += i.toString();
		return $;
	}

	public static <ITEM extends Item> Chunk<Item> CreateChunk(
			LinkedList<ITEM> itemsList) {
		int size = itemsList.size();
		final Item[] items = new Item[size];
		for (int i = 0; i < size; i++) {
			items[i] = itemsList.poll();
		}
		return new Chunk<Item>(0, null, null, items);
	}

}
