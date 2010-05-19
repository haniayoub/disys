package diSys.SystemManager;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import diSys.Common.Chunk;
import diSys.Common.ClientRemoteInfo;
import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;
import diSys.Common.ItemInfo;
import diSys.Common.Logger;
import diSys.Common.RMIRemoteInfo;
import diSys.Common.SystemManagerData;
import diSys.Common.SystemUpdates;
import diSys.Networking.IClientRemoteObject;
import diSys.Networking.NetworkCommon;
import diSys.Networking.RMIObjectBase;


@SuppressWarnings("serial")
public class SystemManager<TASK extends Item,RESULT extends Item> extends RMIObjectBase
		implements ISystemManager<TASK> {
	//RMI object Id
	public static final String GlobalID = "SystemManager";
	public static final String UpdateDir = "UpdateJars";

	public static final String ExecutersList = "executersList";
	public static final Object UpdateLock =new Object();
	//Max ID to assign to Clients
	private static final int MAX_ID = 10000;
	
	public static double MAX_EP = 0;
	//executers In this System Manager

	private ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> executersMap=
		new ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK,RESULT>>();
	private ConcurrentHashMap<ClientRemoteInfo, ClientBox> clientsMap=
		new ConcurrentHashMap<ClientRemoteInfo, ClientBox>();
	private ConcurrentLinkedQueue<ExecuterRemoteInfo> blackList =
		new ConcurrentLinkedQueue<ExecuterRemoteInfo>();
	
	//the next id to assign to Client
	private int nextId = 0;
	//Round Robin counter
	//private int RRcounter = 0;
	
	private VersionManager versionManager=new VersionManager(UpdateDir,5);
	private ExecutersList executersFile=new ExecutersList(ExecutersList);
	//Component to check if Executers Still alive
	private HeartBeatChecker<TASK, RESULT> checker;
	
	private String systemManagerName = "Disys_SysMgr";
	
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
		diSys.Common.Logger.TraceInformation(SystemManager.GetClientHost() + " Whant to Execute "
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
		String address = SystemManager.GetClientHost();
		
		if(address==null){
		diSys.Common.Logger.TraceError("Can't add executer , address couldn't be resolved!",null);
		return;
		}
		addExecuter(address,itemRecieverPort,resultCollectorPort);
		
	}
	

	public void addExecuter(String address, int irPort, int rcPort) throws RemoteException {
		diSys.Common.Logger.TraceInformation("Adding New Executer : "+address+" "+irPort+ " "+rcPort);
		ExecuterRemoteInfo remoteInfo = 
			new ExecuterRemoteInfo(address,irPort, rcPort);
		blackList.add(remoteInfo);
		executersFile.addExecuterToFile(address,irPort,rcPort, "NoName");
		diSys.Common.Logger.TraceInformation("added:"+ remoteInfo.toString());
		
	}
	
	@Override
	public String addExecuter(ExecuterRemoteInfo exir) throws RemoteException {
		blackList.add(exir);
		executersFile.addExecuterToFile(exir.getItemRecieverInfo().Ip(),exir.getItemRecieverInfo().Port(),exir.getResultCollectorInfo().Port(), exir.getName());
		diSys.Common.Logger.TraceInformation("added:"+ exir.toString());
		return "added:"+ exir.toString();
	}
	
	@Override
	public void removeExecuter(ExecuterRemoteInfo exir)
			throws RemoteException {
			blackList.remove(exir);
			executersMap.remove(exir);
			executersFile.remove(exir);
	}
	@Override
	synchronized public ClientRemoteInfo AssignClientRemoteInfo(int port, String ID) throws RemoteException {
		
		String address = SystemManager.GetClientHost();
		if(address==null){
		diSys.Common.Logger.TraceError("Can't Create Client RemoteInfo , address couldn't be resolved!",null);
		return null;
		}
		ClientRemoteInfo remoteInfo = new ClientRemoteInfo(address, GetNextId());
		if(!clientsMap.containsKey(remoteInfo))
		{
			RMIRemoteInfo riRMI =new RMIRemoteInfo(SystemManager.GetClientHost(), port, ID);
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
		//ExecuterRemoteInfo remoteInfo = null;
		Logger.TraceInformation("starting scheduling for num tasks = "+numberOfTasks);
		if(executersMap.isEmpty()){
			Logger.TraceInformation("Executers list is empty !");
			return null;
		}
		for (ExecuterRemoteInfo ri : executersMap.keySet())
		{
			double myPP = ri.EP/SystemManager.MAX_EP;
			double EFF_BC = ri.BC * myPP;
			double EFF_BE = ri.BS/EFF_BC;
			ri.PP = myPP;
			ri.EFF_BE = EFF_BE;
			Logger.TraceInformation(ri.toString());
			Logger.TraceInformation("PP = "+myPP + "EFF_BE = "+EFF_BE);
			
			//RRlist[i++] = ri;
		}
		double Min_EFF_BE = Double.MAX_VALUE;
		for (ExecuterRemoteInfo ri : executersMap.keySet())
			if (ri.EFF_BE<Min_EFF_BE) Min_EFF_BE = ri.EFF_BE;  
		LinkedList<ExecuterRemoteInfo> minEffs= new LinkedList<ExecuterRemoteInfo>();
		for (ExecuterRemoteInfo ri : executersMap.keySet())
			if ( ri.EFF_BE == Min_EFF_BE) minEffs.add(ri);
		ExecuterRemoteInfo maxPPri=null;
		for (ExecuterRemoteInfo ri : minEffs){
			if (maxPPri == null) maxPPri = ri;
			if (maxPPri.PP<ri.PP) maxPPri=ri;
		}
		maxPPri.BS+=numberOfTasks;
		return maxPPri;
		
		/*
		ExecuterRemoteInfo RRlist[] = new ExecuterRemoteInfo[executersMap.size()];
		int i=0;
		for (ExecuterRemoteInfo ri : executersMap.keySet())
		{
			RRlist[i++] = ri;
		}
		
		
		RRcounter = RRcounter%i;
		return RRlist[RRcounter++];
		*/
		/*
		int minNumOfTasks = Integer.MAX_VALUE;
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
		*/
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
	
	synchronized public String Update(SystemUpdates updates,boolean force) throws RemoteException
	{
		if(this.clientsMap.size()>1&&!force) {
			throw new RemoteException("Can't Update While There is Another Clients Connected to the system !");
		}
		String s="";
		if(updates!=versionManager.getLastVersion().updates)
		s+=versionManager.addVersion(updates);
		//TODO: no need to wait for heart beat checker !!
		diSys.Common.Logger.TraceInformation("Waiting heart beat checker ...");
	    synchronized(UpdateLock){
	    	checker.Stop();
	    	diSys.Common.Logger.TraceInformation("Starting update ...");
	    for (ExecuterRemoteInfo ri  : executersMap.keySet())
	    {
		   this.updateExecuter(executersMap.get(ri));
		   executersMap.get(ri).Blocked=false;
	    }
	    }
	    
	    int delay = 1000;   // delay for 1 sec.
	    int period = 10000;
	    Timer timer = new Timer();
	    this.Dispose();
	    timer.scheduleAtFixedRate(new TimerTask() {
	            public void run() {
	            	if((new File(systemManagerName + ".exe")).exists())
	            	{
	            		diSys.Common.CommandLineRunner.Run("start " + systemManagerName + ".exe -jar SystemManager.jar "+CommandLineArgs);
	            	}
	            	else
	            	{
	            		diSys.Common.CommandLineRunner.Run("start java -jar SystemManager.jar "+CommandLineArgs);
	            	}
	        		diSys.Common.Logger.TerminateSystem("Terminate due to update", null);
	            }
	        }, delay, period);
		
		diSys.Common.Logger.TraceInformation("System is up to date");
		s+="Update operation has been sent to executers";
		return s;
	}
	
	@Override
	public String UpdateToLastRevision(boolean force) throws RemoteException {
		if(this.versionManager.getLastVersion()==null||this.versionManager.getLastVersion().updates==null) return "Couldn't update , no updates found!";
	    return Update(this.versionManager.getLastVersion().updates,force);
	}
	
	private void MoveToBlackList(ExecuterRemoteInfo ri){
		blackList.add(ri);
		executersMap.remove(ri);
	}

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
	public void updateExecuter(ExecuterBox<TASK, RESULT> eb){
		AutoUpdateTask auTask=null;
		Version last=versionManager.getLastVersion();
		
		if(last==null||last.version==0){
			diSys.Common.Logger.TraceError("System Manager has no updates for executers",null);
			return;
		}
		
		auTask = new AutoUpdateTask(last.updates,last.version);
		diSys.Common.Logger.TraceInformation("Sendig Update Task to Executer "+eb.getRemoteInfo().toString() );
		Chunk<Item> c = new Chunk<Item>(-2, null, null, new Item[]{auTask});
	    try {
			eb.getItemReciever().Add((TASK)c);
		} catch (RemoteException e) {
			diSys.Common.Logger.TraceError("failed to send update Task To :"+eb.getRemoteInfo(), e);
		}
	    
		eb.Blocked=true;
	    eb.BlockCounter=1;
	}
	
	public int GetLastVersionNumber() {
		if(versionManager.getLastVersion()==null) return 0;
		return versionManager.getLastVersion().version;
	}
	@Override
	public SystemManagerData GetData() throws RemoteException {
		SystemManagerData $ =new SystemManagerData();
		$.Version=this.GetLastVersionNumber();
		 for (ExecuterRemoteInfo ri : executersMap.keySet())
		 {
			 	$.activeExecuters.add(ri);
		 }
		 for (ClientRemoteInfo cri  : clientsMap.keySet())
		 {
			 	$.Clients.add(cri);
		 }
		 $.enActiveExecuters.addAll(blackList);	
		 for (ExecuterRemoteInfo ri : executersMap.keySet())
		 {
			 	if( $.enActiveExecuters.contains(ri))$.enActiveExecuters.remove(ri);
		 }
		return $;
	}
	
	@Override
	public String CollectLog() throws RemoteException {
		
		return diSys.Common.Logger.logTracer.toString();
	}
	private static String CommandLineArgs;
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws  IOException {
		CommandLineArgs="";
		for(String s:args)
		CommandLineArgs=CommandLineArgs+" "+s;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
		
		}
		int port=0;
		int interval=300;
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
		while(true)
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			
			}
		//diSys.Common.Logger.TraceInformation("SystemManager Stopped!");
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
	@Override
	public LinkedList<ItemInfo> getQueueInfo(ExecuterRemoteInfo ri) throws RemoteException {
		return executersMap.get(ri).getItemReciever().getQueueInfo();
	}
	@Override
	public void changePriority(ExecuterRemoteInfo exri, int itemHashCode,
			int newPriority) throws RemoteException {
		executersMap.get(exri).getItemReciever().changePriority(itemHashCode, newPriority);
	}
	@Override
	public void removeTask(ExecuterRemoteInfo exri, int itemHashCode)
			throws RemoteException {
		executersMap.get(exri).getItemReciever().removeTask(itemHashCode);
	}
	@Override
	public ItemInfo getCurrentTask(ExecuterRemoteInfo exri)
			throws RemoteException {
		return executersMap.get(exri).getItemReciever().getCurrentTask();
	}
	/*
	@Override
	public void moveDownTask(ExecuterRemoteInfo exri, int itemHashCode)
			throws RemoteException {
		executersMap.get(exri).getItemReciever().moveDownTask(itemHashCode);	
	}
	@Override
	public void moveUpTask(ExecuterRemoteInfo exri, int itemHashCode)
			throws RemoteException {
		executersMap.get(exri).getItemReciever().moveUpTask(itemHashCode);
	}
	*/
}
