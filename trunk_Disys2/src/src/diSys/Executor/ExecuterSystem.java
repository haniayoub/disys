package diSys.Executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import diSys.AutoUpdate.JarClassLoader;
import diSys.Common.ATask;
import diSys.Common.Chunk;
import diSys.Common.FileLogger;
import diSys.Common.FileManager;
import diSys.Common.IExecutor;
import diSys.Common.Item;
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
class ExecuterSystem<TASK extends ATask<? extends Item>,RESULT extends Item,E extends IExecutor<TASK,RESULT>> {
	
	public static final String UpdateDir = "ExecuterUpdateJars";
	public static final String UpdateExtension = "jar";
	public static final String ClassNameExtension = "clsn";
	public static final String VersionFile = "Version.txt";
	public static final String HeartBeatLogFile = "HeartBeat.txt";
	public static final String ExecuterLogFile = "log.txt";
	//the chunks received from clients 
	private BlockingQueue<Chunk<TASK>> recievedChunks = 
		new LinkedBlockingQueue<Chunk<TASK>>();
	//tasks to perform
	private PriorityBlockingQueue<RemoteItem<TASK>> tasks = new PriorityBlockingQueue<RemoteItem<TASK>>(
			100, new Comparator<RemoteItem<TASK>>() {
				public int compare(RemoteItem<TASK> t1, RemoteItem<TASK> t2) {
					if(t1.getPriority() == t2.getPriority()) return 0;
					return t1.getPriority() > t2.getPriority() ? 1 : -1;
				}
			});
	private Item currentTask = null;

	//results Queue
	private BlockingQueue<RemoteItem<RESULT>> results= 
						new LinkedBlockingQueue<RemoteItem<RESULT>>();
	//Result organized in a Map of clients
	private ConcurrentHashMap<RemoteInfo,ConcurrentLinkedQueue<RESULT>> clientResults=
						new ConcurrentHashMap<RemoteInfo, ConcurrentLinkedQueue<RESULT>>();
	//
	//Workers of the executer System
	//
	
	public RemoteItemReceiver<Chunk<TASK>> chunkReceiver;
	private ChunkBreaker<TASK> chunkBreaker;
	@SuppressWarnings("unchecked")
	private TaskExecuter<TASK,RESULT,IExecutor> taskExecuter;
	private RemoteItemOrganizer<RESULT> resultOrganizer;
    private RMIItemCollector<RESULT> itemCollector; 
	//system manager reference
    private ISystemManager<TASK> sysManager;
    private RMIRemoteInfo systemManagerRemoteInfo;
    
    private BenchMarkTask benchMark;
    //number of task executers
    private int numerOfExecuters;
	private WorkerSystem ws=new WorkerSystem();
	private WorkerCollection ExecutersCollection;
	private int Version;
	private Thread heartBeat;
	private FileLogger fileLogger = new FileLogger(ExecuterLogFile);
	private String executerName;
	@SuppressWarnings("unchecked")
	public ExecuterSystem(IExecutor executer,int numerOfWorkers,int irport,int rcport,String SysManagerAddress,int sysManagerport, String executerName) throws InterruptedException {
		super();
		this.executerName = executerName;
		benchMark = new BenchMarkTask(5*60, this);
		benchMark.start();
		File f=new File(UpdateDir);
		f.mkdir();
		Thread.sleep(1000);
		//IExecutor newExec=executer;
		try {
			LoadUpdates();
		} catch (Exception e) {
			fileLogger.TraceWarning("Executer Is could not load Updates", e);
		}
		
		try {
			chunkReceiver=new RemoteItemReceiver(recievedChunks, tasks ,irport, this);
			chunkReceiver.Version=this.Version;
		} catch (Exception e) {
			fileLogger.TerminateSystem("Error intializing Remote Chunk Reciever", e);
		}
		
		try {
			itemCollector=new RMIItemCollector<RESULT>(clientResults,rcport);
		} catch (Exception e) {
			fileLogger.TerminateSystem("Error intializing Remote Chunk Reciever ", e);
		}
		
		fileLogger.TraceInformation("Starting Executer IRport: "+chunkReceiver.getPort()+" RCport:"+itemCollector.getPort()+" Workers:"+numerOfWorkers);

		if(SysManagerAddress!=null){
		//////////////////////////Fire Wall problematic !!!!
		systemManagerRemoteInfo=new RMIRemoteInfo(SysManagerAddress,sysManagerport,SystemManager.GlobalID);
		sysManager=NetworkCommon.loadRMIRemoteObject(systemManagerRemoteInfo);
		fileLogger.TraceInformation("Connecting to system Manager "+SysManagerAddress +" "+ sysManagerport);
		if(sysManager!=null){
		try {
			sysManager.addExecuter(chunkReceiver.getPort(),itemCollector.getPort());
		} catch (RemoteException e) {
			fileLogger.TraceWarning("Couldn't add Executer to System Manager", null);
		}
		}
		///////////////////////////////////////////////
		}
		
		chunkBreaker=new ChunkBreaker<TASK>(recievedChunks,tasks, this);
	
		String myID=chunkReceiver.getRmiID();
		taskExecuter = new TaskExecuter<TASK,RESULT,IExecutor>(myID,tasks,results, this);
		resultOrganizer=new RemoteItemOrganizer<RESULT>(results,clientResults);
		numerOfExecuters= numerOfWorkers;
		ExecutersCollection=new WorkerCollection(taskExecuter,numerOfExecuters,null);
		ws.add(chunkBreaker,1);
		ws.add(ExecutersCollection);
		ws.add(resultOrganizer,1);
		fileLogger.TraceInformation("Executer is running ...");
		heartBeat = new HeartBeat(HeartBeatLogFile);
		heartBeat.start();
	}
	
