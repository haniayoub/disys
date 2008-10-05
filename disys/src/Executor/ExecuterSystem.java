package Executor;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Networking.RemoteItemReceiver;
import WorkersSystem.WorkER.WorkerSystem;

import Common.Chunk;
import Common.IExecutor;
import Common.Item;
import Common.RemoteItem;

public class ExecuterSystem<TASK extends Item,RESULT extends Item,E extends IExecutor<TASK,RESULT>> {
	public BlockingQueue<RemoteItem<TASK>> tasks = 
						new LinkedBlockingQueue<RemoteItem<TASK>>();
	public BlockingQueue<RemoteItem<RESULT>> results= 
						new LinkedBlockingQueue<RemoteItem<RESULT>>();
	public BlockingQueue<Chunk<TASK>> recievedChunks = 
		new LinkedBlockingQueue<Chunk<TASK>>();
	
	private TaskExecuter<TASK,RESULT,E> taskExecuter;
	private ChunkBreaker<TASK> chunkBreaker;
	@SuppressWarnings("unused")
	private RemoteItemReceiver<Chunk<TASK>> chunkReceiver;
	private int numerOfExecuters;
	
	WorkerSystem ws=new WorkerSystem();
	public ExecuterSystem(E executer,int numerOfWorkers) {
		super();
		try {
			chunkReceiver=new RemoteItemReceiver<Chunk<TASK>>(0,recievedChunks);
		} catch (RemoteException e) {
			System.out.println("Error intializing Remote Chunk Reciever :"+e.toString());
			e.printStackTrace();
		}
		this.taskExecuter = new TaskExecuter<TASK,RESULT,E>(executer,tasks,results);
		chunkBreaker=new ChunkBreaker<TASK>(recievedChunks,tasks);
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
