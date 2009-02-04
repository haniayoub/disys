package diSys.SystemManager;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import diSys.Common.Chunk;
import diSys.Common.ClientRemoteInfo;
import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;
import diSys.Common.RMIRemoteInfo;
import diSys.Common.SystemUpdates;
import diSys.Networking.IClientRemoteObject;
import diSys.Networking.IItemCollector;
import diSys.Networking.IRemoteItemReceiver;
import diSys.Networking.NetworkCommon;
import diSys.Networking.RMIObjectBase;


@SuppressWarnings("serial")
public class SystemManager<TASK extends Item,RESULT extends Item> extends RMIObjectBase
		implements ISystemManager<TASK> {
	//RMI object Id
	@SuppressWarnings("unused")
	
	public static final String GlobalID = "SystemManager";
	public static final String UpdateDir = "UpdateJars";

	public static final String ExecutersList = "executersList";
	//Max ID to assign to Clients
	private static final int MAX_ID = 10000;
	//executers In this System Manager
	private ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> executersMap=
		new ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK,RESULT>>();
	private ConcurrentHashMap<ClientRemoteInfo, ClientBox> clientsMap=
		new ConcurrentHashMap<ClientRemoteInfo, ClientBox>();
	private ConcurrentLinkedQueue<ExecuterRemoteInfo> blackList =
		new ConcurrentLinkedQueue<ExecuterRemoteInfo>();
	//the next id to assign to Client
	private int nextId = 0;
	private VersionManager versionManager=new VersionManager(UpdateDir,5);
	private ExecutersList executersFile=new ExecutersList(ExecutersList);
	//Component to check if Executers Still alive
	private HeartBeatChecker<TASK, RESULT> checker;
	
	public SystemManager(int port,int checkInterval) throws Exception {
		super(GlobalID,port);
		 Initialize(port,checkInterval);
	}
	public void Initialize(int port,int checkInterval) throws IOException{
		diSys.Common.Logger.TraceInformation("System Manager is Running on port "+this.getPort() +" Heartbeat Check Interval "+checkInterval);
		diSys.Common.Logger.TraceInformation("RMI ID:"+this.getRmiID());
		checker=new HeartBeatChecker<TASK, RESULT>(this,executersMap,blackList,clientsMap,checkInterval);
		checker.start();
		File f=new File(UpdateDir);
		f.mkdir();
		File exListfile=new File(ExecutersList);
		diSys.Common.Logger.TraceInformation("System Last Version "+versionManager.getLastVersion());
		if(!exListfile.exists()){
			exListfile.createNewFile();
		}else{
			LinkedList<ExecuterRemoteInfo> executersArr=executersFile.LoadExecutersList();
			for(ExecuterRemoteInfo ex:executersArr) blackList.add(ex);//addExecuter(ex);
		}	
	}
	@Override
	public ExecuterRemoteInfo Schedule(int numberOfTask) throws RemoteException {
		if (executersMap.keySet().isEmpty()) return null;
		diSys.Common.Logger.TraceInformation(this.GetClientHost() + " Whant to Execute "
					+ numberOfTask + " Tasks");
		
		ExecuterRemoteInfo remoteInfo=ScheduleExecuter(numberOfTask);
		diSys.Common.Logger.TraceInformation("Scheduling executer:" + remoteInfo.toString());
		return remoteInfo;
	}

	
	@Override
	public void addExecuter(int itemRecieverPort, int resultCollectorPort)
			throws RemoteException {
		String address = this.GetClientHost();
		if(address==null){
		diSys.Common.Logger.TraceError("Can't add executer , address couldn't be resolved!",null);
		return;
		}
		addExecuter(address,itemRecieverPort,resultCollectorPort);
		executersFile.addExecuterToFile(address,itemRecieverPort,resultCollectorPort);
	}
	/*
	private void addExecuter(ExecuterRemoteInfo er) throws RemoteException{
		
		addExecuter(er.getItemRecieverInfo().Ip(),er.getItemRecieverInfo().Port(),er.getResultCollectorInfo().Port());
	}*/

	public void addExecuter(String address, int irPort, int rcPort) throws RemoteException {
		diSys.Common.Logger.TraceInformation("Adding New Executer : "+address+" "+irPort+ " "+rcPort);
		ExecuterRemoteInfo remoteInfo = 
			new ExecuterRemoteInfo(address,irPort, rcPort);
		IRemoteItemReceiver<TASK> ir=
			NetworkCommon.loadRMIRemoteObject(remoteInfo.getItemRecieverInfo());
		IItemCollector<RESULT> rc=
			NetworkCommon.loadRMIRemoteObject(remoteInfo.getResultCollectorInfo());
		if(ir==null||rc==null){
			diSys.Common.Logger.TraceError("executer is offline",null);
			return;
		}
		
		executersMap.put(remoteInfo,new ExecuterBox<TASK, RESULT>(remoteInfo,ir,rc,false));
		this.updateExecuter(remoteInfo);
		diSys.Common.Logger.TraceInformation("added:"+ remoteInfo.toString());
		
	}
	@Override
	public ClientRemoteInfo AssignClientRemoteInfo(int port, String ID) throws RemoteException {
		String address = this.GetClientHost();
		if(address==null){
		diSys.Common.Logger.TraceError("Can't Create Client RemoteInfo , address couldn't be resolved!",null);
		return null;
		}
		ClientRemoteInfo remoteInfo = new ClientRemoteInfo(address, GetNextId());
		if(!clientsMap.containsKey(remoteInfo))
		{
			RMIRemoteInfo riRMI =new RMIRemoteInfo(this.GetClientHost(), port, ID);
			IClientRemoteObject cro = NetworkCommon.loadRMIRemoteObject(riRMI);
			clientsMap.put(remoteInfo, new ClientBox(cro));
			diSys.Common.Logger.TraceInformation("Client " + riRMI.toString() + " has been added to the system");
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
				diSys.Common.Logger.TraceWarning("Client " + ri.toString() + " has been removed from the system", e);
				clientsMap.remove(ri);
			}
		}
		String s = ""; 
		CleanExitTask ceTask = new CleanExitTask();
		for (ExecuterRemoteInfo ri  : executersMap.keySet())
		{
			ExecuterBox<TASK, RESULT> eb = executersMap.get(ri);
			try{
				Chunk<Item> c = new Chunk<Item>(-1, null, null, new Item[]{ceTask});
				eb.getItemReciever().Add((TASK)c);
			}
			catch(Exception e)
			{
				s += "Executer " + ri.getItemRecieverInfo().toString()+ " didn't response";
			}
		}
		return "Clean Exit done. " + s;
	}
	
	@SuppressWarnings({ "unchecked", "unchecked" })
	synchronized public String Update(SystemUpdates updates,boolean force) throws RemoteException
	{
		if(this.clientsMap.size()>1&&!force) {
			throw new RemoteException("Can't Update While There is Another Clients Connected to the system !");
			//diSys.Common.Logger.TraceWarning("Can't Update While There is Clients Connected to the system !", null);
			//return "Can't Update While There is Clients Connected to the system !";
		}
		String s="";
		s+=versionManager.addVersion(updates.UpdateJar(), updates.ExecuterClassName());
		
		LinkedList<ExecuterRemoteInfo> toREmove=new LinkedList<ExecuterRemoteInfo>();
		AutoUpdateTask auTask =new AutoUpdateTask(updates.UpdateJar(),versionManager.getLastVersion().version,updates.ExecuterClassName());
		for (ExecuterRemoteInfo ri  : executersMap.keySet())
		{
			executersMap.get(ri).Blocked=true;
			
			updateExecuter(ri,auTask);
			toREmove.add(ri);
		}
		for(ExecuterRemoteInfo ri:toREmove) this.MoveToBlackList(ri);
		
		s+="Update operation has been sent to executers";
		return s;
	}
	
	private void MoveToBlackList(ExecuterRemoteInfo ri){
		blackList.add(ri);
		executersMap.remove(ri);
	}
	@SuppressWarnings({ "unchecked", "unused" })
	public void updateExecuter(ExecuterRemoteInfo ri) throws RemoteException{
		AutoUpdateTask auTask=null;
		Version last=versionManager.getLastVersion();
		if(last.jar==null||last.className==null){
			diSys.Common.Logger.TraceError("System Manager has no updates for executers",null);
			return;
		}
		auTask = new AutoUpdateTask(last.jar,last.version,last.className);
		updateExecuter(ri);
		this.MoveToBlackList(ri);
		
	}
	
	@SuppressWarnings("unchecked")
	private void updateExecuter(ExecuterRemoteInfo ri,AutoUpdateTask updateTask) throws RemoteException{
		diSys.Common.Logger.TraceInformation("Sendig Update Task to Executer "+ri.getItemRecieverInfo().toString() );
		Chunk<Item> c = new Chunk<Item>(-2, null, null, new Item[]{updateTask});
		ExecuterBox<TASK, RESULT> eb = executersMap.get(ri);
	    eb.getItemReciever().Add((TASK)c);
	}
	
	@SuppressWarnings("unchecked")
	public void updateExecuter(ExecuterBox<TASK, RESULT> eb) throws RemoteException{
		AutoUpdateTask auTask=null;
		Version last=versionManager.getLastVersion();
		if(last.jar==null||last.className==null){
			diSys.Common.Logger.TraceError("System Manager has no updates for executers",null);
			return;
		}
		auTask = new AutoUpdateTask(last.jar,last.version,last.className);
		diSys.Common.Logger.TraceInformation("Sendig Update Task to Executer "+eb.getRemoteInfo().getItemRecieverInfo().toString() );
		Chunk<Item> c = new Chunk<Item>(-2, null, null, new Item[]{auTask});
	    eb.getItemReciever().Add((TASK)c);
		
	}
	public int GetLastVersionNumber() {
		return versionManager.getLastVersion().version;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException, IOException {
		int port=0;
		int interval=300;
		PrintUsage();
		if(args.length>0){
			port=Integer.parseInt(args[0]);
		}
		if(args.length>1){
			interval=Integer.parseInt(args[1]);
		}
		try {
			new SystemManager(port,interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		diSys.Common.Logger.TraceInformation("SystemManager is online");
		System.in.read();
		diSys.Common.Logger.TraceInformation("SystemManager Stopped!");
	}
	public static void PrintUsage(){
		System.out.println("-------------------------------[System Manager]--------------------------");
		System.out.println("[SysremManager] [port] [Interval]");
		System.out.println("[SysremManager] = Command Line");
		System.out.println("[port]          = The Port to listen On , default 3000-30000");
		System.out.println("[Interval]      = Executers Heartbeat Check interval default 300 ms");
		System.out.println("--------------------------------------------------------------------------");
		System.out.println();
	}
	
}
