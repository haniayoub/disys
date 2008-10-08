package Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;

import Common.Chunk;
import Common.Item;
import Common.RemoteInfo;
import Networking.IRemoteItemReceiver;
import SystemManager.ISystemManager;
import WorkersSystem.WorkER.AWorker;

public class ChunkScheduler<TASK extends Item> extends AWorker<Chunk<TASK>,Chunk<TASK>>  {
	private ISystemManager<TASK> sysManager;

	@SuppressWarnings("unchecked")
	public ChunkScheduler(RemoteInfo systemManagerRemoteInfo,BlockingQueue<Chunk<TASK>> taskChunks, BlockingQueue<Chunk<TASK>> taskChunks2) {
		super(taskChunks, taskChunks2);
		try {
			sysManager = (ISystemManager<TASK>) 
				    Naming.lookup(systemManagerRemoteInfo.GetRmiAddress());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Chunk<TASK> doItem(Chunk<TASK> task) {
		RemoteInfo executerAddress=null;
		try {
		executerAddress=sysManager.Schedule(task.numberOfItems());
			
		} catch (RemoteException e) {
			System.out.println("Failed to schdule a Chunk :"+e.getMessage());
			e.printStackTrace();
			return task;
		}
		if(executerAddress==null){
			System.out.println("Failed to schdule a Chunk : No executers Found");
			return task;
		}
		IRemoteItemReceiver<Chunk<? extends Item>> RemoteExecuter=null;
		try {
			RemoteExecuter=(IRemoteItemReceiver<Chunk<? extends Item>>) 
					Naming.lookup(executerAddress.GetRmiAddress());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to schdule a Chunk :executer Not Found:"+executerAddress.GetRmiAddress());
			return task;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to schdule a Chunk :executer Not Found:"+executerAddress.GetRmiAddress());
			return task;
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to schdule a Chunk :executer Not Found:"+executerAddress.GetRmiAddress());
			return task;
		}
		try {
			RemoteExecuter.Add(task);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to schdule a Chunk :Remote Exception at executer.add(Chunk) :"+executerAddress.GetRmiAddress());
			return task;
		}
	    return null;
	}

}
