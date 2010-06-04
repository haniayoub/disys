package diSys.Client;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;

import diSys.Common.Chunk;
import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;
import diSys.Common.RMIRemoteInfo;
import diSys.Networking.IRemoteItemReceiver;
import diSys.Networking.NetworkCommon;
import diSys.SystemManager.ISystemManager;
import diSys.WorkersSystem.WorkER.AWorker;

/**
 * Connect to System manager to schedule chunks for execution
 * 
 * @author saeed
 * 
 * @param <TASK>
 * @param <RESULT>
 */
public class ChunkScheduler<TASK extends Item, RESULT extends Item> extends
		AWorker<Chunk<TASK>, Chunk<TASK>> {
	private ISystemManager<TASK> sysManager;
	private FailureDetector failureDetector;
	// result collector to update
	private ResultCollector<TASK, RESULT> resultCollector;

	public ChunkScheduler(ISystemManager<TASK> sysManager,
			ResultCollector<TASK, RESULT> resultCollector,
			BlockingQueue<Chunk<TASK>> taskChunks,
			BlockingQueue<Chunk<TASK>> taskChunks2, FailureDetector failureDetector) {
		super(taskChunks, taskChunks2);
		this.sysManager = sysManager;
		this.resultCollector = resultCollector;
		this.failureDetector = failureDetector;
	}

	@Override
	public Chunk<TASK> doItem(Chunk<TASK> task) {
		ExecuterRemoteInfo executerAddress = null;
		try {
			executerAddress = sysManager.Schedule(task.numberOfItems());

		} catch (RemoteException e) {
			diSys.Common.Logger.TraceWarning("Failed to schdule a Chunk", e);
			return task;
		}
		if (executerAddress == null) {
			diSys.Common.Logger.TraceWarning(
					"Failed to schdule a Chunk : No executers Found", null);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return task;
		}
		RMIRemoteInfo itemRecieverInfo = executerAddress.getItemRecieverInfo();
		IRemoteItemReceiver<Chunk<TASK>> RemoteExecuter = NetworkCommon
				.loadRMIRemoteObject(itemRecieverInfo);
		if (RemoteExecuter == null) {
			diSys.Common.Logger.TraceWarning(
					"Failed to schdule a Chunk :Connection Failed", null);
			return task;
		}

		try {
			RemoteExecuter.Add(task);
			TASK[] ts = task.getItems();
			for(TASK t : ts)
				failureDetector.add(t, executerAddress);

		} catch (RemoteException e) {
			diSys.Common.Logger.TraceWarning(
					"Couldn't add Chunk to Remote Receiver:"
							+ itemRecieverInfo.GetRmiAddress(), e);
			return task;
		}
		// Common.Logger.TraceInformation("Chunk [size:"+task.numberOfItems()+"] Sent to Remote Receiver:"+itemRecieverInfo.GetRmiAddress());
		resultCollector.WaitForResults(
				executerAddress.getResultCollectorInfo(), task);
		return null;
	}

}
