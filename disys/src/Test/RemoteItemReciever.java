package Test;


import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import CalcExecuterDemo.CalcTask;
import Common.Item;
import Common.RemoteItem;

public class RemoteItemReciever<ITEM extends Item> extends UnicastRemoteObject implements IItemReciever<ITEM> {

	BlockingQueue<ITEM> recievedItems;
	private static final long serialVersionUID = -3040410137934057567L;
	private static final int MAX_PORT = 30000;
	private static final int INITIAL_PORT = 3000;
		public RemoteItemReciever(long id,BlockingQueue<ITEM> chunksQueue) throws RemoteException {
			super();
			try {
				// if (System.getSecurityManager() == null)
			     //      System.setSecurityManager ( new RMISecurityManager() );
				 int port=createRegistry();
				 Naming.bind ("//:"+port+"/ChunkReciever"+id, this);

			} catch (Exception e) {
				
				//Naming.rebind("ChunkReciever"+id, this);
				
			
				System.out.println("Failed to Bind : "
						+ e.getMessage());
				System.out.println("Exiting.");
				e.printStackTrace();
				System.exit(1);
			}
			recievedItems=chunksQueue;
		}
	private int createRegistry() throws RemoteException {
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
	@Override
	public void Add(ITEM item) throws RemoteException {
		System.out.println("Item Revieved:"+item.toString());
		recievedItems.add(item);
	}
	
	public static void main(String[] args) throws InterruptedException {
	BlockingQueue<CalcTask> recievedItems=new LinkedBlockingQueue<CalcTask>();
	try {
		RemoteItemReciever<CalcTask> itemReciever=new RemoteItemReciever<CalcTask>(0,recievedItems);
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.exit(0);
	}
	while(true){
		System.out.println(recievedItems.take().toString());
	}
	
	
	}
}
