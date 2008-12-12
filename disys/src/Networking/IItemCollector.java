package Networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Common.Chunk;
import Common.Item;

public interface IItemCollector<ITEM extends Item> extends Remote , IRMIObjectBase{
	public Chunk<ITEM> Collect(long id) throws RemoteException;
	public String CollectLog()throws RemoteException;
}