	public void Run(String[] args) {
		 ws.startWork();
	}
	
	public void Exit() {
		 ws.stopWork();
		 chunkReceiver.Dispose();
		 itemCollector.Dispose();
	}
	private String getLastVerFile(int UpdateVer){
		return getVerDir(UpdateVer)+"/Updates."+UpdateExtension;
	}
	
	private String classNameFile(int UpdateVer){
		return getVerDir(UpdateVer)+"/ClassName."+ClassNameExtension;
	}
	
	private String getVerDir(int UpdateVer){
		return "Ver"+UpdateVer;
	}
	
	@SuppressWarnings("unchecked")
	public RemoteItemReceiver GetItemReciver() {
	return this.chunkReceiver;
	}
	
	public void updateExecuters(AutoUpdateTask item) {
		fileLogger.TraceInformation("################### System Update ###############");
		File dir=new File(getVerDir(item.version));
		dir.mkdir();
		String fileName=getLastVerFile(item.version);
		String IncludeJarsDir=getUpdateJarsDir(item.version);
		File jarsf=new File(IncludeJarsDir);
		jarsf.mkdir();
		try {
		fileLogger.TraceInformation("Writing file "+fileName+"  "+item.updates.UpdateJar().length);
		FileManager.WriteFile(fileName, item.updates.UpdateJar());
		FileManager.WriteFile(classNameFile(item.version),item.updates.ExecuterClassName().getBytes());
		FileManager.WriteFile(VersionFile,item.version.toString().getBytes());
		
		for(String fname:item.updates.IncludeJars.keySet()){
			try {
				fileLogger.TraceInformation("Saving Include jar File:"+fname);
				FileManager.WriteFile(IncludeJarsDir+fname,item.updates.IncludeJars.get(fname));
			} catch (FileNotFoundException e) {
				fileLogger.TraceError("Include jar File:"+fname+"Not found to save",null);
			}
		}
		} catch (FileNotFoundException e) {
			fileLogger.TraceError("Update Failed : "+e.getMessage(), null);
			return;
		}
		this.ExecutersCollection.stopWorking();
		this.ExecutersCollection=null;
		this.Exit();
		File f = new File(fileName);
		try {
			JarClassLoader.AddUrlToSystem(f.toURI().toURL());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if((new File(executerName + ".exe")).exists())
		{
			diSys.Common.CommandLineRunner.Run("start " + executerName + ".exe -jar Executer.jar "+CommandLineArgs);
		}
		else
		{
			diSys.Common.CommandLineRunner.Run("start java -jar Executer.jar "+CommandLineArgs);
		}
		fileLogger.TerminateSystem("Terminate due to update", null);
		
		}
	private String getUpdateJarsDir(Integer version2) {
		return getVerDir(version2)+"/IncludeJars/";
	
	}

	@SuppressWarnings("unchecked")
	public void LoadUpdates(){
	    String versionString;
	   
	    fileLogger.TraceInformation("Loading updates!..");
	    try {
			versionString = FileManager.ReadLines(VersionFile)[0];
		} catch (Exception e) {
			fileLogger.TraceError("Couldn't read Version from version file!\n"+e.getMessage(),null);
			return ;//null;
		}
		fileLogger.TraceInformation("Loading updates Version "+versionString);
	    int version=Integer.parseInt(versionString);
	    
	    File fdel = new File(getVerDir(version-2));
	    if(fdel.exists()&&fdel.listFiles()!=null)
	    for(File file:fdel.listFiles()) file.delete();
	    fileLogger.TraceInformation("Deleteing dir "+fdel.getName()+" :"+ fdel.delete());
	    
	    File f = new File(getLastVerFile(version));
	    String executerClassName;
		try {
			executerClassName = FileManager.ReadLines(classNameFile(version))[0];
		} catch (Exception e) {
			fileLogger.TraceError("Couldn't Read IExecuter Class Name!\n"+e.getMessage(),null);
			return;// null;
		}
	   // jcl = new JarClassLoader(f);
	    
		//JarClassLoader.AddUrlToSystem(f.toURI().toURL());
		fileLogger.TraceInformation("New Class " + executerClassName + " will be dynamically loaded...");
		try {
			JarClassLoader.AddUrlToSystem(f.toURI().toURL());
		} catch (MalformedURLException e) {
			fileLogger.TraceError("Couldn't Update !\n"+e.getMessage(),null);
			return;// null;
		}
		String IncludeJarsDir=getUpdateJarsDir(version);
		File jarsf=new File(IncludeJarsDir);
         for(File ff:jarsf.listFiles())
			try {
				JarClassLoader.AddUrlToSystem(ff.toURI().toURL());
			} catch (MalformedURLException e) {
				fileLogger.TraceError("Couldn't Update !\n"+e.getMessage(), null);
				return;// null;
			}
			fileLogger.TraceInformation("Intializing class " +executerClassName);
		/*IExecutor newExecuter;
		try {
			newExecuter = (IExecutor)Class.forName(executerClassName).newInstance();
		} catch (Exception e) {
			fileLogger.TraceError("Couldn't Update Failed to initialize Executer Class!\n"+e.getMessage(), null);
			return null;
		}catch(NoClassDefFoundError e){
			fileLogger.TraceInformation("Couldn't Update Failed to initialize Executer Class!");
			return null;
		}*/
        	 
		this.Version=version;
		
		// BufferedReader in = new BufferedReader(new FileReader("foo.in"));
		//return newExecuter;
	}

	public int GetVersion() {
		// TODO Auto-generated method stub
		return this.Version;
	}
	private static String CommandLineArgs;

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
		String executerName = args[0];
		int irport=Integer.parseInt(args[1]);
		int rcport=Integer.parseInt(args[2]);
		
		if(args.length>3){
			sysmAddress=args[3];
			sysmPort=Integer.parseInt(args[4]);
			}
		CommandLineArgs="";
		for(String s:args)
		CommandLineArgs=CommandLineArgs+" "+s;
		ExecuterSystem es=new ExecuterSystem(null, 1,irport,rcport,sysmAddress,sysmPort, executerName); 
		System.out.println("Executer Started !");
		es.Run(args);
		while(true)
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			
			}
	}
	public static void PrintUsage(){
		System.out.println("-------------------------------[Executer]----------------------------------");
		System.out.println("parameters: [ExecuterName][Irport] [Rcport] [System Manager Address] [System Manager Port]");
		System.out.println("[Executer Name]            = the executer name");
		System.out.println("[Item Receiver Port]       = the port to listen foe incoming tasks");
		System.out.println("[Result Collector Port]    = the port to listen for result collectors");
		System.out.println("[System Manager Address]   = optional to notify the system Manager");
		System.out.println("[System Manager Port]      = optional to notify the system Manager");
		System.out.println("example : java -jar executer 3 3000 3001");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println();
	}

	public Item getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Item currentTask) {
		this.currentTask = currentTask;
	}
}
