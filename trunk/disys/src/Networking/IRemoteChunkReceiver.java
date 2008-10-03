package Networking;
import java.rmi.Remote;
import java.rmi.RemoteException;

import Common.Chunk;
import Common.Item;
import Common.RemoteItem;

public interface IRemoteChunkReceiver extends Remote{
	public void Add(Chunk<? extends Item> chunk) throws RemoteException;
}
