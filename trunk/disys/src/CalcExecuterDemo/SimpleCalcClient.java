package CalcExecuterDemo;


import java.util.Random;

import Client.ClientSystem;
/*
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import Common.Chunk;
import Networking.IRemoteItemReceiver;
*/
public class SimpleCalcClient {
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	static Random generator = new Random( 19580427 );
	public static void main(String[] args) throws InterruptedException {
	ClientSystem<CalcTask, CalcResult> cs=new ClientSystem<CalcTask, CalcResult>("LocalHost",3000);
	for(int i=0;i<5392;i++) cs.tasks.add(CreateRandomTask());
	Thread.sleep(1000);
	for(int i=0;i<1420;i++) cs.tasks.add(CreateRandomTask());
	
	Thread.sleep(10000);
	System.out.println("Client Done!");
	}
	private static CalcTask CreateRandomTask(){
		
		final CalcTask ct=new CalcTask(generator.nextInt(1000));
			ct.x=generator.nextInt(1000);
			ct.y=generator.nextInt(1000);
		return ct;
		}

	/*@SuppressWarnings("unchecked")
	public static void main(String[] args){
		 // Assign security manager
	    //if (System.getSecurityManager() == null)
	    //{
	      //  System.setSecurityManager   (new RMISecurityManager());
	    //}
		IRemoteItemReceiver<Chunk<CalcTask>> chunkReciver=null;
	    // Call registry for PowerService
	    try {
	    	int port=Integer.parseInt(args[1]);
	    	chunkReciver = (IRemoteItemReceiver<Chunk<CalcTask>>) 
	    	
			Naming.lookup("rmi://" + args[0] +":"+port +"/itemReciever0");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i=0;i<10;i++){
			Chunk<CalcTask> chunk=CreateRandomChunk();
			try {
				chunkReciver.Add(chunk);
				//Thread.sleep(100);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	@SuppressWarnings("unused")
	private static Chunk<CalcTask> CreateRandomChunk(){
	final LinkedList<CalcTask> list=new LinkedList<CalcTask>();
	final Random generator = new Random( 19580427 );
	final int size=generator.nextInt(10)+3;
	for(int i=0;i<size;i++){
		final CalcTask ct=new CalcTask(i);
		ct.x=generator.nextInt(1000);
		ct.y=generator.nextInt(1000);
		list.add(ct);
	}
	final CalcTask[] array=new CalcTask[list.size()];
	for (int i=0;i<array.length;i++){
	array[i]=list.get(i);
	}
	final Chunk<CalcTask> chunk=new Chunk<CalcTask>(10,null,null,array);
	return chunk;
	}*/
	
	
}
