package diSys.Networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMIObjectBase  extends Remote{
	public void Alive() throws RemoteException;
}
