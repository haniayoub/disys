package Executor;

import java.util.concurrent.BlockingQueue;

import Common.Chunk;
import Common.Item;
import Common.RemoteItem;
import WorkersSystem.WorkER.AWorker;

public class ChunkBreaker<ITEM extends Item> extends AWorker<Chunk<ITEM>,RemoteItem<ITEM>> {

	public ChunkBreaker(BlockingQueue<Chunk<ITEM>> wi, BlockingQueue<RemoteItem<ITEM>> rq) {
		super(wi, rq);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public RemoteItem<ITEM> doItem(Chunk<ITEM> chunk) {
		Item[] items=chunk.getItems();
		for(Item item:items){
			RemoteItem ri=new  RemoteItem(item,chunk.getClientRemoteInfo());
			this.Results.add(ri);
		}
		return null;
	}
}
