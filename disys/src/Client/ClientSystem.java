package Client;

import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import Common.Chunk;
import Common.ClientRemoteInfo;
import Common.Item;
import Common.ItemPrinter;
import Common.RMIRemoteInfo;
import Networking.NetworkCommon;
import SystemManager.ISystemManager;
import SystemManager.SystemManager;
import WorkersSystem.WorkER.WorkerSystem;

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
public class ClientSystem<TASK extends Item, RESULT extends Item> {
	
	public PriorityBlockingQueue<TASK> tasks = new PriorityBlockingQueue<TASK>(100, 
			new Comparator<TASK>() {
          		public int compare(TASK t1, TASK t2) {
          			return t1.getPriority() < t2.getPriority() ? -1 : 1; 
          		}
        	});
	public  BlockingQueue<RESULT> results = new LinkedBlockingQueue<RESULT>();
	private BlockingQueue<Chunk<TASK>> taskChunks = new LinkedBlockingQueue<Chunk<TASK>>();
	
	private ClientRemoteInfo myRemoteInfo;
	private RMIRemoteInfo RemoteSysManagerInfo;

	private WorkerSystem ws = new WorkerSystem();
	
	private ChunkCreator<TASK> chunkCreatorWorker;
	private ChunkScheduler<TASK, RESULT> chunkScheduler;
	private ResultCollector<TASK,RESULT> resultCollector;
	private ItemPrinter<RESULT> resultPrinter = new ItemPrinter<RESULT>(results, null);

	private ISystemManager<TASK> sysManager;

	public ClientSystem(String SysManagerAddress, int sysManagerport,int chunkSize) {
		super();
		RemoteSysManagerInfo = new RMIRemoteInfo(SysManagerAddress,
				sysManagerport, SystemManager.GlobalID);
		ConnectToSystemManager(RemoteSysManagerInfo);
		chunkCreatorWorker = new ChunkCreator<TASK>(tasks, taskChunks,
				myRemoteInfo, chunkSize);
		resultCollector = new ResultCollector<TASK,RESULT>(myRemoteInfo.Id(),1000,
				results);
		chunkScheduler = new ChunkScheduler<TASK, RESULT>(sysManager,
				resultCollector, taskChunks, taskChunks);

		ws.add(chunkCreatorWorker, 1);
		ws.add(chunkScheduler, 1);
		ws.add(resultPrinter, 1);
	}

	@SuppressWarnings("unchecked")
	public void ConnectToSystemManager(RMIRemoteInfo systemManagerRemoteInfo) {
	
		sysManager=NetworkCommon.loadRMIRemoteObject(systemManagerRemoteInfo);
		if(sysManager==null){
		Common.Loger.TerminateSystem("Failed to connecet to Remote System Mnager:"+
							systemManagerRemoteInfo.GetRmiAddress(), null);
		}
		Common.Loger.TraceInformation("conneceted to Remote System Mnager:"
				+ systemManagerRemoteInfo.GetRmiAddress());
		
		try {
			myRemoteInfo = sysManager.AssignClientRemoteInfo();
		} catch (RemoteException e) {
			Common.Loger.TerminateSystem("Remote System Mnager Failed to assign Remote ID to Client.",null);
		}
		
		if (myRemoteInfo == null) 
			Common.Loger.TerminateSystem("Remote System Failed Mnager to assign Remote ID to Client:null Client ID",null);		
		
		Common.Loger.TraceInformation("My Remote Info is :" + myRemoteInfo.toString());
	}

	public void Start() {
		ws.startWork();
		resultCollector.start();
	}

	public void Stop() {
		ws.stopWork();
		resultCollector.Stop();
	}
	
	public boolean isStable(){
	return (resultCollector.isIdle() && tasks.isEmpty());
	}
}
