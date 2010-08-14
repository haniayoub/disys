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
	@SuppressWarnings({ "unchecked" })
	private SystemManager sysm;
	private Thread workerThread;
	private Worker myWorker;
	private int period = 100;
	int blackListCounter = 0;

	public class Worker implements Runnable {
		private boolean done = false;
		//check the heart beat of each executer 
		public void run() {
			while (!done) {
				try{
			    synchronized(SystemManager.UpdateLock){
				HeartBeatClients();
				CheckBLackList();
				HeartBeatExecuters();
			    }
				}
				catch(Exception e){
				diSys.Common.Logger.TraceWarning("Ecxeption in heartbeat checker", e);
				}
				sleep(period);
			}
		}
		private void HeartBeatExecuters(){
			LinkedList<ExecuterRemoteInfo> toDelete = new LinkedList<ExecuterRemoteInfo>();
			for (ExecuterRemoteInfo ri : executersMap.keySet()) {
				try {
					ExecuterRemoteData erd = (ExecuterRemoteData)executersMap.get(ri).getItemReciever().getExecuterData();
					if(erd == null){
						diSys.Common.Logger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
						continue;
					}
					executersMap.get(ri).setNumOfTasks(erd.numOfTasks);
					executersMap.get(ri).setLog(erd.log);
					ri.setVersion(erd.Version);
					ri.BS = erd.BS;
					ri.EP = erd.EP;
					ri.BC = erd.BC;
					
					if (ri.EP > SystemManager.MAX_EP) {
						SystemManager.MAX_EP = ri.EP;
					}
					
					if(erd.Version < sysm.GetLastVersionNumber()) {
						diSys.Common.Logger.TraceWarning("Executer "+ ri.toString()+"  is not up to Date version:"+erd.Version +" neet to Update to "+sysm.GetLastVersionNumber(),null);
						executersMap.get(ri).Blocked=false;
						continue;
					}
					
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
			//Calc the Mac EP
			double tmp = 0;
			for (ExecuterRemoteInfo ri : executersMap.keySet())
				if (ri.EP > tmp)
					tmp = ri.EP;
			
			if(tmp != SystemManager.MAX_EP)
				diSys.Common.Logger.TraceInformation("MAX Executer Power = "+tmp);
			
			SystemManager.MAX_EP = tmp;
		}
		private void HeartBeatClients(){
			LinkedList<ClientRemoteInfo> toDelete = new LinkedList<ClientRemoteInfo>();
			for (ClientRemoteInfo cri : clientsMap.keySet()) {
				try {
					clientsMap.get(cri).isIdle();
				} catch (Exception e) {
					toDelete.add(cri);
				}
			}
			for (ClientRemoteInfo cri : toDelete) {
				diSys.Common.Logger.TraceWarning("Client is not Alive:"
						+ cri.toString() + " - Removing ...", null);
				clientsMap.remove(cri);
			}
		}
		
		private void CheckBLackList(){
			LinkedList<ExecuterRemoteInfo> toDelete = new LinkedList<ExecuterRemoteInfo>();
		    for (ExecuterRemoteInfo ri : blackList)
			{
				try {
					//diSys.Common.Logger.TraceInformation("Loading executer "+ri.getItemRecieverInfo().toString());
					IRemoteItemReceiver<TASK> ir=
						NetworkCommon.loadRMIRemoteObject(ri.getItemRecieverInfo());
					if(ir==null){
						diSys.Common.Logger.TraceWarning("executer is offline "+ri.getItemRecieverInfo().toString(),null);
						continue;
					}
					IItemCollector<RESULT> rc=
						NetworkCommon.loadRMIRemoteObject(ri.getResultCollectorInfo());
					if(rc==null){
						diSys.Common.Logger.TraceWarning("executer is offline "+ri.getItemRecieverInfo().toString(),null);
						continue;
					}
					ExecuterBox<TASK, RESULT> exec=new ExecuterBox<TASK, RESULT>(ri,ir,rc,false);
					ExecuterRemoteData erd = (ExecuterRemoteData)exec.getItemReciever().getExecuterData();
					if(erd == null)
						{
						diSys.Common.Logger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
						continue;
						}
					
					if(erd.Version < sysm.GetLastVersionNumber()) {
						diSys.Common.Logger.TraceWarning("Executer "+ ri.toString()+"  is not up to Date version:"+erd.Version +" Updating to "+sysm.GetLastVersionNumber(),null);
						
						 toDelete.add(ri);
						 exec.Blocked=false;
						 executersMap.put(ri, exec);
						continue;
				
					}
					if(erd.Version==0){
						diSys.Common.Logger.TraceWarning("Executer "+ ri.toString()+"  is not up to Date version:"+erd.Version +" System Manager has no updates: "+sysm.GetLastVersionNumber(),null);
						 //TODO:Update Executer explicitlly here !!
						 toDelete.add(ri);
						 exec.Blocked=false;
						 executersMap.put(ri, exec);
						continue;
					}
					diSys.Common.Logger.TraceInformation("Executer "+ ri.toString()+" is Online and up to Date version:"+erd.Version +" Removing from black List");
					executersMap.put(ri,exec);
					toDelete.add(ri);
				} catch (Exception e) {
					diSys.Common.Logger.TraceWarning("***************executer is offline",e);
					continue;
				}
			
			}
			for (ExecuterRemoteInfo ri : toDelete) {
				diSys.Common.Logger.TraceInformation("Removing Executer from black list:"
						+ ri.toString());
				blackList.remove(ri);
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
