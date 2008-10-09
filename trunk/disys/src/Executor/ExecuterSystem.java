package Executor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Common.Chunk;
import Common.IExecutor;
import Common.Item;
import Common.ItemPrinter;
import Common.RMIRemoteInfo;
import Common.RemoteItem;
import Networking.RemoteItemReceiver;
import SystemManager.ISystemManager;
import SystemManager.SystemManager;
import WorkersSystem.WorkER.WorkerSystem;

public class ExecuterSystem<TASK extends Item,RESULT extends Item,E extends IExecutor<TASK,RESULT>> {
	public BlockingQueue<RemoteItem<TASK>> tasks = 
						new LinkedBlockingQueue<RemoteItem<TASK>>();
	public BlockingQueue<RemoteItem<RESULT>> results= 
						new LinkedBlockingQueue<RemoteItem<RESULT>>();
	public BlockingQueue<Chunk<TASK>> recievedChunks = 
		new LinkedBlockingQueue<Chunk<TASK>>();
	
	private TaskExecuter<TASK,RESULT,E> taskExecuter;
	private ChunkBreaker<TASK> chunkBreaker;
	private ItemPrinter<RemoteItem<RESULT>> resultPrinter=new ItemPrinter<RemoteItem<RESULT>>(results,null);
	
	private ISystemManager<TASK> sysManager;
	@SuppressWarnings("unused")
	private RemoteItemReceiver<Chunk<TASK>> chunkReceiver;
	private int numerOfExecuters;
	
	WorkerSystem ws=new WorkerSystem();
	RMIRemoteInfo systemManagerRemoteInfo;
	@SuppressWarnings("unchecked")
	public ExecuterSystem(E executer,int numerOfWorkers,String SysManagerAddress,int sysManagerport) {
		super();
		systemManagerRemoteInfo=new RMIRemoteInfo(SysManagerAddress,sysManagerport,SystemManager.GlobalID);
		try {
			
			chunkReceiver=new RemoteItemReceiver<Chunk<TASK>>(recievedChunks);
		} catch (RemoteException e) {
			System.out.println("Error intializing Remote Chunk Reciever :"+e.toString());
			e.printStackTrace();
		}
		
		//////////////////////////Fire Wall problematic !!!!
		try {
			sysManager = (ISystemManager<TASK>) 
				    Naming.lookup(systemManagerRemoteInfo.GetRmiAddress());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(sysManager!=null){
		try {
			sysManager.addExecuter(chunkReceiver.getLocalId(), chunkReceiver.getPort());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Couldn't add Executer to System Manager");
		}
		}
		///////////////////////////////////////////////
		
		this.taskExecuter = new TaskExecuter<TASK,RESULT,E>(executer,tasks,results);
		chunkBreaker=new ChunkBreaker<TASK>(recievedChunks,tasks);
		this.numerOfExecuters= numerOfWorkers;
		ws.add(taskExecuter,numerOfExecuters);
		ws.add(chunkBreaker,1);
		ws.add(resultPrinter,1);
	}

	/**
	 * @param args
	 */
	public void Run(String[] args) {
		 ws.startWork();
	}
	
	public void Exit() {
		 ws.stopWork();
	}

}
