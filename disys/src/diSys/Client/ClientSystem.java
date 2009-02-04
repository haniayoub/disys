package diSys.Client;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import diSys.Common.Chunk;
import diSys.Common.ClientRemoteInfo;
import diSys.Common.Item;
import diSys.Common.FileManager;
import diSys.Common.RMIRemoteInfo;
import diSys.Common.SynchronizedCounter;
import diSys.Common.SystemUpdates;
import diSys.Networking.IClientRemoteObject;
import diSys.Networking.NetworkCommon;
import diSys.Networking.RMIObjectBase;
import diSys.SystemManager.ISystemManager;
import diSys.SystemManager.SystemManager;
import diSys.WorkersSystem.WorkER.WorkerSystem;


/**
 * Client System : this is the full package of the Clients it includes all the
 * workers and threads in order to make a fully working Client
 * 
 * @author saeed
 * 
 * @param <TASK>
 *            TASKs Type to send for execution
 * @param <RESULT>
 *            Results Type to recieve
 */
@SuppressWarnings("serial")
public class ClientSystem<TASK extends Item, RESULT extends Item> extends RMIObjectBase implements IClientRemoteObject{
	
	private PriorityBlockingQueue<TASK> tasks = new PriorityBlockingQueue<TASK>(100, 
			new Comparator<TASK>() {
          		public int compare(TASK t1, TASK t2) {
          			return t1.getPriority() < t2.getPriority() ? -1 : 1; 
          		}
        	});
	private BlockingQueue<RESULT> results = new LinkedBlockingQueue<RESULT>();
	private BlockingQueue<Chunk<TASK>> taskChunks = new LinkedBlockingQueue<Chunk<TASK>>();
	
	private ClientRemoteInfo myRemoteInfo;
	private RMIRemoteInfo RemoteSysManagerInfo;

	private WorkerSystem ws = new WorkerSystem();
	
	private ChunkCreator<TASK> chunkCreatorWorker;
	private ChunkScheduler<TASK, RESULT> chunkScheduler;
	private ResultCollector<TASK,RESULT> resultCollector;
	private SynchronizedCounter sCounter;
	//private ItemPrinter<RESULT> resultPrinter = new ItemPrinter<RESULT>(results, null);
	
	private ISystemManager<TASK> sysManager;  //TODO: Change to be private...
	
//	private String updateJarPath;
//	private String executerClassName;
	SystemUpdates updates;
	private boolean forceUpdate;
	
	public static final String GlobalID = "Client";
	public ClientSystem(String SysManagerAddress, int sysManagerport,int chunkSize) throws Exception {
		super(GlobalID);
		sCounter=new SynchronizedCounter(0);
		RemoteSysManagerInfo = new RMIRemoteInfo(SysManagerAddress,
				sysManagerport, SystemManager.GlobalID);
		ConnectToSystemManager(RemoteSysManagerInfo);
		chunkCreatorWorker = new ChunkCreator<TASK>(tasks, taskChunks,
				myRemoteInfo, chunkSize);
		resultCollector = new ResultCollector<TASK,RESULT>(myRemoteInfo.Id(),1000,this);
		chunkScheduler = new ChunkScheduler<TASK, RESULT>(sysManager,
				resultCollector, taskChunks, taskChunks);

		ws.add(chunkCreatorWorker, 1);
		ws.add(chunkScheduler, 1);
	}
	public ClientSystem(String SysManagerAddress, int sysManagerport,int chunkSize,SystemUpdates updates,boolean ForceUpdate) throws Exception {
		super(GlobalID);
		this.updates=updates;
		this.forceUpdate=ForceUpdate;
		sCounter=new SynchronizedCounter(0);
		RemoteSysManagerInfo = new RMIRemoteInfo(SysManagerAddress,
				sysManagerport, SystemManager.GlobalID);
		ConnectToSystemManager(RemoteSysManagerInfo);
		chunkCreatorWorker = new ChunkCreator<TASK>(tasks, taskChunks,
				myRemoteInfo, chunkSize);
		resultCollector = new ResultCollector<TASK,RESULT>(myRemoteInfo.Id(),1000,this);
		chunkScheduler = new ChunkScheduler<TASK, RESULT>(sysManager,
				resultCollector, taskChunks, taskChunks);

		ws.add(chunkCreatorWorker, 1);
		ws.add(chunkScheduler, 1);
	}
	
	
	

	@SuppressWarnings("unchecked")
	public void ConnectToSystemManager(RMIRemoteInfo systemManagerRemoteInfo) {
	
		sysManager=NetworkCommon.loadRMIRemoteObject(systemManagerRemoteInfo);
		if(sysManager==null){
		diSys.Common.Logger.TerminateSystem("Failed to connecet to Remote System Mnager:"+
							systemManagerRemoteInfo.GetRmiAddress(), null);
		}
		diSys.Common.Logger.TraceInformation("conneceted to Remote System Mnager:"
				+ systemManagerRemoteInfo.GetRmiAddress());
		try {
			
			myRemoteInfo = sysManager.AssignClientRemoteInfo(this.getPort(),GlobalID);
		} catch (RemoteException e) {
			diSys.Common.Logger.TerminateSystem("Remote System Mnager Failed to assign Remote ID to Client.",null);
		}
		
		if (myRemoteInfo == null) 
			diSys.Common.Logger.TerminateSystem("Remote System Failed Mnager to assign Remote ID to Client:null Client ID",null);		
		diSys.Common.Logger.TraceInformation("My Remote Info is :" + myRemoteInfo.toString());

			try {
				diSys.Common.Logger.TraceInformation("Updating sysatem Manager ... ");
				String s=sysManager.Update(updates,forceUpdate);
				diSys.Common.Logger.TraceInformation("Update: "+s);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}


	public void Start() {
		
		ws.startWork();
		resultCollector.start();
	}

	public void Stop() {
		ws.stopWork();
		resultCollector.Stop();
		this.Dispose();
	}
	
	public void AddTask(TASK task){
		tasks.offer(task);
		sCounter.add(1);
	}
	public void AddResult(RESULT result){
		results.add(result);
		sCounter.substract(1);
	}
	
	public RESULT Take() throws InterruptedException{
		return results.take();
	}
	public RESULT Poll(int timeOut,TimeUnit timeUnit) throws InterruptedException{
		return	results.poll(timeOut,timeUnit);
	}
	
	public boolean isStable(){
	return (resultCollector.isIdle() && tasks.isEmpty());
	}
    
	@Override
	public boolean IsIdle() throws RemoteException {
		return sCounter.value()==0;
	}

	@Override
	public void Alive() throws RemoteException {
		
	}
}
