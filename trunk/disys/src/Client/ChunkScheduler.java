package Client;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;

import Common.Chunk;
import Common.ExecuterRemoteInfo;
import Common.Item;
import Common.RMIRemoteInfo;
import Networking.IRemoteItemReceiver;
import Networking.NetworkCommon;
import SystemManager.ISystemManager;
import WorkersSystem.WorkER.AWorker;
/**
 * Connect to System manager to schedule chunks for execution 
 * @author saeed
 *
 * @param <TASK>
 * @param <RESULT>
 */
public class ChunkScheduler<TASK extends Item,RESULT extends Item> extends AWorker<Chunk<TASK>,Chunk<TASK>>  {
	private ISystemManager<TASK> sysManager;
	//result collector to update
	private ResultCollector<TASK,RESULT> resultCollector;
	@SuppressWarnings("unchecked")
	public ChunkScheduler(ISystemManager<TASK> sysManager,ResultCollector<TASK,RESULT> resultCollector,BlockingQueue<Chunk<TASK>> taskChunks, BlockingQueue<Chunk<TASK>> taskChunks2) {
		super(taskChunks, taskChunks2);
		this.sysManager=sysManager;
		this.resultCollector=resultCollector;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Chunk<TASK> doItem(Chunk<TASK> task) {
		ExecuterRemoteInfo executerAddress=null;
		try {
		executerAddress=sysManager.Schedule(task.numberOfItems());
			
		} catch (RemoteException e) {
			Common.Logger.TraceWarning("Failed to schdule a Chunk", e);
			return task;
		}
		if(executerAddress==null){
			Common.Logger.TraceWarning("Failed to schdule a Chunk : No executers Found", null);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return task;
		}
		RMIRemoteInfo itemRecieverInfo=executerAddress.getItemRecieverInfo();
		IRemoteItemReceiver<Chunk<TASK>> RemoteExecuter=
			NetworkCommon.loadRMIRemoteObject(itemRecieverInfo);
		if(RemoteExecuter==null){
			Common.Logger.TraceWarning("Failed to schdule a Chunk :Connection Failed", null);
			return task;
		}
		
		try {
			RemoteExecuter.Add(task);
		
		} catch (RemoteException e) {
			Common.Logger.TraceWarning("Couldn't add Chunk to Remote Receiver:"+itemRecieverInfo.GetRmiAddress(), null);
			return task;
		}
		Common.Logger.TraceInformation("Chunk [size:"+task.numberOfItems()+"] Sent to Remote Receiver:"+itemRecieverInfo.GetRmiAddress());
		resultCollector.WaitForResults(executerAddress.getResultCollectorInfo(),task);
	    return null;
	}

}
