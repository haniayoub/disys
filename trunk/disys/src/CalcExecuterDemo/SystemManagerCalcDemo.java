package CalcExecuterDemo;

import java.rmi.RemoteException;

import SystemManager.SystemManager;

public class SystemManagerCalcDemo {
	/**
	 * @param args
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException {

		try {
			new SystemManager<CalcTask,CalcResult>();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Common.Loger.TraceInformation("SystemManager is online");
		//System.console().readLine();
		Common.Loger.TraceInformation("SystemManager Done!");
	}
}
