package CalcExecuterDemo;


import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Client.RemoteClient;

public class ClientSystemCalcDemo {
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	static Random generator = new Random( 19580427 );
	public static void main(String[] args) throws InterruptedException, IOException {
	if(args.length<2){
		System.out.println("parameters: [System Manager Address] [System Manager Port]");
		Thread.sleep(2000);
		return ;
	}
	RemoteClient<CalcTask, CalcResult> rc=new RemoteClient<CalcTask, CalcResult>(args[0],Integer.parseInt(args[1]),3);
	rc.Start();
	
	CalcTask ct1=new CalcTask(1,12);
	CalcTask ct2=new CalcTask(2,11);
	CalcTask ct3=new CalcTask(3,6);
	CalcTask ct4=new CalcTask(4,3);
	CalcTask ct5=new CalcTask(5,2);
	CalcTask ct6=new CalcTask(6,10);
	
	for(int i=1;i<100;i++){
	rc.AddTask(ct1);
	rc.AddTask(ct2);
	rc.AddTask(ct3);
	rc.AddTask(ct4);
	rc.AddTask(ct5);
	rc.AddTask(ct6);
	}
	
	for(int i=0;i<100;i++)
		try {
			System.out.println(rc.GetResult(100, TimeUnit.SECONDS));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	System.in.read();
	rc.Stop();
	System.out.println("Client Done!");
	}
	@SuppressWarnings("unused")
	private static CalcTask CreateRandomTask(){
		
		final CalcTask ct=new CalcTask(generator.nextInt(1000));
			ct.x=generator.nextInt(1000);
			ct.y=generator.nextInt(1000);
		return ct;
	}
	
}
