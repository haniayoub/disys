package SystemManager;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

import CalcExecuterDemo.CalcTask;
import Common.ClientRemoteInfo;
import Common.Item;
import Common.RMIRemoteInfo;

@SuppressWarnings("serial")
public class SystemManager<ITEM extends Item> extends UnicastRemoteObject implements ISystemManager<ITEM> {
	public static final String GlobalID="SystemManager";

	private static final int MAX_ID =10000;
	
	private LinkedList<RMIRemoteInfo> executerList=new LinkedList<RMIRemoteInfo>();
   
	private int nextExecuter=0;
	private int nextId=0;
	
	public SystemManager() throws RemoteException {
		super();
		try {
			 // if (System.getSecurityManager() == null)
		     //      System.setSecurityManager ( new RMISecurityManager() );
			 int port=Networking.Common.createRegistry();
			 Naming.bind ("//:"+port+"/"+GlobalID, this);
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
	public RMIRemoteInfo Schedule(int numberOfTask) throws RemoteException {
		if(executerList.isEmpty())return null;
		try {
			
			System.out.println(RemoteServer.getClientHost()+" Whant to do "+numberOfTask+" Tasks");
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(nextExecuter>executerList.size()-1)nextExecuter=0; //scheduling is round robin basis
		RMIRemoteInfo remoteInfo=executerList.get(nextExecuter++);
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
		RMIRemoteInfo remoteInfo=new RMIRemoteInfo(address,port,id); 
		executerList.add(remoteInfo);
		System.out.println("new Executer added:"+remoteInfo.GetRmiAddress());
	}
	@Override
	public ClientRemoteInfo AssignClientRemoteInfo() throws RemoteException {
		String address;
		try {
			address = RemoteServer.getClientHost();
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Cant add the executer address couldn't be resolved!");
			return null;
		}
		ClientRemoteInfo remoteInfo=new ClientRemoteInfo(address,GetNextId()); 
		return remoteInfo;
	}
	private long GetNextId() {
		if(nextId>MAX_ID)nextId=0;
		return nextId++;
	}

	/**
	 * @param args
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException {

		new SystemManager<CalcTask>();
		System.out.println("SystemManager is online");
		System.console().readLine();
		System.out.print("SystemManager Done!");
	}

	


	

}
