package Executor;

import java.util.concurrent.BlockingQueue;

import Common.Chunk;
import Common.Item;
import Common.RemoteItem;
import SystemManager.CleanExitTask;
import WorkersSystem.WorkER.AWorker;

/**
 * Reads chunks from A Queue and break them into Items and put them in Items Queue 
 * @author saeed
 *
 * @param <ITEM>
 */
public class ChunkBreaker<ITEM extends Item> extends AWorker<Chunk<ITEM>,RemoteItem<ITEM>> {

	public ChunkBreaker(BlockingQueue<Chunk<ITEM>> wi, BlockingQueue<RemoteItem<ITEM>> rq) {
		super(wi, rq);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RemoteItem<ITEM> doItem(Chunk<ITEM> chunk) {
		Item[] items=chunk.getItems();
		Common.Logger.TraceInformation("New Chunk size["+items.length+"] recived from ["+chunk.getClientRemoteInfo()+"]");
		for(Item item:items){
			if(item instanceof CleanExitTask)
			{
				//TODO: free all buffers
				System.out.print("Executer has been killed, exiting...");
				System.exit(0);
			}
			RemoteItem ri=new  RemoteItem(item,chunk.getClientRemoteInfo());
			this.Results.add(ri);
		}
		return null;
	}
}
