package CalcExecuterDemo;


import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Client.RemoteClient;

public class MultiplyCalcDemo {
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
	String managerAddress=args[0];
	int p=Integer.parseInt(args[1]);
	RemoteClient<CalcTask, CalcResult> rc=new RemoteClient<CalcTask, CalcResult>(managerAddress,p,3,"release\\newjar.jar",MultiplyCalculator.class.getName());
	rc.Start();
	
	CalcTask ct1=new CalcTask(1,12);
	CalcTask ct2=new CalcTask(2,11);
	CalcTask ct3=new CalcTask(3,6);
	
	rc.AddTask(ct1);
	rc.AddTask(ct2);
	rc.AddTask(ct3);
	
	for(int i=0;i<3;i++)
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
