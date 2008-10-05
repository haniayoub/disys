package CalcExecuterDemo;

import Common.RemoteItem;
import Executor.ExecuterSystem;

public class ExecuterSystemDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
	ExecuterSystem<Calculator> es=new ExecuterSystem<Calculator>(new Calculator(), 5); 
	System.out.println("Executer Started !");
	/*for(int i=0;i<1000;i++){
		CalcTask ct=new CalcTask(i);
		ct.x=i;
		ct.y=10;
		RemoteItem<CalcTask> ri=new RemoteItem<CalcTask>(ct,null);
		es.tasks.add(ri);
	}*/
	es.Run(args);
	Thread.sleep(10000);
	for (RemoteItem ri:es.results){
	System.out.println(ri.getItem().toString());
	}
	}
	

}
