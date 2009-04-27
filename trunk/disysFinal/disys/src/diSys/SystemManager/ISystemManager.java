package diSys.SystemManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

import diSys.Common.ClientRemoteInfo;
import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;
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
	
	
}