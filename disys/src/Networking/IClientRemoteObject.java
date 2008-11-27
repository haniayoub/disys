package Networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientRemoteObject extends Remote , IRMIObjectBase{
	public boolean IsIdle() throws RemoteException;

}
