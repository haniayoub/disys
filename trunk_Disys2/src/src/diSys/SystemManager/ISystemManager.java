package diSys.SystemManager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

import diSys.Common.ClientRemoteInfo;
import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;
import diSys.Common.ItemInfo;
import diSys.Common.SystemManagerData;
import diSys.Common.SystemUpdates;


public interface ISystemManager<ITEM extends Item> extends Remote {
	public ExecuterRemoteInfo Schedule(int numberOfTask) throws RemoteException;

	public void addExecuter(int itemReciverPort, int resultCollectorPort)
			throws RemoteException;

	public ClientRemoteInfo AssignClientRemoteInfo(int port,String ID) throws RemoteException;
	public String addExecuter(ExecuterRemoteInfo exir) throws RemoteException;
	public void removeExecuter(ExecuterRemoteInfo exir) throws RemoteException;
	public String Update(SystemUpdates updates,boolean force) throws RemoteException;
	public String UpdateToLastRevision(boolean force) throws RemoteException;
	public String CleanExit() throws RemoteException;
	public SystemManagerData GetData() throws RemoteException;
	public String CollectLog() throws RemoteException;
	public LinkedList<ItemInfo> getQueueInfo(ExecuterRemoteInfo exri) throws RemoteException; 
	
	public void changePriority(ExecuterRemoteInfo exri ,int itemHashCode, int newPriority)throws RemoteException ;
	public void removeTask(ExecuterRemoteInfo exri ,int itemHashCode)throws RemoteException ;
	public ItemInfo getCurrentTask(ExecuterRemoteInfo exri)throws RemoteException ;
	public void EnableExecuter(ExecuterRemoteInfo exri )throws RemoteException ;
	public void DisableExecuter(ExecuterRemoteInfo exri )throws RemoteException ;
	public boolean GetExecuterStatusExecuter(ExecuterRemoteInfo exri ) throws RemoteException ;
	//public void DisableExecuter(ExecuterRemoteInfo exri )throws RemoteException ;
	
	//public void moveUpTask(ExecuterRemoteInfo exri, int itemHashCode)throws RemoteException ;
	//public void moveDownTask(ExecuterRemoteInfo exri, int itemHashCode)throws RemoteException ;
}