package Networking;


import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;

import Common.Chunk;
import Common.Item;

public class RemoteChunkReceiver extends UnicastRemoteObject implements IRemoteChunkReceiver {

	BlockingQueue<Chunk<? extends Item>> recievedChunks;
	private static final long serialVersionUID = -3040410137934057567L;
	private static final int MAX_PORT = 30000;
	private static final int INITIAL_PORT = 3000;
		public RemoteChunkReceiver(long id,BlockingQueue<Chunk<? extends Item>> chunksQueue) throws RemoteException {
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
			recievedChunks=chunksQueue;
		}
	@Override
	public void Add(/*Chunk<? extends Item> chunk*/ String s) throws RemoteException {
		//recievedChunks.add(chunk);
		System.out.println("chnk added size"+s);
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
	
}
