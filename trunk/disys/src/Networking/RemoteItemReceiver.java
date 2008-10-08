package Networking;


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import Common.Item;

public class RemoteItemReceiver<ITEM extends Item> extends UnicastRemoteObject implements IRemoteItemReceiver<ITEM> {

	BlockingQueue<ITEM> recievedItems;
	private static final long serialVersionUID = -3040410137934057567L;
	
		public RemoteItemReceiver(long id,BlockingQueue<ITEM> itemsQueue) throws RemoteException {
			super();
			try {
				// if (System.getSecurityManager() == null)
			     //      System.setSecurityManager ( new RMISecurityManager() );
				 int port=Common.createRegistry();
				 Naming.bind ("//:"+port+"/itemReciever"+id, this);

			} catch (Exception e) {
				
				//Naming.rebind("ChunkReciever"+id, this);
				
			
				System.out.println("Failed to Bind : "
						+ e.getMessage());
				System.out.println("Exiting.");
				e.printStackTrace();
				System.exit(1);
			}
			recievedItems=itemsQueue;
		}
	@Override
	public void Add(ITEM item) throws RemoteException {
		recievedItems.add(item);
		System.out.println("item added "+item.toString());
	}
	
}
