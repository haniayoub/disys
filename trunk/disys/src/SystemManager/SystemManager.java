package SystemManager;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

import CalcExecuterDemo.CalcTask;
import Common.Item;
import Common.RemoteInfo;

@SuppressWarnings("serial")
public class SystemManager<ITEM extends Item> extends UnicastRemoteObject implements ISystemManager<ITEM> {
	
	private LinkedList<RemoteInfo> executerList=new LinkedList<RemoteInfo>();

	public SystemManager(int id) throws RemoteException {
		super();
		try {
			 // if (System.getSecurityManager() == null)
		     //      System.setSecurityManager ( new RMISecurityManager() );
			 int port=Networking.Common.createRegistry();
			 Naming.bind ("//:"+port+"/systemManager"+id, this);
		} catch (Exception e) {
			
			System.out.println("Failed to Bind : "
					+ e.getMessage());
			System.out.println("Exiting.");
			e.printStackTrace();
			System.exit(1);
		}
		executerList.add(new RemoteInfo("LocalHost",3000,"itemReciever0"));
	}

	@Override
	public RemoteInfo Schedule(int numberOfTask) throws RemoteException {
		// TODO Auto-generated method stub
		return executerList.peek();
	}
	
	/**
	 * @param args
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException {

		new SystemManager<CalcTask>(0);
		System.out.print("SystemManager is online");
		System.console().readLine();
	}

	

}
