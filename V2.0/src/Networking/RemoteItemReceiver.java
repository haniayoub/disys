package Networking;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;

import Common.Item;

/**
 * ITEM receiver is RMI object which waits for clients to send to it items and
 * put them in a queue
 * 
 * @author saeed
 * 
 * @param <ITEM>
 */
@SuppressWarnings("serial")
public class RemoteItemReceiver<ITEM extends Item> extends RMIObjectBase
		implements IRemoteItemReceiver<ITEM> {
	public static final String GlobalId = "itemReciever";

	private BlockingQueue<ITEM> recievedItems;

	public RemoteItemReceiver(BlockingQueue<ITEM> itemsQueue) throws Exception {
		super(GlobalId);
		recievedItems = itemsQueue;
	}

	@Override
	public void Add(ITEM item) throws RemoteException {
		recievedItems.add(item);
	}

}
