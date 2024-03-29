package diSys.Networking;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
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
	private Registry reg;
	/**
	 * you have to register the port first
	 * @param rmiID
	 * @param port
	 * @throws Exception
	 */
	public RMIObjectBase(String rmiID, int startPort,int endPort) throws Exception {
		super();
		Security.ConfigureSecuritySettings();
		
		this.rmiID = rmiID;
		port=endPort;
		if(startPort!=0){
			port = NetworkCommon.createRegistry(startPort,endPort);
		}
		else {
			reg=NetworkCommon.createRegistry(port);
		}
		try {
			Naming.bind("//:" + port + "/" + rmiID, this);
		} catch (Exception e) {
				try {
			Naming.rebind("//:" + port + "/" + rmiID, this);
			} catch (Exception e1) {
				diSys.Common.Logger.TraceError("Failed To reBind :" + "//:" + port + "/"
						+ rmiID, e1);
			
				throw e;
			}
		}
	}

	/*public RMIObjectBase(String rmiID) throws Exception {
		this(rmiID, 0);
	}*/
	
	public RMIObjectBase(String rmiID,int port) throws Exception {
		this(rmiID, 0,port);
	}

	public static String GetClientHost() {
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
			UnicastRemoteObject.unexportObject(this, true); 
			try {
				this.finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				System.out.println("Error While finalizing ");
			}
			if(reg!=null)UnicastRemoteObject.unexportObject(reg, true); 

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error While unbinding ://:" + port + "/"
					+ rmiID);
		}
		System.out.println("Unbinded ://:" + port + "/" + rmiID);
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
