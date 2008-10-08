package Networking;


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import Common.Item;

public class RemoteItemReceiver<ITEM extends Item> extends UnicastRemoteObject implements IRemoteItemReceiver<ITEM> {
	static final String GlobalId="itemReciever";

	BlockingQueue<ITEM> recievedItems;
	private String localId;
	private int port;
	private static final long serialVersionUID = -3040410137934057567L;
	
		public RemoteItemReceiver(BlockingQueue<ITEM> itemsQueue) throws RemoteException {
			super();
			localId=GlobalId;
			try {
				// if (System.getSecurityManager() == null)
			     //      System.setSecurityManager ( new RMISecurityManager() );
				 port=Common.createRegistry();
				 Naming.bind ("//:"+port+"/"+localId, this);

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
	   public String getLocalId() {
			return localId;
		}
	   public int getPort() {
			return port;
		}
	@Override
	public void Add(ITEM item) throws RemoteException {
		recievedItems.add(item);
		System.out.println("item added "+item.toString());
	}
	
}
