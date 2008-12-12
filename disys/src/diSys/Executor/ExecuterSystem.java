package diSys.Executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import diSys.AutoUpdate.JarClassLoader;
import diSys.Common.Chunk;
import diSys.Common.IExecutor;
import diSys.Common.Item;
import diSys.Common.JarFileReader;
import diSys.Common.Logger;
import diSys.Common.RMIRemoteInfo;
import diSys.Common.RemoteInfo;
import diSys.Common.RemoteItem;
import diSys.Networking.NetworkCommon;
import diSys.Networking.RMIItemCollector;
import diSys.Networking.RemoteItemReceiver;
import diSys.SystemManager.AutoUpdateTask;
import diSys.SystemManager.ISystemManager;
import diSys.SystemManager.SystemManager;
import diSys.WorkersSystem.WorkER.AWorker;
import diSys.WorkersSystem.WorkER.WorkerCollection;
import diSys.WorkersSystem.WorkER.WorkerSystem;


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
	
	public static final String UpdateDir = "ExecuterUpdateJars";
	public static final String UpdateExtension = "ujar";
	
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
		File f=new File(UpdateDir);
		f.mkdir();		
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
		
		diSys.Common.Logger.TraceInformation("Starting Executer IRport: "+chunkReceiver.getPort()+" RCport:"+itemCollector.getPort()+" Workers:"+numerOfWorkers);

		if(SysManagerAddress!=null){
		//////////////////////////Fire Wall problematic !!!!
		systemManagerRemoteInfo=new RMIRemoteInfo(SysManagerAddress,sysManagerport,SystemManager.GlobalID);
		sysManager=NetworkCommon.loadRMIRemoteObject(systemManagerRemoteInfo);
		diSys.Common.Logger.TraceInformation("Connecting to system Manager "+SysManagerAddress +" "+ sysManagerport);
		if(sysManager!=null){
		try {
			sysManager.addExecuter(chunkReceiver.getPort(),itemCollector.getPort());
		} catch (RemoteException e) {
			Logger.TraceWarning("Couldn't add Executer to System Manager", null);
		}
		}
		///////////////////////////////////////////////
		}
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

		String fileName =getLastVerFile(item.version);
		diSys.Common.Logger.TraceInformation("Saving new Update Jar File to:"+fileName);
		JarFileReader.WriteFile(fileName, item.jf);
		
		File f = new File(fileName);
		JarClassLoader jcl = new JarClassLoader(f);
		diSys.Common.Logger.TraceInformation("New Class " + item.className + " will be dynamically loaded...");
		
		for(AWorker e : ExecutersCollection.getWorkerList())
		{
			diSys.Common.Logger.TraceInformation("Updating Executer :"+e.getId());
			IExecutor newExecuter=(IExecutor)jcl.loadClass(item.className).newInstance();
			((TaskExecuter)e).UpdateExecuter(newExecuter);
		}
	}
	
	private String getLastVerFile(int UpdateVer){
		return UpdateDir+"\\"+UpdateVer+"."+UpdateExtension;
	}
	
	@SuppressWarnings("unchecked")
	public RemoteItemReceiver GetItemReciver() {
	return this.chunkReceiver;
		
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	
	public static void main(String[] args) throws InterruptedException, IOException {
		PrintUsage();
		int workers=3;
		String address=null;
		int port=0;
		if(args.length>1){
		workers=Integer.parseInt(args[0]);
		}
		if(args.length>2){
			address=args[1];
			port=Integer.parseInt(args[2]);
			}
		ExecuterSystem es=new ExecuterSystem(null, workers,address,port); 
		System.out.println("Executer Started !");
		es.Run(args);
		System.in.read();
		es.Exit();
		System.out.println("executer Done");
	}
	public static void PrintUsage(){
		System.out.println("-------------------------------[Executer]----------------------------------");
		System.out.println("parameters:[worker Threads] [System Manager Address] [System Manager Port]");
		System.out.println("[worker Threads] = the number of worker threads default 3");
		System.out.println("[System Manager Address]   = optional to notify the system Manager");
		System.out.println("[System Manager Port]      = optional to notify the system Manager");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println();
	}

	
}