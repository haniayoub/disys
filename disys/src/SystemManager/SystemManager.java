package SystemManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
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
	private ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK, RESULT>> blackList =
		new ConcurrentHashMap<ExecuterRemoteInfo, ExecuterBox<TASK,RESULT>>();
	//the next id to assign to Client
	private int nextId = 0;
	private int UpdateVer = 0;
	private String LastClassName;
	
	//Component to check if Executers Still alive
	private HeartBeatChecker<TASK, RESULT> checker;
	
	public SystemManager() throws Exception {
		super(GlobalID);
		checker=new HeartBeatChecker<TASK, RESULT>(this,executersMap,blackList,500);
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
		this.updateExecuter(remoteInfo);
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
		if(!this.clientsMap.isEmpty()) {
			Common.Logger.TraceWarning("Can't Update While There is Clients Connected to the system !", null);
			return "Can't Update While There is Clients Connected to the system !";
		}
		UpdateVer++;
		Common.Logger.TraceInformation("System is updating to version "+UpdateVer+" new Executer ["+className+"]");
		try {
			String newVerFile=this.getLastVerFile();
			Common.Logger.TraceInformation("Saving New Update Jar to :"+newVerFile);
			JarFileReader.WriteFile(newVerFile,jar);
		} catch (FileNotFoundException e1) {
			UpdateVer--;
			e1.printStackTrace();
			return "failed to update File not Found ";
		}
		
		String s="";
		AutoUpdateTask auTask=null;
		try {
			auTask = new AutoUpdateTask(JarFileReader.ReadFileBytes(this.getLastVerFile()),this.getLastVersion(),className);
		} catch (FileNotFoundException e) {
			throw(new RemoteException(e.toString()));
		}
		LastClassName=className;
		Common.Logger.TraceInformation("Sendig Update Task to Executers");
		Chunk<Item> c = new Chunk<Item>(-2, null, null, new Item[]{auTask});
		LinkedList<ExecuterRemoteInfo> toREmove=new LinkedList<ExecuterRemoteInfo>();
		for (ExecuterRemoteInfo ri  : executersMap.keySet())
		{
			ExecuterBox<TASK, RESULT> eb = executersMap.get(ri);
			try{
				eb.getItemReciever().Add((TASK)c);
			}
			catch(Exception e)
			{
				Common.Logger.TraceInformation( "Executer " + ri.getItemRecieverInfo().toString()+ " didn't response"+"\n");
				s += "Executer " + ri.getItemRecieverInfo().toString()+ " didn't response"+"\n";
				Common.Logger.TraceInformation( "Moving Executer " + ri.getItemRecieverInfo().toString()+ " To Black List\n");
				toREmove.add(ri);
			}
		}
		for(ExecuterRemoteInfo ri:toREmove) this.MoveToBlackList(ri);
		Common.Logger.TraceInformation("Update operation has been sent to executers");
		s+="Update operation has been sent to executers";
		return s;
	}
	
	private void MoveToBlackList(ExecuterRemoteInfo ri){
		blackList.put(ri, executersMap.get(ri));
		executersMap.remove(ri);
	}
	@SuppressWarnings({ "unchecked", "unused" })
	public void updateExecuter(ExecuterRemoteInfo ri) throws RemoteException{
		AutoUpdateTask auTask=null;
		try {
			auTask = new AutoUpdateTask(JarFileReader.ReadFileBytes(this.getLastVerFile()),this.getLastVersion(),LastClassName);
		} catch (FileNotFoundException e) {
			
			Common.Logger.TraceError("Update File Not Found",e);
			return;
		}
		updateExecuter(ri,auTask);
	}
	@SuppressWarnings("unchecked")
	private void updateExecuter(ExecuterRemoteInfo ri,AutoUpdateTask updateTask) throws RemoteException{
		Common.Logger.TraceInformation("Sendig Update Task to Executer "+ri.getItemRecieverInfo().toString() );
		Chunk<Item> c = new Chunk<Item>(-2, null, null, new Item[]{updateTask});
		ExecuterBox<TASK, RESULT> eb = executersMap.get(ri);
	    eb.getItemReciever().Add((TASK)c);
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException, IOException {

		try {
			new SystemManager();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Common.Logger.TraceInformation("SystemManager is online");
		//System.console().readLine();
		System.in.read();
		Common.Logger.TraceInformation("SystemManager Stopped!");
	}

	public int GetLastVersion() {
		
		return this.UpdateVer;
	}
	
}
