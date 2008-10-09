package SystemManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Common.ClientRemoteInfo;
import Common.Item;
import Common.RMIRemoteInfo;

public interface ISystemManager<ITEM extends Item> extends Remote{
	public RMIRemoteInfo Schedule(int numberOfTask) throws RemoteException;
	public void addExecuter(String id,int port) throws RemoteException;
	public ClientRemoteInfo AssignClientRemoteInfo() throws RemoteException;
}