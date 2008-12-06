package SystemManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import Common.Chunk;
import Common.ClientRemoteInfo;
import Common.ExecuterRemoteInfo;
import Common.Item;
import Common.JarFileReader;
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
	public static final String UpdateDir = "UpdateJars";
	public static final String UpdateExtension = "ujar";
	//Max ID to assign to Clients
	private static final int MAX_ID = 10000;
	//executers In this System Manager
	private ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> executersMap=
		new ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK,RESULT>>();
	private ConcurrentHashMap<ClientRemoteInfo, ClientBox> clientsMap=
		new ConcurrentHashMap<ClientRemoteInfo, ClientBox>();
	//the next id to assign to Client
	private int nextId = 0;
	private int UpdateVer = 0;
	
	//Component to check if Executers Still alive
	private HeartBeatChecker<TASK, RESULT> checker;
	
	public SystemManager() throws Exception {
		super(GlobalID);
		checker=new HeartBeatChecker<TASK, RESULT>(executersMap,200);
		checker.start();
		File f=new File(UpdateDir);
		f.mkdir();
		UpdateVer=getLastVersion();
		Common.Logger.TraceInformation("System Last Version "+UpdateVer);
	}
	
	private int getLastVersion(){
		File fl=new File(UpdateDir);
		
		File[] files=fl.listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				
				return name.endsWith(UpdateExtension);
			}
		});
		int maxVer=0;
		int curr=0;
		for(File f:files){
		curr=Integer.parseInt(f.getName().replace("."+UpdateExtension,""));
		if(maxVer<curr)maxVer=curr;
		}
		return maxVer;
	}
	
	private String getLastVerFile(){
		return UpdateDir+"\\"+UpdateVer+"."+UpdateExtension;
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
			Common.Logger.TraceInformation("Client " + riRMI.toString() + " has been added to the system");
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
	public String Update(byte[] jar,String className) throws RemoteException
	{
		String s="";
		AutoUpdateTask auTask=null;
		try {
			auTask = new AutoUpdateTask(JarFileReader.ReadFileBytes(this.getLastVerFile()),this.getLastVersion(),className);
		} catch (FileNotFoundException e) {
			throw(new RemoteException(e.toString()));
		}
		for (ExecuterRemoteInfo ri  : executersMap.keySet())
		{
			ExecuterBox<TASK, RESULT> eb = executersMap.get(ri);
			try{
				Chunk<Item> c = new Chunk<Item>(-2, null, null, new Item[]{auTask});
				eb.getItemReciever().Add((TASK)c);
			}
			catch(Exception e)
			{
				s += "Executer " + ri.getItemRecieverInfo().toString()+ " didn't response";
			}
		}
		return s;
	}
	
}
