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
import diSys.Common.Logger;
import diSys.Common.RMIRemoteInfo;
import diSys.Common.SystemUpdates;
import diSys.Executor.ExecuterRemoteData;
import diSys.Networking.IClientRemoteObject;
import diSys.Networking.IItemCollector;
import diSys.Networking.IRemoteItemReceiver;
import diSys.Networking.NetworkCommon;
import diSys.Networking.RMIObjectBase;
import diSys.Networking.Security;


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
	/*private ConcurrentLinkedQueue<ExecuterRemoteInfo> toUpdateList =
		new ConcurrentLinkedQueue<ExecuterRemoteInfo>();
	*/
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
		if (executersMap.isEmpty()){
			Logger.TraceInformation("Executers list is empty !!!");
			return null;
		}
		diSys.Common.Logger.TraceInformation(this.GetClientHost() + " Whant to Execute "
					+ numberOfTask + " Tasks");
		
		ExecuterRemoteInfo remoteInfo=ScheduleExecuter(numberOfTask);
		if(remoteInfo==null){
			diSys.Common.Logger.TraceInformation("No executers");
		}else{
		diSys.Common.Logger.TraceInformation("Scheduling executer:" + remoteInfo.toString());
		}
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
		/*IRemoteItemReceiver<TASK> ir=
			NetworkCommon.loadRMIRemoteObject(remoteInfo.getItemRecieverInfo());
		IItemCollector<RESULT> rc=
			NetworkCommon.loadRMIRemoteObject(remoteInfo.getResultCollectorInfo());
		if(ir==null||rc==null){
			diSys.Common.Logger.TraceError("executer is offline",null);
			return;
		}
		
		executersMap.put(remoteInfo,new ExecuterBox<TASK, RESULT>(remoteInfo,ir,rc,false));
		this.updateExecuter(remoteInfo);
		*/
		blackList.add(remoteInfo);
		diSys.Common.Logger.TraceInformation("added:"+ remoteInfo.toString());
		
	}
	@Override
	synchronized public ClientRemoteInfo AssignClientRemoteInfo(int port, String ID) throws RemoteException {
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
		
		if(executersMap.isEmpty()){
			Logger.TraceInformation("Executers list is empty !");
		}
		for (ExecuterRemoteInfo ri : executersMap.keySet()) 
		{
			ExecuterBox<TASK, RESULT> eb = executersMap.get(ri);
			if(eb.Blocked){
				Logger.TraceInformation(eb.getRemoteInfo()+" is Blocked !");
				continue;
			}
				
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
		s+=versionManager.addVersion(updates);
		
		//LinkedList<ExecuterRemoteInfo> toREmove=new LinkedList<ExecuterRemoteInfo>();
		LinkedList<ExecuterRemoteInfo> toUpdate=new LinkedList<ExecuterRemoteInfo>();
		//AutoUpdateTask auTask =new AutoUpdateTask(updates,versionManager.getLastVersion().version);
		for (ExecuterRemoteInfo ri  : executersMap.keySet())
		{
			executersMap.get(ri).Blocked=true;
			toUpdate.add(ri);
		//	updateExecuter(ri,auTask);
			//toREmove.add(ri);
		}
	/*
		for(ExecuterRemoteInfo ri:toREmove) {
			//this.MoveToBlackList(ri);
			toUpdate.add(executersMap.get(ri));
			//executersMap.remove(ri);
			//toUpdateList.add(ri);
		}
		*/
		//Wait untill system is up to date !!!
		/*while(!toUpdateList.isEmpty()){
			diSys.Common.Logger.TraceInformation("Waiting System to Update ...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}*/
		 startUpdate(toUpdate);
		
		
		diSys.Common.Logger.TraceInformation("System is up to date");
		s+="Update operation has been sent to executers";
		return s;
	}
	private void startUpdate(LinkedList<ExecuterRemoteInfo> toUpdate)
	{
		for (ExecuterRemoteInfo ri: toUpdate)
		{
			try {
				this.updateExecuter(ri);
			} catch (RemoteException e) {
				blackList.add(ri);
			}
		}
	}
		
	/*
	private void startUpdate(LinkedList<ExecuterBox<TASK, RESULT>> toUpdate)
	{
		for (ExecuterBox<TASK, RESULT> exec: toUpdate)
		{
			try {
				this.updateExecuter(exec);
			} catch (RemoteException e) {
			blackList.add(exec.getRemoteInfo());
			}
		}
		LinkedList<ExecuterRemoteInfo> updateList=new LinkedList<ExecuterRemoteInfo>();
		LinkedList<ExecuterRemoteInfo> badList=new LinkedList<ExecuterRemoteInfo>();
		LinkedList<ExecuterRemoteInfo> toDelete=new LinkedList<ExecuterRemoteInfo>();
		
		for (ExecuterBox<TASK, RESULT> exec: toUpdate) updateList.add(exec.getRemoteInfo());
		
		for (int timer=0 ;timer<10;timer++){
			if(updateList.isEmpty()){
				Logger.TraceInformation("Update System Completed Successfully ["+toDelete.size()+" executers]");
				break;
			}
			for(ExecuterRemoteInfo ri:updateList){
				try {
					Logger.TraceInformation("Checking "+ri.getItemRecieverInfo()+" Update Ststus");
					IRemoteItemReceiver<TASK> ir=
					NetworkCommon.loadRMIRemoteObjectNoLog(ri.getItemRecieverInfo());
					IItemCollector<RESULT> rc=
					NetworkCommon.loadRMIRemoteObjectNoLog(ri.getResultCollectorInfo());
					if(ir==null||rc==null){
					diSys.Common.Logger.TraceWarning(ri.getItemRecieverInfo()+ " is offline 1",null);
					badList.add(ri);
					continue;
					}
					ExecuterRemoteData erd;
					try {
						erd = (ExecuterRemoteData)ir.getExecuterData();
					} catch (RemoteException e) {
						diSys.Common.Logger.TraceWarning(ri.getItemRecieverInfo()+ " is offline 2",null);
						badList.add(ri);
						continue;
					}
					if(erd == null)
					{
						diSys.Common.Logger.TraceWarning("null executer data received " + ri.getItemRecieverInfo().RMIId(), null);
						badList.add(ri);
						continue;
					}
					if(erd.Version < GetLastVersionNumber()) {
						diSys.Common.Logger.TraceWarning("Executer "+ ri.toString()+"  is not up to Date version:"+erd.Version+" adding to bad list",null);
						badList.add(ri);
						continue;
					}
					diSys.Common.Logger.TraceInformation("Executer "+ ri.toString()+" is up to Date version:"+erd.Version +" Removing from bad List ");
					badList.remove(ri);
					toDelete.add(ri);
				}catch(Exception e){
					badList.add(ri);
					continue;
					}
			}
			for (ExecuterRemoteInfo riexe : toDelete) {
				diSys.Common.Logger.TraceInformation("Removing updated Executer from update list:"
				+ riexe.getItemRecieverInfo());
				updateList.remove(riexe);
				diSys.Common.Logger.TraceInformation("Returning executer to executers list"
						+ riexe.getItemRecieverInfo());
				IRemoteItemReceiver<TASK> ir=
				NetworkCommon.loadRMIRemoteObjectNoLog(riexe.getItemRecieverInfo());
				IItemCollector<RESULT> rc=
				NetworkCommon.loadRMIRemoteObjectNoLog(riexe.getResultCollectorInfo());
				if(ir==null||rc==null){
				diSys.Common.Logger.TraceWarning("executer is offline 5",null);
				blackList.add(riexe);
				continue;
				}
				executersMap.put(riexe, new ExecuterBox<TASK, RESULT>(riexe,ir,rc,false));
				diSys.Common.Logger.TraceInformation("executer is back"+ riexe.getItemRecieverInfo());
			}
			try {Thread.sleep(1000);} catch (InterruptedException e) {};
		}
		for (ExecuterRemoteInfo riexe : badList) {
			diSys.Common.Logger.TraceInformation("Removing bad Executers to black list:"
			+ riexe.getItemRecieverInfo());
			blackList.add(riexe);
			//updateList.remove(riexe);
		}
		
	}*/
	
	private void MoveToBlackList(ExecuterRemoteInfo ri){
		blackList.add(ri);
		executersMap.remove(ri);
	}
	@SuppressWarnings({ "unchecked", "unused" })
	public void updateExecuter(ExecuterRemoteInfo ri) throws RemoteException{
		AutoUpdateTask auTask=null;
		Version last=versionManager.getLastVersion();
		if(last==null){
			diSys.Common.Logger.TraceError("System Manager has no updates for executers",null);
			return;
		}
		auTask = new AutoUpdateTask(last.updates,last.version);
		updateExecuter(ri,auTask);
		this.MoveToBlackList(ri);
		
	}
	
	@SuppressWarnings("unchecked")
	private void updateExecuter(ExecuterRemoteInfo ri,AutoUpdateTask updateTask) throws RemoteException{
		diSys.Common.Logger.TraceInformation("Sendig Update Task to Executer "+ri.getItemRecieverInfo().toString() );
		Chunk<Item> c = new Chunk<Item>(-2, null, null, new Item[]{updateTask});
		ExecuterBox<TASK, RESULT> eb = executersMap.get(ri);
		if(eb==null){
			diSys.Common.Logger.TraceWarning("Executer is not alive "+ri.getItemRecieverInfo().toString(),null );
			return;
		}
	    eb.getItemReciever().Add((TASK)c);
	}
	
	@SuppressWarnings("unchecked")
	public void updateExecuter(ExecuterBox<TASK, RESULT> eb) throws RemoteException{
		AutoUpdateTask auTask=null;
		Version last=versionManager.getLastVersion();
		if(last==null){
			diSys.Common.Logger.TraceError("System Manager has no updates for executers",null);
			return;
		}
		auTask = new AutoUpdateTask(last.updates,last.version);
		diSys.Common.Logger.TraceInformation("Sendig Update Task to Executer "+eb.getRemoteInfo().toString() );
		Chunk<Item> c = new Chunk<Item>(-2, null, null, new Item[]{auTask});
	    eb.getItemReciever().Add((TASK)c);
		
	}
	public int GetLastVersionNumber() {
		if(versionManager.getLastVersion()==null) return 0;
		return versionManager.getLastVersion().version;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException, IOException {
		int port=0;
		int interval=3000;
		PrintUsage();
		if(args.length>0){
			port=Integer.parseInt(args[0]);
		}
		if(args.length>1){
			interval=Integer.parseInt(args[1]);
		}
		//interval=3000;
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
