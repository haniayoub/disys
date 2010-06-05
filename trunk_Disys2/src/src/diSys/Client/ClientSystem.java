package diSys.Client;

import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import diSys.Common.Chunk;
import diSys.Common.ClientRemoteInfo;
import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;
import diSys.Common.RMIRemoteInfo;
import diSys.Common.SynchronizedCounter;
import diSys.Common.SystemUpdates;
import diSys.Networking.IClientRemoteObject;
import diSys.Networking.IRemoteItemReceiver;
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
public class ClientSystem<TASK extends Item, RESULT extends Item> extends
		RMIObjectBase implements IClientRemoteObject {
	private static final int INITIAL_PORT = 6661;
	private static final int MAX_PORT = 7000;
	private PriorityBlockingQueue<TASK> tasks = new PriorityBlockingQueue<TASK>(
			100, new Comparator<TASK>() {
				public int compare(TASK t1, TASK t2) {
					if(t1.getPriority() == t2.getPriority()) return 0;
					return t1.getPriority() > t2.getPriority() ? 1 : -1;
				}
			});
	private BlockingQueue<RESULT> results = new LinkedBlockingQueue<RESULT>();
	private BlockingQueue<Chunk<TASK>> taskChunks = new LinkedBlockingQueue<Chunk<TASK>>();

	private ClientRemoteInfo myRemoteInfo;
	private RMIRemoteInfo RemoteSysManagerInfo;

	private WorkerSystem ws = new WorkerSystem();

	private ChunkCreator<TASK> chunkCreatorWorker;
	private ChunkScheduler<TASK, RESULT> chunkScheduler;
	private ResultCollector<TASK, RESULT> resultCollector;
	private SynchronizedCounter sCounter;

	private ISystemManager<TASK> sysManager;
	
	private FailureDetector<TASK> failureDetector = new FailureDetector<TASK>(10000, tasks);

	private boolean forceUpdate;
	
	private Long waitforTaskID=new Long(-1); 
	private RESULT waitForResult;	
	private Object ResultMonitor=new Object();
	public static final String GlobalID = "Client";

	public ClientSystem(String SysManagerAddress, int sysManagerport,
			int chunkSize) throws Exception {
		super(GlobalID, INITIAL_PORT, MAX_PORT);
		System.setProperty("sun.rmi.transport.connectionTimeout", "1000");
		sCounter = new SynchronizedCounter(0);
		RemoteSysManagerInfo = new RMIRemoteInfo(SysManagerAddress,
				sysManagerport, SystemManager.GlobalID);
		ConnectToSystemManager(RemoteSysManagerInfo, null);
		chunkCreatorWorker = new ChunkCreator<TASK>(tasks, taskChunks,
				myRemoteInfo, chunkSize);
		resultCollector = new ResultCollector<TASK, RESULT>(myRemoteInfo.Id(),
				1000, this);
		
		chunkScheduler = new ChunkScheduler<TASK, RESULT>(sysManager,
				resultCollector, taskChunks, taskChunks, failureDetector);

		ws.add(chunkCreatorWorker, 1);
		ws.add(chunkScheduler, 1);
	}

	public ClientSystem(String SysManagerAddress, int sysManagerport,
			int chunkSize, SystemUpdates updates, boolean ForceUpdate)
			throws Exception {
		super(GlobalID, INITIAL_PORT, MAX_PORT);
		this.forceUpdate = ForceUpdate;
		sCounter = new SynchronizedCounter(0);
		RemoteSysManagerInfo = new RMIRemoteInfo(SysManagerAddress,
				sysManagerport, SystemManager.GlobalID);
		ConnectToSystemManager(RemoteSysManagerInfo, updates);
		chunkCreatorWorker = new ChunkCreator<TASK>(tasks, taskChunks,
				myRemoteInfo, chunkSize);
		resultCollector = new ResultCollector<TASK, RESULT>(myRemoteInfo.Id(),
				1000, this);
		chunkScheduler = new ChunkScheduler<TASK, RESULT>(sysManager,
				resultCollector, taskChunks, taskChunks, failureDetector);

		ws.add(chunkCreatorWorker, 1);
		ws.add(chunkScheduler, 1);
	}

	public void ConnectToSystemManager(RMIRemoteInfo systemManagerRemoteInfo,
			SystemUpdates updates) {

		sysManager = NetworkCommon.loadRMIRemoteObject(systemManagerRemoteInfo);
		if (sysManager == null) {
			diSys.Common.Logger.TerminateSystem(
					"Failed to connecet to Remote System Mnager:"
							+ systemManagerRemoteInfo.GetRmiAddress(), null);
		}
		diSys.Common.Logger
				.TraceInformation("conneceted to Remote System Mnager:"
						+ systemManagerRemoteInfo.GetRmiAddress());
		try {

			myRemoteInfo = sysManager.AssignClientRemoteInfo(this.getPort(),
					GlobalID);
		} catch (RemoteException e) {
			diSys.Common.Logger
					.TerminateSystem(
							"Remote System Mnager Failed to assign Remote ID to Client.",
							null);
		}

		if (myRemoteInfo == null)
			diSys.Common.Logger
					.TerminateSystem(
							"Remote System Failed Mnager to assign Remote ID to Client:null Client ID",
							null);
		diSys.Common.Logger.TraceInformation("My Remote Info is :"
				+ myRemoteInfo.toString());
		if (updates != null)
			try {
				diSys.Common.Logger
						.TraceInformation("Updating sysatem Manager ... ");
				String s = sysManager.Update(updates, forceUpdate);
				diSys.Common.Logger.TraceInformation("Update: " + s);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sysManager = NetworkCommon
						.loadRMIRemoteObject(systemManagerRemoteInfo);
				diSys.Common.Logger.TraceInformation("System is up to date!");
			} catch (RemoteException e) {

				e.printStackTrace();
			}
	}

	public void Start() {

		ws.startWork();
		resultCollector.start();
		failureDetector.start();
	}

	public void Stop() {
		ws.stopWork();
		resultCollector.Stop();
		failureDetector.Stop();
		this.Dispose();
	}

	public void AddTask(TASK task) {
		tasks.offer(task);
		sCounter.add(1);
	}
	@SuppressWarnings("unchecked")
	public void AddTask(TASK task, String IP, int irPort, int rcPort) {
		ExecuterRemoteInfo exri=new ExecuterRemoteInfo(IP, irPort, rcPort);
		IRemoteItemReceiver<Chunk<TASK>> RemoteExecuter = NetworkCommon
				.loadRMIRemoteObject(exri.getItemRecieverInfo());
		if (RemoteExecuter == null) {
			diSys.Common.Logger.TraceWarning(
					"Failed to schdule a Chunk :Connection Failed", null);
		}
		Chunk<TASK> c = (Chunk<TASK>) new Chunk<Item>(task.getId(), myRemoteInfo, exri.getItemRecieverInfo(), new Item[]{task});
		try {
			
			RemoteExecuter.Add(c);

		} catch (RemoteException e) {
			diSys.Common.Logger.TraceWarning(
					"Couldn't add Chunk to Remote Receiver:"
							+ exri.getItemRecieverInfo().GetRmiAddress(), e);
		}
		resultCollector.WaitForResults(exri.getResultCollectorInfo(), c);
		sCounter.add(1);
	}
	
	public void AddLast(TASK task, String IP, int irPort, int rcPort) {
		ExecuterRemoteInfo exri=new ExecuterRemoteInfo(IP, irPort, rcPort);
		IRemoteItemReceiver<Chunk<TASK>> RemoteExecuter = NetworkCommon
				.loadRMIRemoteObject(exri.getItemRecieverInfo());
		if (RemoteExecuter == null) {
			diSys.Common.Logger.TraceWarning(
					"Failed to schdule a Chunk :Connection Failed", null);
		}
		try {
			task.setPriority(RemoteExecuter.getMaxPriority()+1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.AddTask(task, IP, irPort, rcPort);
	}
	
	public void AddFirst(TASK task, String IP, int irPort, int rcPort) {
		ExecuterRemoteInfo exri=new ExecuterRemoteInfo(IP, irPort, rcPort);
		IRemoteItemReceiver<Chunk<TASK>> RemoteExecuter = NetworkCommon
				.loadRMIRemoteObject(exri.getItemRecieverInfo());
		if (RemoteExecuter == null) {
			diSys.Common.Logger.TraceWarning(
					"Failed to schdule a Chunk :Connection Failed", null);
		}
		try {
			task.setPriority(RemoteExecuter.getMinPriority()-1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.AddTask(task, IP, irPort, rcPort);
	}

	public void AddResult(RESULT result) {
		if(waitforTaskID==result.getId()) 
		{   //Waiting for this result
			synchronized(ResultMonitor){			
			waitforTaskID=new Long(-1);
			waitForResult=result;
			ResultMonitor.notify();
			}
			sCounter.substract(1);
			return;
		}
		results.add(result);
		sCounter.substract(1);
	}

	public RESULT Take() throws InterruptedException {
		return results.take(); 
	}
	
	public RESULT DoTask(TASK task) {
		
		synchronized(ResultMonitor){
			waitforTaskID=task.getId();
			waitForResult=null;
		}
		AddTask(task);
		while(true){
		//Waiting for result
		synchronized(ResultMonitor){
		try {
			ResultMonitor.wait();
		} catch (InterruptedException e) {
		}
		}
		if(waitForResult==null) continue;
		return waitForResult;
		}
	}

	public RESULT Poll(int timeOut, TimeUnit timeUnit)
			throws InterruptedException {
		return results.poll(timeOut, timeUnit);
	}

	public boolean isStable() {
		return (resultCollector.isIdle() && tasks.isEmpty());
	}

	@Override
	public boolean IsIdle() throws RemoteException {
		return sCounter.value() == 0;
	}

	@Override
	public void Alive() throws RemoteException {

	}
}
