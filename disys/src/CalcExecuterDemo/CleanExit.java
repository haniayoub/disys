package CalcExecuterDemo;

import java.rmi.RemoteException;

import diSys.Common.Item;
import diSys.Common.RMIRemoteInfo;
import diSys.SystemManager.ISystemManager;
import diSys.SystemManager.SystemManager;


public class CleanExit {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		RMIRemoteInfo ri=new RMIRemoteInfo(args[0],Integer.parseInt(args[1]),SystemManager.GlobalID);
		ISystemManager<Item> sm=diSys.Networking.NetworkCommon.loadRMIRemoteObject(ri); 
		try {
			System.out.println(sm.CleanExit());
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		
	
	}

}
