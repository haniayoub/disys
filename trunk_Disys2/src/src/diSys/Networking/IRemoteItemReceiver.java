package diSys.Networking;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

import diSys.Common.ExecuterStatistics;
import diSys.Common.Item;
import diSys.Common.ItemInfo;


public interface IRemoteItemReceiver<ITEM extends Item> extends Remote , IRMIObjectBase{
	public void Add(ITEM item) throws RemoteException;
	public RemoteData getExecuterData() throws RemoteException;
	public LinkedList<ItemInfo> getQueueInfo() throws RemoteException;
	public void changePriority(int itemHashCode, int newPriority)throws RemoteException ;
	public void removeTask(int itemHashCode)throws RemoteException ;
	public ItemInfo getCurrentTask()throws RemoteException ;
	//public void moveUpTask(int itemHashCode)throws RemoteException ;
	//public void moveDownTask(int itemHashCode)throws RemoteException ;
	public int getMinPriority()throws RemoteException ;
	public int getMaxPriority()throws RemoteException ;
	public ExecuterStatistics GetStatistics() throws RemoteException;
	public void ResetStatistics() throws RemoteException;
}
