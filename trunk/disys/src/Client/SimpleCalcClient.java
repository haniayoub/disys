package Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Random;

import CalcExecuterDemo.*;
import Common.Chunk;
import Networking.IRemoteChunkReceiver;

public class SimpleCalcClient {
	public static void main(String[] args){
		 // Assign security manager
	    //if (System.getSecurityManager() == null)
	    //{
	      //  System.setSecurityManager   (new RMISecurityManager());
	    //}
		IRemoteChunkReceiver chunkReciver=null;
	    // Call registry for PowerService
	    try {
	    	int port=Integer.parseInt(args[1]);
	    	chunkReciver = (IRemoteChunkReceiver) 
	    	
			Naming.lookup("rmi://" + args[0] +":"+port +"/ChunkReciever0");
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
				chunkReciver.Add("chunk");
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
	for(int i=0;i<=size;i++){
		final CalcTask ct=new CalcTask(i);
		ct.x=generator.nextInt(1000);
		ct.y=generator.nextInt(1000);
		list.add(ct);
	}
	final CalcTask[] array=new CalcTask[list.size()];
	for (int i=0;i<array.length;i++){
	array[i]=list.get(i);
	}
	final Chunk<CalcTask> chunk=new Chunk<CalcTask>(null,null,array);
	return chunk;
	}
	
	
}
