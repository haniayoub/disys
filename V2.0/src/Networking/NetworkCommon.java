package Networking;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import Common.Loger;
import Common.RMIRemoteInfo;

/**
 * Networking Common Class
 * @author saeed
 *
 */
public class NetworkCommon {
	private static final int MAX_PORT = 30000;
	private static final int INITIAL_PORT = 3000;
	/**
	 * Creates a Remote RMI Object by RMI RemoteInfo 
	 * @param <T> the Type of the Class
	 * @param ri the Object Remote Info
	 * @return RMI Object
	 */
	@SuppressWarnings("unchecked")
	public static  <T extends Remote> T loadRMIRemoteObject(RMIRemoteInfo ri){
		
		try {
			return (T)Naming.lookup(ri.GetRmiAddress());
		} catch (Exception e) {
			Common.Loger.TraceError("Couldn't Load RMI Remote Object :"+ri.GetRmiAddress(), e);
		}
		return null;
	}
	/**
	 * Return the first Unused Port from 3000 to 30000 
	 * @return the port
	 * @throws RemoteException
	 */
	static public int createRegistry() throws RemoteException {
		RemoteException failureException = null;
		for (int i = INITIAL_PORT; i <= MAX_PORT; ++i) {
			try {
				LocateRegistry.createRegistry(i);
				System.out.println("using port "+i);
				return i;
			} catch (RemoteException e) {
				failureException = e;
			}
		}
		Loger.TraceError("Failed to create registry", failureException);
		throw failureException;
	}
}
