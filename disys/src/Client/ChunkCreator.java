package Client;

import java.util.LinkedList;

import Common.Chunk;
import Common.Item;
import WorkersSystem.WorkER.AWorker;


public class ChunkCreator<ITEM extends Item> extends AWorker<ITEM,Chunk<ITEM>> {
	private int MAX_CHUNK_SIZE;
	LinkedList<Item> chunkItems=new LinkedList<Item>();
	public ChunkCreator(int max_chunk_size) {
		super();
		MAX_CHUNK_SIZE = max_chunk_size;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Chunk<ITEM> doItem(ITEM task) {
		chunkItems.add(task);
		if(this.WorkItems.isEmpty()||chunkItems.size()==MAX_CHUNK_SIZE)
					return (Chunk<ITEM>) CreateChunk();
		return null;
	}
	
	private Chunk<Item> CreateChunk(){
		final Item[] items=new Item[chunkItems.size()];
		for(int i=0;i<chunkItems.size() ;i++){
		items[i]=chunkItems.poll();
		}
		return new Chunk<Item>(0,null,null,items);
	}

}
