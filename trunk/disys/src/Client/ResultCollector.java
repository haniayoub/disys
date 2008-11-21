package Client;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import Common.Chunk;
import Common.Item;
import Common.Loger;
import Common.RMIRemoteInfo;
import Networking.IItemCollector;
import Networking.NetworkCommon;

/**
 * Connects to executers used bye this Client and every [period] of time it try to retrieve 
 * its results 
 * @author saeed
 *
 * @param <RESULT>
 */
public class ResultCollector<TASK extends Item,RESULT extends Item> {

	private BlockingQueue<RESULT> resultsQueue;	
	private ConcurrentHashMap<RMIRemoteInfo,Integer> executers=new ConcurrentHashMap<RMIRemoteInfo, Integer>();
	private ConcurrentHashMap<RMIRemoteInfo,IItemCollector<RESULT>> executersRemoteCollectors=
		new ConcurrentHashMap<RMIRemoteInfo,IItemCollector<RESULT>>();

	//private ConcurrentHashMap<RMIRemoteInfo,LinkedList<TASK>> executersTasks=new ConcurrentHashMap<RMIRemoteInfo,LinkedList<TASK>>();

	
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
				Set<RMIRemoteInfo> keySet=executersRemoteCollectors.keySet();
				for(RMIRemoteInfo ri:keySet){
				Chunk<RESULT> resultChunk;
				try {
					if(executers.get(ri).intValue()<=0) {
						if(executers.get(ri).intValue()<0)
							Common.Loger.TraceWarning("received results More than expected from:"+ri.GetRmiAddress(), null);
						executers.remove(ri);
						executersRemoteCollectors.remove(ri);
						continue;
					}
					resultChunk=executersRemoteCollectors.get(ri).Collect(myID);
				} catch (RemoteException e) {
					
					Common.Loger.TraceError("error while collecting results chunk from:"+ri.GetRmiAddress(), e);
					continue;
				}
				if (resultChunk == null) {
					Common.Loger.TraceInformation("no results available from:"+ri.GetRmiAddress());
					continue;
				}
				for (RESULT r : resultChunk.getItems())
					resultsQueue.add(r);
				Common.Loger.TraceInformation("new "+resultChunk.numberOfItems()+" results from:"+ri.GetRmiAddress());
				executers.put(ri,executers.get(ri)-resultChunk.numberOfItems());
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
		public void halt(){
			done=true;
		}
	}
	

	@SuppressWarnings("unchecked")
	public ResultCollector(long myId,int period,
			BlockingQueue<RESULT> resultsQueue) {
		super();
		
		myWorker=new Worker(myId,period);
		
		collectorThread = new Thread(myWorker);
		this.resultsQueue=resultsQueue;
	}
	public void start(){
		Loger.TraceInformation("Result Collector Started!");
		collectorThread.start();
	}
	public void Stop(){
		myWorker.halt();
		try {
			collectorThread.interrupt();
			collectorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Loger.TraceInformation("result Collector worker stoped working");
	}
	@SuppressWarnings("unchecked")
	public void WaitForResults(RMIRemoteInfo ri,Chunk<TASK> chunk){
	if(!executersRemoteCollectors.contains(ri)){
		
		IItemCollector<RESULT> resultChunkCollector=NetworkCommon.loadRMIRemoteObject(ri);
		if(resultChunkCollector==null){
			Loger.TraceError("Couldn't  Connect to Executer:"+ ri.GetRmiAddress(), null);
			return;
		}
			executersRemoteCollectors.put(ri, resultChunkCollector);
	}
	
	if(!executers.containsKey(ri)) executers.put(ri,0);
	executers.put(ri,chunk.numberOfItems()+executers.get(ri));
	Loger.TraceInformation("Waiting For "+chunk.numberOfItems()+" results from "+ri.GetRmiAddress());
	
	/// Task Recovery support
	/*
	if(!executersTasks.contains(ri))executersTasks.put(ri,new LinkedList<TASK>());
	for(TASK t:chunk.getItems()) executersTasks.get(ri).add(t);
	*/
	}
}
