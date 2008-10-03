package Executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import WorkersSystem.WorkER.WorkerSystem;

import Common.Chunk;
import Common.IExecutor;
import Common.Item;
import Common.RemoteItem;

public class ExecuterSystem<E extends IExecutor<? extends Item,? extends Item>> {
	public BlockingQueue<RemoteItem<? extends Item>> tasks = 
						new LinkedBlockingQueue<RemoteItem<? extends Item>>();
	public BlockingQueue<RemoteItem<? extends Item>> results= 
						new LinkedBlockingQueue<RemoteItem<? extends Item>>();
	public BlockingQueue<Chunk<? extends Item>> recievedChunks = 
		new LinkedBlockingQueue<Chunk<? extends Item>>();
	
	TaskExecuter<E> taskExecuter;
	ChunkBreaker chunkBreaker;
	int numerOfExecuters;
	
	WorkerSystem ws=new WorkerSystem();
	public ExecuterSystem(E executer,int numerOfWorkers) {
		super();
		this.taskExecuter = new TaskExecuter<E>(executer,tasks,results);
		chunkBreaker=new ChunkBreaker(recievedChunks,tasks);
		this.numerOfExecuters= numerOfWorkers;
		ws.add(taskExecuter,numerOfExecuters);
		ws.add(chunkBreaker,1);
	}

	/**
	 * @param args
	 */
	public void Run(String[] args) {
		 ws.startWork();
	}

}
