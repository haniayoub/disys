package SystemManager;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

import CalcExecuterDemo.CalcTask;
import Common.Item;
import Common.RemoteInfo;

@SuppressWarnings("serial")
public class SystemManager<ITEM extends Item> extends UnicastRemoteObject implements ISystemManager<ITEM> {
	
	private LinkedList<RemoteInfo> executerList=new LinkedList<RemoteInfo>();

	private int next=0;
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
	//	executerList.add(new RemoteInfo("LocalHost",3001,"itemReciever0"));
	}

	@Override
	public RemoteInfo Schedule(int numberOfTask) throws RemoteException {
		if(executerList.isEmpty())return null;
		try {
			
			System.out.println(RemoteServer.getClientHost()+" Whant to do "+numberOfTask+" Tasks");
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(next>executerList.size()-1)next=0; //scheduling is round robin basis
		RemoteInfo remoteInfo=executerList.get(next++);
		System.out.println("Scheduling executer:"+remoteInfo.GetRmiAddress());
		return remoteInfo;
	}

	@Override
	public void addExecuter(String id, int port) throws RemoteException {
		String address;
		try {
			address = RemoteServer.getClientHost();
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Cant add the executer address couldn't be resolved!");
			return ;
		}
		RemoteInfo remoteInfo=new RemoteInfo(address,port,id); 
		executerList.add(remoteInfo);
		System.out.println("new Executer added:"+remoteInfo.GetRmiAddress());
	}

	/**
	 * @param args
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException {

		new SystemManager<CalcTask>(0);
		System.out.print("SystemManager is online");
		System.console().readLine();
		System.out.print("SystemManager Done!");
	}


	

}
