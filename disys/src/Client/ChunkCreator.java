package Client;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import Common.Chunk;
import Common.Item;
import WorkersSystem.WorkER.AWorker;


public class ChunkCreator<ITEM extends Item> extends AWorker<ITEM,Chunk<ITEM>> {

	private int MAX_CHUNK_SIZE;
	LinkedList<Item> chunkItems=new LinkedList<Item>();
	public ChunkCreator(BlockingQueue<ITEM> wi, BlockingQueue<Chunk<ITEM>> rq,int max_chunk_size) {
		super(wi, rq);
		MAX_CHUNK_SIZE = max_chunk_size;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Chunk<ITEM> doItem(ITEM task) {
		chunkItems.add(task);
		if(this.WorkItems.isEmpty()||chunkItems.size()==MAX_CHUNK_SIZE){
			Chunk<ITEM> chunk=		(Chunk<ITEM>) CreateChunk();
			return chunk;
		}
		return null;
	}
	
	private Chunk<Item> CreateChunk(){
		int size=chunkItems.size();
		final Item[] items=new Item[size];	
		for(int i=0;i<size ;i++){
		items[i]=chunkItems.poll();
		}
		return new Chunk<Item>(0,null,null,items);
	}

}
