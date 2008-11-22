package CalcExecuterDemo;

import java.rmi.RemoteException;

import SystemManager.SystemManager;

public class SystemManagerCalcDemo {
	/**
	 * @param args
	 * @throws RemoteException
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws RemoteException, InterruptedException {

		try {
			new SystemManager<CalcTask,CalcResult>();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Common.Loger.TraceInformation("SystemManager is online");
		//System.console().readLine();
		Thread.sleep(10000000);
		Common.Loger.TraceInformation("SystemManager Done!");
	}
}
