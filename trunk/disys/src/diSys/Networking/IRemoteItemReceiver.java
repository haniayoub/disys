package diSys.Networking;
import java.rmi.Remote;
import java.rmi.RemoteException;

import diSys.Common.Item;


public interface IRemoteItemReceiver<ITEM extends Item> extends Remote , IRMIObjectBase{
	public void Add(ITEM item) throws RemoteException;
	public RemoteData getExecuterData() throws RemoteException;
}
