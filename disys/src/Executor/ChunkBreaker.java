package Executor;

import java.util.concurrent.BlockingQueue;

import Common.Chunk;
import Common.Item;
import Common.RemoteItem;
import WorkersSystem.WorkER.AWorker;

public class ChunkBreaker extends AWorker<Chunk<? extends Item>,RemoteItem<? extends Item>> {

	public ChunkBreaker(BlockingQueue<Chunk<? extends Item>> wi, BlockingQueue<RemoteItem<? extends Item>> rq) {
		super(wi, rq);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public RemoteItem<? extends Item> doItem(Chunk<? extends Item> task) {
		Item[] items=task.getItems();
		for(Item item:items){
			RemoteItem ri=new  RemoteItem(item,task.getClientRemoteInfo());
			this.Results.add(ri);
		}
		return null;
	}
}
