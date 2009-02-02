package diSys.SystemManager;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import diSys.Common.ClientRemoteInfo;
import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;
import diSys.Executor.ExecuterRemoteData;
import diSys.Networking.IItemCollector;
import diSys.Networking.IRemoteItemReceiver;
import diSys.Networking.NetworkCommon;


public class HeartBeatChecker<TASK extends Item, RESULT extends Item> {
	// executers In this System Manager
	private ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> executersMap;
	private ConcurrentLinkedQueue<ExecuterRemoteInfo> blackList ;
	private ConcurrentHashMap<ClientRemoteInfo, ClientBox> clientsMap;
	@SuppressWarnings({ "unused", "unchecked" })
	private SystemManager sysm;
	private Thread workerThread;
	private Worker myWorker;
	private int period = 100;
	int blackListCounter = 0;

	public class Worker implements Runnable {
		private boolean done = false;
		private LinkedList<ExecuterBox<TASK, RESULT>> toUpdate=new  LinkedList<ExecuterBox<TASK, RESULT>>();
		private int blackListCounter = 0;
		//check the heart beat of each executer 
		public void run() {
			while (!done) {
				HeartBeatClients();
				HeartBeatExecuters();
				CheckToUpdateList();
				CheckBLackList();
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
						diSys.Common.Logger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
					executersMap.get(ri).setNumOfTasks(erd.numOfTasks);
					executersMap.get(ri).setLog(erd.log);
				} catch (RemoteException e) {
					diSys.Common.Logger.TraceWarning("executer is not Alive:"
							+ ri.toString() + " - Moved to Black List", null);
					toDelete.add(ri);
					blackList.add(ri);
				}
			}
			for (ExecuterRemoteInfo ri : toDelete) {
				diSys.Common.Logger.TraceInformation("Removing Executer :"
						+ ri.toString());
				executersMap.remove(ri);
			}
		}
		private void HeartBeatClients(){
			LinkedList<ClientRemoteInfo> toDelete = new LinkedList<ClientRemoteInfo>();
			for (ClientRemoteInfo cri : clientsMap.keySet()) {
				try {
					clientsMap.get(cri).isIdle();
				} catch (RemoteException e) {
					toDelete.add(cri);
				}
			}
			for (ClientRemoteInfo cri : toDelete) {
				diSys.Common.Logger.TraceWarning("Client is not Alive:"
						+ cri.toString() + " - Removing ...", null);
				clientsMap.remove(cri);
			}
		}
		
		@SuppressWarnings("unchecked")
		private void CheckBLackList(){
		    for (ExecuterRemoteInfo ri : blackList)
			{
				try {
					IRemoteItemReceiver<TASK> ir=
						NetworkCommon.loadRMIRemoteObject(ri.getItemRecieverInfo());
					IItemCollector<RESULT> rc=
						NetworkCommon.loadRMIRemoteObject(ri.getResultCollectorInfo());
					if(ir==null||rc==null){
						diSys.Common.Logger.TraceWarning("executer is offline",null);
						return;
					}
					ExecuterBox<TASK, RESULT> exec=new ExecuterBox<TASK, RESULT>(ri,ir,rc,false);
					//executersMap.put(ri,new ExecuterBox<TASK, RESULT>(ir,rc,false));
					ExecuterRemoteData erd = (ExecuterRemoteData)exec.getItemReciever().getExecuterData();
					if(erd == null)
						{
						diSys.Common.Logger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
						continue;
						}
					if(erd.Version < sysm.GetLastVersionNumber()) {
						diSys.Common.Logger.TraceWarning("Executer "+ ri.toString()+"  is not up to Date version:"+erd.Version +" Updating to "+sysm.GetLastVersionNumber(),null);
						sysm.updateExecuter(exec);
						toUpdate.add(exec);
						continue;
					}
					diSys.Common.Logger.TraceInformation("Executer "+ ri.toString()+" is Online and up to Date version:"+erd.Version +" Removing from black List");
					//sysm.addExecuter(ri.getItemRecieverInfo().Ip(), ri.getItemRecieverInfo().Port(), ri.getResultCollectorInfo().Port());
					//executersMap.put(ri, blackList.get(ri));
					executersMap.put(ri,exec);
					blackList.remove(ri);
					toUpdate.remove(exec);
				} catch (RemoteException e) {
				diSys.Common.Logger.TraceWarning("***************executer is offline",null);{
					continue;}
				}
			}
		}
		private void CheckToUpdateList(){
			LinkedList<ExecuterBox<TASK, RESULT>> toDelete = new LinkedList<ExecuterBox<TASK, RESULT>>();
			for (ExecuterBox<TASK, RESULT> exec: toUpdate)
			{
				ExecuterRemoteData erd=null;
				try {
					erd = (ExecuterRemoteData)exec.getItemReciever().getExecuterData();
				} catch (RemoteException e) {
					toDelete.add(exec);
					continue;
				}
				ExecuterRemoteInfo ri=exec.getRemoteInfo();
				if(erd == null)
					{
					diSys.Common.Logger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
					toDelete.add(exec);
					continue;
					}
				if(erd.Version < sysm.GetLastVersionNumber()) {
					diSys.Common.Logger.TraceWarning("Executer "+ ri.toString()+"  is not up to Date version:"+erd.Version,null);
					continue;
				}
				diSys.Common.Logger.TraceInformation("Executer "+ ri.toString()+" is up to Date version:"+erd.Version +" Removing from black List");
				executersMap.put(ri,exec);
				blackList.remove(exec);
				toDelete.add(exec);
			}
			for (ExecuterBox<TASK, RESULT> ri : toDelete) {
				diSys.Common.Logger.TraceInformation("Removing Executer :"
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
			ConcurrentLinkedQueue<ExecuterRemoteInfo> blackList ,
			ConcurrentHashMap<ClientRemoteInfo, ClientBox> clientsMap,
			int period) {
		super();
		this.clientsMap=clientsMap;
		this.sysm=systemM;
		this.executersMap = executersMap;
		this.blackList=blackList;
		myWorker = new Worker();
		workerThread = new Thread(myWorker);
		this.period = period;
	}

	public void start() {
		diSys.Common.Logger.TraceInformation("heartBeat Checker Started!");
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
		diSys.Common.Logger.TraceInformation("heartBeat Checker stoped working");
	}
}
