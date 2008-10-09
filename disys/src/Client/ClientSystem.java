package Client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Common.Chunk;
import Common.ClientRemoteInfo;
import Common.Item;
import Common.RMIRemoteInfo;
import SystemManager.ISystemManager;
import SystemManager.SystemManager;
import WorkersSystem.WorkER.WorkerSystem;

public class ClientSystem<TASK extends Item,RESULT extends Item> {

	public BlockingQueue<TASK> tasks = 
		new LinkedBlockingQueue<TASK>();
	public BlockingQueue<RESULT> results= 
		new LinkedBlockingQueue<RESULT>();
	public BlockingQueue<Chunk<TASK>> taskChunks = 
		new LinkedBlockingQueue<Chunk<TASK>>();
	ClientRemoteInfo myRemoteInfo;
	RMIRemoteInfo RemoteSysManagerInfo;
	
	WorkerSystem ws=new WorkerSystem();
	ChunkCreator<TASK> chunkCreatorWorker=new ChunkCreator<TASK>(tasks,taskChunks,myRemoteInfo,1000); 
	ChunkScheduler<TASK> chunkScheduler;
	ISystemManager<TASK> sysManager;
 	
	public ClientSystem(String SysManagerAddress,int sysManagerport) {
		super();
		RemoteSysManagerInfo=new RMIRemoteInfo(SysManagerAddress,sysManagerport,SystemManager.GlobalID);
		ConnectToSystemManager(RemoteSysManagerInfo);
		chunkScheduler=new ChunkScheduler<TASK>(sysManager,taskChunks,taskChunks);
		ws.add(chunkCreatorWorker,1);
		ws.add(chunkScheduler,1);
	}
	@SuppressWarnings("unchecked")
	public void ConnectToSystemManager(RMIRemoteInfo systemManagerRemoteInfo){
	try {
		sysManager = (ISystemManager<TASK>) 
			    Naming.lookup(systemManagerRemoteInfo.GetRmiAddress());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println(e.getMessage());
		System.out.println("Failed to connecet to Remote System Mnager:" +systemManagerRemoteInfo.GetRmiAddress());
		System.exit(1);
	} 
	System.out.println("conneceted to Remote System Mnager:" +systemManagerRemoteInfo.GetRmiAddress());
	try {
		myRemoteInfo=sysManager.AssignClientRemoteInfo();
	} catch (RemoteException e) {
		e.printStackTrace();
		System.out.println(e.getMessage());
		System.out.println("Remote System Mnager to assign Remote ID to Client.");
		System.exit(1);
	}
	if(myRemoteInfo==null){
		System.out.println("Remote System Mnager to assign Remote ID to Client: null Client Remote info .");
		
		System.exit(1);
	}
	System.out.println("My Remote Info is :"+myRemoteInfo.toString());
	}
	public void Start(){
		ws.startWork();	
	}
	public void Stop(){
		ws.stopWork();	
	}
	}
