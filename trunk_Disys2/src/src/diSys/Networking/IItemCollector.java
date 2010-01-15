package diSys.Networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

import diSys.Common.Chunk;
import diSys.Common.Item;


public interface IItemCollector<ITEM extends Item> extends Remote , IRMIObjectBase{
	public Chunk<ITEM> Collect(long id) throws RemoteException;
	public String CollectLog()throws RemoteException;
}
