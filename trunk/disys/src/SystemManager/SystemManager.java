package SystemManager;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

import Common.ClientRemoteInfo;
import Common.ExecuterRemoteInfo;
import Common.Item;
import Common.RMIRemoteInfo;
import Networking.IClientRemoteObject;
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
	private ConcurrentHashMap<ClientRemoteInfo, ClientBox> clientsMap=
		new ConcurrentHashMap<ClientRemoteInfo, ClientBox>();
	//the next id to assign to Client
	private int nextId = 0;
	
	//Component to check if Executers Still alive
	private HeartBeatChecker<TASK, RESULT> checker;
	
	public SystemManager() throws Exception {
		super(GlobalID);
		checker=new HeartBeatChecker<TASK, RESULT>(executersMap,200);
		checker.start();
	}
	@Override
	public ExecuterRemoteInfo Schedule(int numberOfTask) throws RemoteException {
		if (executersMap.keySet().isEmpty()) return null;
		Common.Logger.TraceInformation(this.GetClientHost() + " Whant to Execute "
					+ numberOfTask + " Tasks");
		
		ExecuterRemoteInfo remoteInfo=ScheduleExecuter(numberOfTask);
		Common.Logger.TraceInformation("Scheduling executer:" + remoteInfo.toString());
		return remoteInfo;
	}

	@Override
	public void addExecuter(int itemRecieverPort, int resultCollectorPort)
			throws RemoteException {
		String address = this.GetClientHost();
		if(address==null){
		Common.Logger.TraceError("Can't add executer , address couldn't be resolved!",null);
		return;
		}
		ExecuterRemoteInfo remoteInfo = 
			new ExecuterRemoteInfo(address,itemRecieverPort, resultCollectorPort);
		
		IRemoteItemReceiver<TASK> ir=
			NetworkCommon.loadRMIRemoteObject(remoteInfo.getItemRecieverInfo());
		IItemCollector<RESULT> rc=
			NetworkCommon.loadRMIRemoteObject(remoteInfo.getResultCollectorInfo());
		if(ir==null||rc==null){
			Common.Logger.TraceError("Can't add executer, Couldn't Connect to RMI Objects",null);
			return;
		}
		executersMap.put(remoteInfo,new ExecuterBox<TASK, RESULT>(ir,rc,false));
		Common.Logger.TraceInformation("added:"+ remoteInfo.toString());
	}

	@Override
	public ClientRemoteInfo AssignClientRemoteInfo(int port, String ID) throws RemoteException {
		String address = this.GetClientHost();
		if(address==null){
		Common.Logger.TraceError("Can't Create Client RemoteInfo , address couldn't be resolved!",null);
		return null;
		}
		ClientRemoteInfo remoteInfo = new ClientRemoteInfo(address, GetNextId());
		if(!clientsMap.containsKey(remoteInfo))
		{
			RMIRemoteInfo riRMI =new RMIRemoteInfo(this.GetClientHost(), port, ID);
			IClientRemoteObject cro = NetworkCommon.loadRMIRemoteObject(riRMI);
			clientsMap.put(remoteInfo, new ClientBox(cro));
		}
		return remoteInfo;
	}

	private long GetNextId() {
		if (nextId > MAX_ID)
			nextId = 0;
		return nextId++;
	}
	
	private ExecuterRemoteInfo ScheduleExecuter(int numberOfTasks){
		ExecuterRemoteInfo remoteInfo = null;
		int minNumOfTasks = Integer.MAX_VALUE;
		
		for (ExecuterRemoteInfo ri : executersMap.keySet()) 
		{
			ExecuterBox<TASK, RESULT> eb = executersMap.get(ri);
			if(eb.Blocked)
				continue;
			if(eb.getNumOfTasks() < minNumOfTasks)
			{
				remoteInfo = ri;
				minNumOfTasks = eb.getNumOfTasks(); 
			}
		}
		return remoteInfo;
	}
	
	@SuppressWarnings("unchecked")
	public String CleanExit() throws RemoteException{
		for (ClientRemoteInfo ri  : clientsMap.keySet())
		{
			ClientBox cb = clientsMap.get(ri);
			try{
				if(!cb.isIdle())
					return "Some clients still have undone tasks...";
			}
			catch(Exception e)
			{
				Common.Logger.TraceWarning("Client " + ri.toString() + " has been removed from the system", e);
				clientsMap.remove(ri);
			}
		}
		String s = ""; 
		CleanExitTask ceTask = new CleanExitTask();
		for (ExecuterRemoteInfo ri  : executersMap.keySet())
		{
			ExecuterBox<TASK, RESULT> eb = executersMap.get(ri);
			try{
				eb.getItemReciever().Add((TASK)ceTask);
			}
			catch(Exception e)
			{
				s += "Executer " + ri.getItemRecieverInfo().toString()+ " didn't response";
			}
		}
		return "Clean Exit done. " + s;
	}
}
