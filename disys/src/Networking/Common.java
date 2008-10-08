package Networking;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Common {
	private static final int MAX_PORT = 30000;
	private static final int INITIAL_PORT = 3000;
	
	static public int createRegistry() throws RemoteException {
		RemoteException failureException = null;
		for (int i = INITIAL_PORT; i <= MAX_PORT; ++i) {
			try {
				LocateRegistry.createRegistry(i);
				System.out.println("using port "+i);
				return i;
			} catch (RemoteException e) {
				failureException = e;
				System.out.print(".");
			}
		}
		System.err.println("Failed to create registry");
		throw failureException;
	}
}
