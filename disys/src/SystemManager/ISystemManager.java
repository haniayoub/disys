package SystemManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Common.Item;
import Common.RemoteInfo;

public interface ISystemManager<ITEM extends Item> extends Remote{
	public RemoteInfo Schedule(int numberOfTask) throws RemoteException;
}