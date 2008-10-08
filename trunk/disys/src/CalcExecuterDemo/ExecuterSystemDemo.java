package CalcExecuterDemo;

import Executor.ExecuterSystem;

public class ExecuterSystemDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
	ExecuterSystem<CalcTask,CalcResult,Calculator> es=new ExecuterSystem<CalcTask,CalcResult,Calculator>(new Calculator(), 5); 
	System.out.println("Executer Started !");
	es.Run(args);
	//Thread.sleep(100000);
	System.console().readLine();
	es.Exit();
	System.out.println("executer Done");
	
	}
}
