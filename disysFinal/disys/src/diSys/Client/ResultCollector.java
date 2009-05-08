package diSys.Client;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import diSys.Common.Chunk;
import diSys.Common.Item;
import diSys.Common.Logger;
import diSys.Common.RMIRemoteInfo;
import diSys.Networking.IItemCollector;
import diSys.Networking.NetworkCommon;


/**
 * Connects to executers used bye this Client and every [period] of time it try
 * to retrieve its results
 * 
 * @author saeed
 * 
 * @param <RESULT>
 */
public class ResultCollector<TASK extends Item, RESULT extends Item> {

	private ClientSystem<TASK, RESULT> system;
	private ConcurrentHashMap<RMIRemoteInfo, Integer> executers = new ConcurrentHashMap<RMIRemoteInfo, Integer>();
	private ConcurrentHashMap<RMIRemoteInfo, IItemCollector<RESULT>> executersRemoteCollectors = new ConcurrentHashMap<RMIRemoteInfo, IItemCollector<RESULT>>();

	// private ConcurrentHashMap<RMIRemoteInfo,LinkedList<TASK>>
	// executersTasks=new ConcurrentHashMap<RMIRemoteInfo,LinkedList<TASK>>();

	private Thread collectorThread;
	private Worker myWorker;

	public class Worker implements Runnable {
		long myID;
		int period;
		boolean done = false;

		public Worker(long myID, int period) {
			this.myID = myID;
			this.period = period;
		}

		public void run() {
			while (!done) {
				Set<RMIRemoteInfo> keySet = executersRemoteCollectors.keySet();
				for (RMIRemoteInfo ri : keySet) {
					Chunk<RESULT> resultChunk;
					try {
						if (executers.get(ri).intValue() <= 0) {
							if (executers.get(ri).intValue() < 0)
								diSys.Common.Logger.TraceWarning(
										"received results More than expected from:"
												+ ri.GetRmiAddress(), null);
							executers.remove(ri);
							executersRemoteCollectors.remove(ri);
							continue;
						}
						resultChunk = executersRemoteCollectors.get(ri)
								.Collect(myID);
					} catch (RemoteException e) {

						diSys.Common.Logger.TraceError(
								"error while collecting results chunk from:"
										+ ri.GetRmiAddress(), e);
						continue;
					}
					if (resultChunk == null) {
					//	Common.Logger
					//			.TraceInformation("no results available from:"
					//					+ ri.GetRmiAddress());
						continue;
					}
					for (RESULT r : resultChunk.getItems())
						system.AddResult(r);
				}
				sleep(period);
			}
		}

		private void sleep(final int period) {
			try {
				Thread.sleep(period);
			} catch (InterruptedException e1) {

			}
		}

		public void halt() {
			done = true;
		}
	}

	public ResultCollector(long myId, int period,
			ClientSystem<TASK, RESULT> system) {
		super();
		this.system = system;
		myWorker = new Worker(myId, period);
		collectorThread = new Thread(myWorker);
	}

	public void start() {
		Logger.TraceInformation("Result Collector Started!");
		collectorThread.start();
	}

	public void Stop() {
		myWorker.halt();
		try {
			collectorThread.interrupt();
			collectorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Logger.TraceInformation("result Collector worker stoped working");
	}

	public boolean isIdle() {
		return executersRemoteCollectors.isEmpty();
	}

	public void WaitForResults(RMIRemoteInfo ri, Chunk<TASK> chunk) {
		if (!executersRemoteCollectors.contains(ri)) {

			IItemCollector<RESULT> resultChunkCollector = NetworkCommon
					.loadRMIRemoteObject(ri);
			if (resultChunkCollector == null) {
				Logger.TraceError("Couldn't  Connect to Executer:"
						+ ri.GetRmiAddress(), null);
				return;
			}
			executersRemoteCollectors.put(ri, resultChunkCollector);
		}

		if (!executers.containsKey(ri))
			executers.put(ri, 0);
		executers.put(ri, chunk.numberOfItems() + executers.get(ri));
	
		// / Task Recovery support
	}
}
