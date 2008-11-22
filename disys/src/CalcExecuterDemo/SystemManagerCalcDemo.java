package CalcExecuterDemo;

import java.io.IOException;

import SystemManager.SystemManager;

public class SystemManagerCalcDemo {
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {

		try {
			new SystemManager<CalcTask,CalcResult>();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Common.Logger.TraceInformation("SystemManager is online");
		//System.console().readLine();
		System.in.read();
		Common.Logger.TraceInformation("SystemManager Done!");
	}
}
