package diSys.Networking;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import diSys.Common.Logger;
import diSys.Common.RMIRemoteInfo;


/**
 * Networking Common Class
 * @author saeed
 *
 */
public class NetworkCommon {
	//private static final int MAX_PORT = 30000;
	//private static final int INITIAL_PORT = 3000;
	/**
	 * Creates a Remote RMI Object by RMI RemoteInfo 
	 * @param <T> the Type of the Class
	 * @param ri the Object Remote Info
	 * @return RMI Object
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Remote> T loadRMIRemoteObject(RMIRemoteInfo ri){
		
		try {
			//Registry remoteRegistry = LocateRegistry.getRegistry(ri.Ip(),ri.Port());
			// Get remote object reference
			return (T)Naming.lookup(ri.GetRmiAddress());
			//return (T)Naming.lookup(ri.GetRmiAddress());
		} catch (Exception e) {
			diSys.Common.Logger.TraceError("Connection Failed:"+ri.GetRmiAddress(), null);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized static  <T extends Remote> T loadRMIRemoteObjectNoLog(RMIRemoteInfo ri){
		
		try {
			return (T)Naming.lookup(ri.GetRmiAddress());
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * Return the first Unused Port from 3000 to 30000 
	 * @return the port
	 * @throws RemoteException
	 */
	static public int createRegistry(int startPort,int endPort) throws RemoteException {
		RemoteException failureException = null;
		for (int i = startPort; i <= endPort; ++i) {
			try {
				LocateRegistry.createRegistry(i);
				return i;
			} catch (RemoteException e) {
				failureException = e;
			}
		}
		Logger.TraceError("Failed to create registry", failureException);
		throw failureException;
	}
	static public Registry createRegistry(int r) throws RemoteException {
		RemoteException failureException = null;
		for(int i=0;i<10;i++){	
		try {
				return LocateRegistry.createRegistry(r);
			} catch (RemoteException e) {
				failureException = e;
			}
		Logger.TraceError("Failed to create registry", failureException);
		Logger.TraceInformation("retry in 1 sec ...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			}
		}
		throw failureException;
	}
}
