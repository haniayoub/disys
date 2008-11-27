package SystemManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Common.ClientRemoteInfo;
import Common.ExecuterRemoteInfo;
import Common.Item;

public interface ISystemManager<ITEM extends Item> extends Remote {
	public ExecuterRemoteInfo Schedule(int numberOfTask) throws RemoteException;

	public void addExecuter(int itemReciverPort, int resultCollectorPort)
			throws RemoteException;

	public ClientRemoteInfo AssignClientRemoteInfo(int port,String ID) throws RemoteException;
}