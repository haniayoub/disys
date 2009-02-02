package diSys.Executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import diSys.AutoUpdate.JarClassLoader;
import diSys.Common.Chunk;
import diSys.Common.FileManager;
import diSys.Common.IExecutor;
import diSys.Common.Item;
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
	public static final String UpdateExtension = "jar";
	public static final String ClassNameExtension = "clsn";
	public static final String VersionFile = "Version.txt";
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
	@SuppressWarnings("unchecked")
	private TaskExecuter<TASK,RESULT,IExecutor> taskExecuter;
	private RemoteItemOrganizer<RESULT> resultOrganizer;
    private RMIItemCollector<RESULT> itemCollector; 
	//system manager reference
    private ISystemManager<TASK> sysManager;
    private RMIRemoteInfo systemManagerRemoteInfo;
    //number of task executers
    private int numerOfExecuters;
	private WorkerSystem ws=new WorkerSystem();
	private WorkerCollection ExecutersCollection;
	private int Version;
	@SuppressWarnings("unchecked")
	public ExecuterSystem(IExecutor executer,int numerOfWorkers,int irport,int rcport,String SysManagerAddress,int sysManagerport) throws InterruptedException {
		super();
		File f=new File(UpdateDir);
		f.mkdir();
		Thread.sleep(1000);
		try {
			chunkReceiver=new RemoteItemReceiver<Chunk<TASK>>(recievedChunks,irport);
		} catch (Exception e) {
			Logger.TerminateSystem("Error intializing Remote Chunk Reciever", e);
		}
		
		try {
			itemCollector=new RMIItemCollector<RESULT>(clientResults,rcport);
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
		IExecutor newExec=executer;
		try {
			newExec=LoadUpdates();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chunkBreaker=new ChunkBreaker<TASK>(recievedChunks,tasks, this);
		chunkReceiver.Version=this.Version;
		String myID=chunkReceiver.getRmiID();
		taskExecuter = new TaskExecuter<TASK,RESULT,IExecutor>(newExec,myID,tasks,results);
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
/*
	@SuppressWarnings("unchecked")
	public void updateExecuters(AutoUpdateTask item) throws MalformedURLException, InstantiationException, IllegalAccessException, ClassNotFoundException, FileNotFoundException {

		String fileName =getLastVerFile(item.version);
		diSys.Common.Logger.TraceInformation("Saving new Update Jar File to:"+fileName);
		FileManager.WriteFile(fileName, item.jf);
		
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
	*/
	private String getLastVerFile(int UpdateVer){
		return getVerDir(UpdateVer)+"\\Updates."+UpdateExtension;
	}
	
	private String classNameFile(int UpdateVer){
		return getVerDir(UpdateVer)+"\\ClassName."+ClassNameExtension;
	}
	
	private String getVerDir(int UpdateVer){
		return "Ver"+UpdateVer;
	}
	
	@SuppressWarnings("unchecked")
	public RemoteItemReceiver GetItemReciver() {
	return this.chunkReceiver;
	}
	
	public void PrepareToUpdate(AutoUpdateTask item) throws FileNotFoundException {
		diSys.Common.Logger.TraceInformation("Update Process started to version "+item.version);
		File dir=new File(getVerDir(item.version));
		dir.mkdir();
		
		FileManager.WriteFile(getLastVerFile(item.version), item.jf);
		
		FileManager.WriteFile(classNameFile(item.version),item.className.getBytes());
		
		FileManager.WriteFile(VersionFile,item.version.toString().getBytes());
		diSys.Common.Logger.TraceInformation("Update data saved successfuly to dir "+dir.getName());
		
		this.Exit();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  String command="cmd /c start java -jar executer.jar 1 3000 3001";
		Runtime rt = Runtime.getRuntime();
        try {
			rt.exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
        diSys.Common.Logger.TraceInformation("Rerunning ..."+command);
        System.runFinalization();
        diSys.Common.Logger.TerminateSystem("Done ...", null);
		
	}
	
	@SuppressWarnings("unchecked")
	public IExecutor LoadUpdates() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{
	    String versionString=FileManager.ReadLine(VersionFile);
	    diSys.Common.Logger.TraceInformation("Loading updates Version "+versionString);
	    int version=Integer.parseInt(versionString);
	    File f = new File(getLastVerFile(version));
	    String executerClassName=FileManager.ReadLine(classNameFile(version));
		JarClassLoader jcl = new JarClassLoader(f);
		jcl.AddUrlToSystem(f.toURI().toURL());
		diSys.Common.Logger.TraceInformation("New Class " + executerClassName + " will be dynamically loaded...");
		
		IExecutor newExecuter=(IExecutor)jcl.loadClass(executerClassName).newInstance();
		this.Version=version;
		// BufferedReader in = new BufferedReader(new FileReader("foo.in"));
		return newExecuter;
	}

	public int GetVersion() {
		// TODO Auto-generated method stub
		return this.Version;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	
	public static void main(String[] args) throws InterruptedException, IOException {
		PrintUsage();
		if(args.length<3){
			System.out.println("Bad args ...!");
			return; 
		}
		String sysmAddress=null;
		int sysmPort=0;
		int workers=Integer.parseInt(args[0]);
		int irport=Integer.parseInt(args[1]);
		int rcport=Integer.parseInt(args[2]);
		
		if(args.length>3){
			sysmAddress=args[3];
			sysmPort=Integer.parseInt(args[4]);
			}
		ExecuterSystem es=new ExecuterSystem(null, workers,irport,rcport,sysmAddress,sysmPort); 
		System.out.println("Executer Started !");
		es.Run(args);
		System.in.read();
		es.Exit();
		System.out.println("executer Done");
	}
	public static void PrintUsage(){
		System.out.println("-------------------------------[Executer]----------------------------------");
		System.out.println("parameters:[worker Threads] [Irport] [Rcport] [System Manager Address] [System Manager Port]");
		System.out.println("[worker Threads] = the number of worker threads default 3");
		System.out.println("[Item Receiver Port]       = the port to listen foe incoming tasks");
		System.out.println("[Result Collector Port]    = the port to listen for result collectors");
		System.out.println("[System Manager Address]   = optional to notify the system Manager");
		System.out.println("[System Manager Port]      = optional to notify the system Manager");
		System.out.println("example : java -jar executer 3 3000 3001");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println();
	}
	
}
