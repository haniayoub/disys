package SystemManager;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import Common.ExecuterRemoteInfo;
import Common.Item;
import Executor.ExecuterRemoteData;

public class HeartBeatChecker<TASK extends Item, RESULT extends Item> {
	// executers In this System Manager
	private ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> executersMap;
	private ConcurrentLinkedQueue<ExecuterRemoteInfo> executerInfoList;

	private Thread workerThread;
	private Worker myWorker;
	private int period = 100;

	public class Worker implements Runnable {
		boolean done = false;
		//check the heart beat of each executer 
		public void run() {
			while (!done) {
				LinkedList<ExecuterRemoteInfo> blackList = new LinkedList<ExecuterRemoteInfo>();
				for (ExecuterRemoteInfo ri : executerInfoList) {
					try {
						//executersMap.get(ri).getItemReciever().Alive();
						//executersMap.get(ri).getResultCollector().Alive();
						ExecuterRemoteData erd = (ExecuterRemoteData)executersMap.get(ri).getItemReciever().getExecuterData();
						if(erd == null)
							Common.Loger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
						executersMap.get(ri).setNumOfTasks(erd.numOfTasks);
					} catch (RemoteException e) {
						Common.Loger.TraceWarning("executer is not Alive:"
								+ ri.toString(), null);
						blackList.add(ri);
					}
				}

				for (ExecuterRemoteInfo ri : blackList) {
					Common.Loger.TraceInformation("Removing Executer :"
							+ ri.toString());
					executersMap.remove(ri);
					executerInfoList.remove(ri);
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
			ConcurrentLinkedQueue<ExecuterRemoteInfo> executerInfoList,
			int period) {
		super();
		this.executersMap = executersMap;
		this.executerInfoList = executerInfoList;
		myWorker = new Worker();
		workerThread = new Thread(myWorker);
		this.period = period;
	}

	public void start() {
		Common.Loger.TraceInformation("heartBeat Checker Started!");
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
		Common.Loger.TraceInformation("heartBeat Checker stoped working");
	}
}
