import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import diSys.Client.RemoteClient;
import diSys.Common.SystemUpdates;


public class Main {
	static Random generator = new Random( 19580427 );
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		System.out.println(java.lang.ClassLoader.getSystemClassLoader().getResource("."));
		String managerAddress="localhost";//args[0];
		
		SystemUpdates updates=new SystemUpdates("UpdateJar.jar",MyCAlculator.class.getName());
		int p=5000;//Integer.parseInt(args[1]);
		RemoteClient<CalcTask, CalcResult> rc=new RemoteClient<CalcTask, CalcResult>(managerAddress,p,3,updates,true);
		rc.Start();
		LinkedList<CalcTask> l=new LinkedList<CalcTask>();
		for (int i=0;i<0;i++){
			l.add(new CalcTask(i));
		}
		for (CalcTask c:l){
		rc.AddTask(c);
		}
		
		for(int i=0;i<l.size();i++)
			try {
				System.out.println(rc.GetResult(100, TimeUnit.SECONDS));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
		//System.in.read();
	
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
