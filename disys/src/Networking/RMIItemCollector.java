package Networking;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import Common.Chunk;
import Common.ClientRemoteInfo;
import Common.Item;
import Common.Logger;
import Common.RemoteInfo;
/***
 * RMI ITEM Collector , RMI Object implements Collect method which returns the 
 * Client Results .  
 * @author saeed
 *
 * @param <ITEM> The Item to be wrapped in chunk and returned 
 */
@SuppressWarnings("serial")
public class RMIItemCollector<ITEM extends Item> extends RMIObjectBase
		implements IItemCollector<ITEM> {
	public static final String GlobalId = "itemCollector";

	private ConcurrentHashMap<RemoteInfo, ConcurrentLinkedQueue<ITEM>> clientResults;
	public RMIItemCollector(
			ConcurrentHashMap<RemoteInfo, ConcurrentLinkedQueue<ITEM>> clientResults)
			throws Exception {
		super(GlobalId);
		this.clientResults = clientResults;
	}

	// Collect the results of a client by id.
	@SuppressWarnings("unchecked")
	@Override
	public Chunk<ITEM> Collect(long id) throws RemoteException {
		String ip=this.GetClientHost();
		if(ip==null){
		Logger.TraceError("The Clinet Ip is null aborting Collect Items", null);
		return null;
		}
		ClientRemoteInfo address = new ClientRemoteInfo(ip, id);
		
		ConcurrentLinkedQueue<ITEM> itemsQueue = clientResults.get(address);
		if (itemsQueue == null || itemsQueue.isEmpty())
			return null;
		LinkedList<ITEM> itemsList = new LinkedList<ITEM>();
		while (!itemsQueue.isEmpty())
			itemsList.add(itemsQueue.poll());
		return (Chunk<ITEM>) Chunk.CreateChunk(itemsList);
	}

}
