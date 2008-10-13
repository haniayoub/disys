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
	ClientSystem<CalcTask, CalcResult> cs=new ClientSystem<CalcTask, CalcResult>("LocalHost",3000);
	cs.Start();
	for(int i=0;i<3;i++) cs.tasks.add(CreateRandomTask());
	Thread.sleep(1000);
	for(int i=0;i<2;i++) cs.tasks.add(CreateRandomTask());
	Thread.sleep(2000);
	for(int i=0;i<4;i++) cs.tasks.add(CreateRandomTask());
	
	//Thread.sleep(15000);
	System.console().readLine();
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
