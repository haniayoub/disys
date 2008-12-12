package diSys.Networking;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
/**
 * RMI Object Base , implements the basic operations of RMI object
 * Constructor to bind .
 * Dispose to unbind . 
 * @author saeed
 *
 */
@SuppressWarnings("serial")
public class RMIObjectBase extends UnicastRemoteObject implements
		IRMIObjectBase {

	private String rmiID;
	private int port;
	/**
	 * you have to register the port first
	 * @param rmiID
	 * @param port
	 * @throws Exception
	 */
	public RMIObjectBase(String rmiID, int port) throws Exception {
		super();
		this.rmiID = rmiID;
		if(port==0){
			port = NetworkCommon.createRegistry();
		}
		else {
			port = NetworkCommon.createRegistry(port);
		}
		this.port=port;
		try {
			Naming.bind("//:" + port + "/" + rmiID, this);
		} catch (Exception e) {
			diSys.Common.Logger.TraceError("Failed To Bind :" + "//:" + port + "/"
					+ rmiID, e);
			throw e;
		}
	}

	public RMIObjectBase(String rmiID) throws Exception {
		this(rmiID, 0);
	}

	public String GetClientHost() {
		try {
			return RemoteServer.getClientHost();
		} catch (ServerNotActiveException e) {
			diSys.Common.Logger.TraceWarning("Server is not activated!", e);
			return null;
		}
	}

	public void Dispose() {
		try {
			Naming.unbind("//:" + port + "/" + rmiID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error While unbinding ://:" + port + "/"
					+ rmiID);
		}
		System.out.println("Unbinded ://:" + port + "/" + rmiID);
	}

	public String getRmiID() {
		return rmiID;
	}

	public int getPort() {
		return port;
	}

	@Override
	public void Alive() throws RemoteException {
		
	}

}