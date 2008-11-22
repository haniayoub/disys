package SystemManager;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import Common.ExecuterRemoteInfo;
import Common.Item;
import Executor.ExecuterRemoteData;

public class HeartBeatChecker<TASK extends Item, RESULT extends Item> {
	// executers In this System Manager
	private ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> executersMap;
	//private ConcurrentLinkedQueue<ExecuterRemoteInfo> executerInfoList;

	private Thread workerThread;
	private Worker myWorker;
	private int period = 100;
	int blackListCounter = 0;

	public class Worker implements Runnable {
		private boolean done = false;
		private ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> blackList =
			new ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK,RESULT>>();
		private int blackListCounter = 0;
		//check the heart beat of each executer 
		public void run() {
			while (!done) {
				LinkedList<ExecuterRemoteInfo> toDelete = new LinkedList<ExecuterRemoteInfo>();
				for (ExecuterRemoteInfo ri : executersMap.keySet()) {
					try {
						ExecuterRemoteData erd = (ExecuterRemoteData)executersMap.get(ri).getItemReciever().getExecuterData();
						if(erd == null)
							Common.Logger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
						executersMap.get(ri).setNumOfTasks(erd.numOfTasks);
						executersMap.get(ri).setLog(erd.log);
					} catch (RemoteException e) {
						Common.Logger.TraceWarning("executer is not Alive:"
								+ ri.toString() + " - Moved to Black List", null);
						toDelete.add(ri);
						blackList.put(ri, executersMap.get(ri));
					}
				}
				for (ExecuterRemoteInfo ri : toDelete) {
					Common.Logger.TraceInformation("Removing Executer :"
							+ ri.toString());
					executersMap.remove(ri);
				}
				blackListCounter++;
				if(blackListCounter%10 == 0)
					blackListCounter = 0;
					for (ExecuterRemoteInfo ri : blackList.keySet())
					{
						try {
							ExecuterRemoteData erd = (ExecuterRemoteData)blackList.get(ri).getItemReciever().getExecuterData();
							if(erd == null)
								Common.Logger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
							//executer responded, add it back to running executers list
							executersMap.put(ri, blackList.get(ri));
							blackList.remove(ri);
						} catch (RemoteException e) {
							Common.Logger.TraceWarning("executer is not Alive:"
									+ ri.toString() + " - Removed from Black List (forever)", null);
							blackList.remove(ri);
						}
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

	public HeartBeatChecker(
			ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> executersMap,
			int period) {
		super();
		this.executersMap = executersMap;
		myWorker = new Worker();
		workerThread = new Thread(myWorker);
		this.period = period;
	}

	public void start() {
		Common.Logger.TraceInformation("heartBeat Checker Started!");
		workerThread.start();
	}

	public void Stop() {
		myWorker.halt();
		try {
			workerThread.interrupt();
			workerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Common.Logger.TraceInformation("heartBeat Checker stoped working");
	}
}
