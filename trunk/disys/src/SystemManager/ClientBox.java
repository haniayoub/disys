package SystemManager;

import java.rmi.RemoteException;

import Networking.IClientRemoteObject;

public class ClientBox {
	private IClientRemoteObject cro;
	public ClientBox(IClientRemoteObject cro) {
		this.cro = cro;
	}

	boolean isIdle() throws RemoteException
	{
		return cro.IsIdle();
	}
}
