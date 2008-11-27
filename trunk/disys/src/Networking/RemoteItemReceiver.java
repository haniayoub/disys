package Networking;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;

import Common.Item;
import Executor.ExecuterRemoteData;

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

	@Override
	public RemoteData getExecuterData() throws RemoteException {
		ExecuterRemoteData erd = new ExecuterRemoteData();
		erd.numOfTasks = recievedItems.size();
		erd.log=Common.Logger.logTracer.toString();
		return erd;
	}
}
