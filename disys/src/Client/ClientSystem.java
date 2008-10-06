package Client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import WorkersSystem.WorkER.WorkerSystem;

import Common.Chunk;
import Common.Item;
import Common.RemoteItem;

public class ClientSystem<TASK extends Item,RESULT extends Item> {

	public BlockingQueue<RemoteItem<TASK>> tasks = 
		new LinkedBlockingQueue<RemoteItem<TASK>>();
	public BlockingQueue<RemoteItem<RESULT>> results= 
		new LinkedBlockingQueue<RemoteItem<RESULT>>();
	public BlockingQueue<Chunk<TASK>> taskChunks = 
		new LinkedBlockingQueue<Chunk<TASK>>();
	WorkerSystem ws=new WorkerSystem();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
