package Executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import AutoUpdate.JarClassLoader;
import Common.Chunk;
import Common.IExecutor;
import Common.Item;
import Common.JarFileReader;
import Common.Logger;
import Common.RMIRemoteInfo;
import Common.RemoteInfo;
import Common.RemoteItem;
import Networking.NetworkCommon;
import Networking.RMIItemCollector;
import Networking.RemoteItemReceiver;
import SystemManager.AutoUpdateTask;
import SystemManager.ISystemManager;
import SystemManager.SystemManager;
import WorkersSystem.WorkER.AWorker;
import WorkersSystem.WorkER.WorkerCollection;
import WorkersSystem.WorkER.WorkerSystem;

/**
 * Executer System : this is the full package of the Executer it includes all
 * the worker threads in order to make a fully working Remote executer
 * @author saeed
 *
 * @param <TASK> TASKs Type to receive and execute
 * @param <RESULT> Results Type to return 
 * @param <E> Executer Type
 */
public 
class ExecuterSystem<TASK extends Item,RESULT extends Item,E extends IExecutor<TASK,RESULT>> {
	
	//the chunks received from clients 
	private BlockingQueue<Chunk<TASK>> recievedChunks = 
		new LinkedBlockingQueue<Chunk<TASK>>();
	//tasks to perform
	private BlockingQueue<RemoteItem<TASK>> tasks = 
						new LinkedBlockingQueue<RemoteItem<TASK>>();
	//results Queue
	private BlockingQueue<RemoteItem<RESULT>> results= 
						new LinkedBlockingQueue<RemoteItem<RESULT>>();
	//Result organized in a Map of clients
	private ConcurrentHashMap<RemoteInfo,ConcurrentLinkedQueue<RESULT>> clientResults=
						new ConcurrentHashMap<RemoteInfo, ConcurrentLinkedQueue<RESULT>>();
	//
	//Workers of the executer System
	//
	
	private RemoteItemReceiver<Chunk<TASK>> chunkReceiver;
	private ChunkBreaker<TASK> chunkBreaker;
	private TaskExecuter<TASK,RESULT,E> taskExecuter;
	private RemoteItemOrganizer<RESULT> resultOrganizer;
    private RMIItemCollector<RESULT> itemCollector; 
	//system manager reference
    private ISystemManager<TASK> sysManager;
    private RMIRemoteInfo systemManagerRemoteInfo;
    //number of task executers
    private int numerOfExecuters;
	private WorkerSystem ws=new WorkerSystem();
	private WorkerCollection ExecutersCollection;
	@SuppressWarnings("unchecked")
	public ExecuterSystem(E executer,int numerOfWorkers,String SysManagerAddress,int sysManagerport) {
		super();
		systemManagerRemoteInfo=new RMIRemoteInfo(SysManagerAddress,sysManagerport,SystemManager.GlobalID);
		try {
			chunkReceiver=new RemoteItemReceiver<Chunk<TASK>>(recievedChunks);
		} catch (Exception e) {
			Logger.TerminateSystem("Error intializing Remote Chunk Reciever", e);
		}
		
		try {
			itemCollector=new RMIItemCollector<RESULT>(clientResults);
		} catch (Exception e) {
			Logger.TerminateSystem("Error intializing Remote Chunk Reciever ", e);
		}
		//////////////////////////Fire Wall problematic !!!!
		sysManager=NetworkCommon.loadRMIRemoteObject(systemManagerRemoteInfo);
		if(sysManager!=null){
		try {
			sysManager.addExecuter(chunkReceiver.getPort(),itemCollector.getPort());
		} catch (RemoteException e) {
			Logger.TraceWarning("Couldn't add Executer to System Manager", e);
		}
		}
		///////////////////////////////////////////////
		
		chunkBreaker=new ChunkBreaker<TASK>(recievedChunks,tasks, this);
		String myID=chunkReceiver.getRmiID();
		taskExecuter = new TaskExecuter<TASK,RESULT,E>(executer,myID,tasks,results);
		resultOrganizer=new RemoteItemOrganizer<RESULT>(results,clientResults);
		numerOfExecuters= numerOfWorkers;
		ExecutersCollection=new WorkerCollection(taskExecuter,numerOfExecuters);
		
		ws.add(chunkBreaker,1);
		ws.add(ExecutersCollection);
		ws.add(resultOrganizer,1);
	}

	public void Run(String[] args) {
		 ws.startWork();
	}
	
	public void Exit() {
		 ws.stopWork();
		 chunkReceiver.Dispose();
		 itemCollector.Dispose();
	}

	@SuppressWarnings("unchecked")
	public void updateExecuters(AutoUpdateTask item) throws MalformedURLException, InstantiationException, IllegalAccessException, ClassNotFoundException, FileNotFoundException {
		for(AWorker e : ExecutersCollection.getWorkerList())
		{
			Common.Logger.TraceInformation("Updating Executer...");
			
			String fileName = item.className + "_version" + item.version.toString();
			JarFileReader.WriteFile(fileName, item.jf);
			File f = new File(fileName);
			JarClassLoader jcl = new JarClassLoader(f);
			
			Common.Logger.TraceInformation("New Class " + item.className + "will be dynamically loaded...");
			((TaskExecuter)e).UpdateExecuter((IExecutor)jcl.loadClass(item.className).newInstance());
		}
	}
}
