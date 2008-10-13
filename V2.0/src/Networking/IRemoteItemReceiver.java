package Networking;
import java.rmi.Remote;
import java.rmi.RemoteException;

import Common.Item;

public interface IRemoteItemReceiver<ITEM extends Item> extends Remote , IRMIObjectBase{
	public void Add(ITEM item) throws RemoteException;
}
