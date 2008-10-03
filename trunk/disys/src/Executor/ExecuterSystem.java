package Executor;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Networking.RemoteChunkReceiver;
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
	
	private TaskExecuter<E> taskExecuter;
	private ChunkBreaker chunkBreaker;
	@SuppressWarnings("unused")
	private RemoteChunkReceiver chunkReceiver;
	private int numerOfExecuters;
	
	WorkerSystem ws=new WorkerSystem();
	public ExecuterSystem(E executer,int numerOfWorkers) {
		super();
		try {
			chunkReceiver=new RemoteChunkReceiver(0,recievedChunks);
		} catch (RemoteException e) {
			System.out.println("Error intializing Remote Chunk Reciever :"+e.toString());
			e.printStackTrace();
		}
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
