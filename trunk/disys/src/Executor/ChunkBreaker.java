package Executor;

import java.util.concurrent.BlockingQueue;

import Common.Chunk;
import Common.Item;
import Common.RemoteItem;
import SystemManager.AutoUpdateTask;
import SystemManager.CleanExitTask;
import WorkersSystem.WorkER.AWorker;

/**
 * Reads chunks from A Queue and break them into Items and put them in Items Queue 
 * @author saeed
 *
 * @param <ITEM>
 */
public class ChunkBreaker<ITEM extends Item> extends AWorker<Chunk<ITEM>,RemoteItem<ITEM>> {

	@SuppressWarnings("unchecked")
	public ChunkBreaker(BlockingQueue<Chunk<ITEM>> wi, BlockingQueue<RemoteItem<ITEM>> rq, ExecuterSystem es) {
		super(wi, rq);
		this.es = es;
	}
	
	@SuppressWarnings("unchecked")
	ExecuterSystem es;

	@SuppressWarnings("unchecked")
	@Override
	public RemoteItem<ITEM> doItem(Chunk<ITEM> chunk) {
		Item[] items=chunk.getItems();
		Common.Logger.TraceInformation("New Chunk size["+items.length+"] recived from ["+chunk.getClientRemoteInfo()+"]");
		for(Item item:items){
			if(item instanceof CleanExitTask)
			{
				Common.Logger.TraceInformation("Exit Command [Task] recieved");
				//TODO: free all buffers
				//System.out.print("Executer has been killed, exiting...");
				//System.exit(0);
				Common.Logger.TerminateSystem("Executer has been killed, exiting...", null);
			}
			if(item instanceof AutoUpdateTask)
			{
				Common.Logger.TraceInformation("Update Command [Task] recieved , update to :"+((AutoUpdateTask)item).className);
				try{
					es.updateExecuters((AutoUpdateTask)item);
				}
				catch(Exception e){
					Common.Logger.TraceWarning("Executer System Could not update executers", e);
				}
				return null;
			}
			RemoteItem ri=new  RemoteItem(item,chunk.getClientRemoteInfo());
			this.Results.add(ri);
		}
		return null;
	}
}
