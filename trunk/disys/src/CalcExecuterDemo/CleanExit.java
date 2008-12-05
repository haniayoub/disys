package CalcExecuterDemo;

import java.rmi.RemoteException;

import Common.Item;
import Common.RMIRemoteInfo;
import SystemManager.ISystemManager;
import SystemManager.SystemManager;

public class CleanExit {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		RMIRemoteInfo ri=new RMIRemoteInfo(args[0],Integer.parseInt(args[1]),SystemManager.GlobalID);
		ISystemManager<Item> sm=Networking.NetworkCommon.loadRMIRemoteObject(ri); 
		try {
			System.out.println(sm.CleanExit());
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		
	
	}

}
