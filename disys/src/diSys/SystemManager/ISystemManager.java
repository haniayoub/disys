package diSys.SystemManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

import diSys.Common.ClientRemoteInfo;
import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;


public interface ISystemManager<ITEM extends Item> extends Remote {
	public ExecuterRemoteInfo Schedule(int numberOfTask) throws RemoteException;

	public void addExecuter(int itemReciverPort, int resultCollectorPort)
			throws RemoteException;

	public ClientRemoteInfo AssignClientRemoteInfo(int port,String ID) throws RemoteException;
	public String Update(byte[] jar,String className,boolean force) throws RemoteException;
	public String CleanExit() throws RemoteException;
}