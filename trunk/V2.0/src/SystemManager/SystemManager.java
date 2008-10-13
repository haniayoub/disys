package SystemManager;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import Common.ClientRemoteInfo;
import Common.ExecuterRemoteInfo;
import Common.Item;
import Networking.IItemCollector;
import Networking.IRemoteItemReceiver;
import Networking.NetworkCommon;
import Networking.RMIObjectBase;
@SuppressWarnings("serial")
public class SystemManager<TASK extends Item,RESULT extends Item> extends RMIObjectBase
		implements ISystemManager<TASK> {
	//RMI object Id
	public static final String GlobalID = "SystemManager";
	//Max ID to assign to Clients
	private static final int MAX_ID = 10000;
	//executers In this System Manager
	private ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> executersMap=
		new ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK,RESULT>>();
	private ConcurrentLinkedQueue<ExecuterRemoteInfo> executerInfoList = 
		new ConcurrentLinkedQueue<ExecuterRemoteInfo>();
	//the next id to assign to Client
	private int nextId = 0;
	//Round robin basis 
	private int nextExecuter = 0;
	//Component to check if Executers Still alive
	private HeartBeatChecker<TASK, RESULT> checker;
	
	public SystemManager() throws Exception {
		super(GlobalID);
		checker=new HeartBeatChecker<TASK, RESULT>(executersMap,executerInfoList,200);
		checker.start();
	}
	@Override
	public ExecuterRemoteInfo Schedule(int numberOfTask) throws RemoteException {
		if (executerInfoList.isEmpty()) return null;
		Common.Loger.TraceInformation(this.GetClientHost() + " Whant to Execute "
					+ numberOfTask + " Tasks");
		
		ExecuterRemoteInfo remoteInfo=ScheduleExecuter(numberOfTask);
		Common.Loger.TraceInformation("Scheduling executer:" + remoteInfo.toString());
		return remoteInfo;
	}

	@Override
	public void addExecuter(int itemRecieverPort, int resultCollectorPort)
			throws RemoteException {
		String address = this.GetClientHost();
		if(address==null){
		Common.Loger.TraceError("Can't add executer , address couldn't be resolved!",null);
		return;
		}
		ExecuterRemoteInfo remoteInfo = 
			new ExecuterRemoteInfo(address,itemRecieverPort, resultCollectorPort);
		
		IRemoteItemReceiver<TASK> ir=
			NetworkCommon.loadRMIRemoteObject(remoteInfo.getItemRecieverInfo());
		IItemCollector<RESULT> rc=
			NetworkCommon.loadRMIRemoteObject(remoteInfo.getResultCollectorInfo());
		if(ir==null||rc==null){
			Common.Loger.TraceError("Can't add executer, Couldn't Connect to RMI Objects",null);
			return;
		}
		executersMap.put(remoteInfo,new ExecuterBox<TASK, RESULT>(ir,rc,false));
		executerInfoList.add(remoteInfo);
		Common.Loger.TraceInformation("added:"+ remoteInfo.toString());
	}

	@Override
	public ClientRemoteInfo AssignClientRemoteInfo() throws RemoteException {
		String address = this.GetClientHost();
		if(address==null){
		Common.Loger.TraceError("Can't Create Client RemoteInfo , address couldn't be resolved!",null);
		return null;
		}
		ClientRemoteInfo remoteInfo = new ClientRemoteInfo(address, GetNextId());
		return remoteInfo;
	}

	private long GetNextId() {
		if (nextId > MAX_ID)
			nextId = 0;
		return nextId++;
	}
	
	//BUGBUG : modify this function to schedule task in the right way
	//BUGBUG :if all the executers are blocked the function will stuck in a recursive
	private ExecuterRemoteInfo ScheduleExecuter(int numberOfTasks){
		if (nextExecuter > executerInfoList.size() - 1)
			nextExecuter = 0; // scheduling is round robin basis
		ExecuterRemoteInfo remoteInfo = (ExecuterRemoteInfo) (executerInfoList.toArray()[nextExecuter++]);
		if(executersMap.get(remoteInfo).Blocked){
			return ScheduleExecuter(numberOfTasks);
		}
		return remoteInfo;
	}
}
