package Test;
import java.rmi.Remote;
import java.rmi.RemoteException;

import Common.Item;


public interface IItemReciever<ITEM extends Item> extends Remote{
	public void Add(ITEM item) throws RemoteException;
}
