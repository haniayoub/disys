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
	private ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> blackList ;
	
	@SuppressWarnings({ "unused", "unchecked" })
	private SystemManager sysm;
	private Thread workerThread;
	private Worker myWorker;
	private int period = 100;
	int blackListCounter = 0;

	public class Worker implements Runnable {
		private boolean done = false;
		private LinkedList<ExecuterRemoteInfo> toUpdate=new  LinkedList<ExecuterRemoteInfo>();
		private int blackListCounter = 0;
		//check the heart beat of each executer 
		public void run() {
			while (!done) {
				HeartBeatExecuters();
				CheckToUpdateList();
				blackListCounter++;
				if(blackListCounter%10 == 0){
					blackListCounter = 0;
					CheckBLackList();
				}
				sleep(period);
			}
		}
		private void HeartBeatExecuters(){
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
		}
		private void CheckBLackList(){
		    for (ExecuterRemoteInfo ri : blackList.keySet())
			{
				try {
					ExecuterRemoteData erd = (ExecuterRemoteData)blackList.get(ri).getItemReciever().getExecuterData();
					if(erd == null)
						{
						Common.Logger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
						continue;
						}
					if(erd.Version < sysm.GetLastVersion()) {
						Common.Logger.TraceWarning("Executer "+ ri.toString()+"  is not up to Date version:"+erd.Version +" Updating to "+sysm.GetLastVersion(),null);
						sysm.updateExecuter(ri);
						toUpdate.add(ri);
						continue;
					}
					Common.Logger.TraceInformation("Executer "+ ri.toString()+" is Online and up to Date version:"+erd.Version +" Removing from black List");
					executersMap.put(ri, blackList.get(ri));
					blackList.remove(ri);
				} catch (RemoteException e) {
					continue;
				}
			}
		}
		private void CheckToUpdateList(){
			LinkedList<ExecuterRemoteInfo> toDelete = new LinkedList<ExecuterRemoteInfo>();
			for (ExecuterRemoteInfo ri : toUpdate)
			{
				ExecuterRemoteData erd=null;
				try {
					erd = (ExecuterRemoteData)blackList.get(ri).getItemReciever().getExecuterData();
				} catch (RemoteException e) {
					toDelete.add(ri);
					continue;
				}
				if(erd == null)
					{
					Common.Logger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
					toDelete.add(ri);
					continue;
					}
				if(erd.Version < sysm.GetLastVersion()) {
					Common.Logger.TraceWarning("Executer "+ ri.toString()+"  is not up to Date version:"+erd.Version,null);
					continue;
				}
				Common.Logger.TraceInformation("Executer "+ ri.toString()+" is up to Date version:"+erd.Version +" Removing from black List");
				executersMap.put(ri, blackList.get(ri));
				blackList.remove(ri);
				toDelete.add(ri);
			}
			for (ExecuterRemoteInfo ri : toDelete) {
				Common.Logger.TraceInformation("Removing Executer :"
						+ ri.toString());
				toUpdate.remove(ri);
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

	@SuppressWarnings("unchecked")
	public HeartBeatChecker(
			SystemManager systemM,
			ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> executersMap,
			ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> blackList ,
			int period) {
		super();
		this.sysm=systemM;
		this.executersMap = executersMap;
		this.blackList=blackList;
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
