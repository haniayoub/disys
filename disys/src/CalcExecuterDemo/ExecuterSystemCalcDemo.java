package CalcExecuterDemo;

import Executor.ExecuterSystem;

public class ExecuterSystemCalcDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
	ExecuterSystem<CalcTask,CalcResult,Calculator> es=new ExecuterSystem<CalcTask,CalcResult,Calculator>(new Calculator(), 5,"localhost",3000); 
	System.out.println("Executer Started !");
	es.Run(args);
	System.console().readLine();
	//Thread.sleep(10000);
	es.Exit();
	System.out.println("executer Done");
	}
}
