package CalcExecuterDemo;

import java.io.IOException;

import Executor.ExecuterSystem;

public class ExecuterSystemCalcDemo {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		if(args.length<2){
			System.out.println("parameters: [System Manager Address] [System Manager Port]");
			Thread.sleep(2000);
			return ;
		}
		ExecuterSystem<CalcTask,CalcResult,Calculator> es=new ExecuterSystem<CalcTask,CalcResult,Calculator>(new Calculator(), 5,args[0],Integer.parseInt(args[1])); 
		System.out.println("Executer Started !");
		es.Run(args);
		//System.console().readLine();
		System.in.read();
		es.Exit();
		System.out.println("executer Done");
	}
}
