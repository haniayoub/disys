package diSys.Executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import diSys.Common.Item;
import diSys.Common.RemoteInfo;
import diSys.Common.RemoteItem;
import diSys.WorkersSystem.WorkER.AWorker;

/**
 * Read Remote Item Results from Results Queue and arrange them in A Map
 * of Clients 
 * @author saeed
 *
 * @param <ITEM>
 */
public class RemoteItemOrganizer<ITEM extends Item> extends AWorker< RemoteItem<ITEM>,RemoteItem<ITEM>> {
	ConcurrentHashMap<RemoteInfo,ConcurrentLinkedQueue<ITEM>> clientResults;
	public RemoteItemOrganizer(BlockingQueue<RemoteItem<ITEM>> wi,
			ConcurrentHashMap<RemoteInfo,ConcurrentLinkedQueue<ITEM>> rq) {
		super(wi, null);
		clientResults=rq;
	}

	@Override
	public RemoteItem<ITEM> doItem(RemoteItem<ITEM> task) {
		
		RemoteInfo key=task.getRemoteInfo();
		if(!clientResults.containsKey(key)){
			clientResults.put(key, new ConcurrentLinkedQueue<ITEM>());
		}
		if(!task.getItem().isNull()) 
		//if the result isNull, means we don't need to return a result at all
		{
			clientResults.get(key).add(task.getItem());
		}
		return null;
	}

}
