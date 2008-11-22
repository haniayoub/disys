package CalcExecuterDemo;


import java.util.Random;

import Client.ClientSystem;

public class ClientSystemCalcDemo {
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	static Random generator = new Random( 19580427 );
	public static void main(String[] args) throws InterruptedException {
	if(args.length<2){
		System.out.println("parameters: [System Manager Address] [System Manager Port]");
		Thread.sleep(2000);
		return ;
	}
	
	ClientSystem<CalcTask, CalcResult> cs=new ClientSystem<CalcTask, CalcResult>(args[0],Integer.parseInt(args[1]),100);
	cs.Start();
	for(int i=0;i<220;i++) cs.tasks.offer(CreateRandomTask());
	Thread.sleep(1000);
	for(int i=0;i<90;i++) cs.tasks.offer(CreateRandomTask());
	Thread.sleep(2000);
	for(int i=0;i<110;i++) cs.tasks.offer(CreateRandomTask());
	
	//Thread.sleep(15000);
	//System.console().readLine();
	cs.Stop();
	System.out.println("Client Done!");
	}
	private static CalcTask CreateRandomTask(){
		
		final CalcTask ct=new CalcTask(generator.nextInt(1000));
			ct.x=generator.nextInt(1000);
			ct.y=generator.nextInt(1000);
		return ct;
	}
	
}
