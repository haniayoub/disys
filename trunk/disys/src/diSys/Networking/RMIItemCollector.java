package diSys.Networking;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import diSys.Common.Chunk;
import diSys.Common.ClientRemoteInfo;
import diSys.Common.Item;
import diSys.Common.Logger;
import diSys.Common.RemoteInfo;

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

	@Override
	public String CollectLog() throws RemoteException {
		
		return diSys.Common.Logger.logTracer.toString();
	}

}
