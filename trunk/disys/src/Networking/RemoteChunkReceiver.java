package Networking;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;

import Common.Chunk;
import Common.Item;

public class RemoteChunkReceiver extends UnicastRemoteObject implements IRemoteChunkReceiver {

	BlockingQueue<Chunk<? extends Item>> recievedChunks;
	private static final long serialVersionUID = -3040410137934057567L;
		public RemoteChunkReceiver(long id,BlockingQueue<Chunk<? extends Item>> chunksQueue) throws RemoteException {
			super();
			try {
				 if (System.getSecurityManager() == null)
			            System.setSecurityManager ( new RMISecurityManager() );
				 Naming.bind ("ItemReciever"+id, this);

			} catch (Exception e) {
				System.out.println("Failed to Bind : "
						+ e.getMessage());
				System.out.println("Exiting.");
				e.printStackTrace();
				System.exit(1);
			}
			recievedChunks=chunksQueue;
		}
	@Override
	public void Add(Chunk<? extends Item> chunk) throws RemoteException {
		recievedChunks.add(chunk);
	}
}
