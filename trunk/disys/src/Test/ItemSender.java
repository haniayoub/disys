package Test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import CalcExecuterDemo.CalcTask;
import Common.Chunk;


public class ItemSender {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		IItemReciever<CalcTask> chunkReciver=null;
	    // Call registry for PowerService
	    try {
	    	int port=Integer.parseInt(args[1]);
	    	chunkReciver = (IItemReciever) 
	    	
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
			CalcTask ct=new CalcTask(i);
			ct.x=i;
			ct.y=1;
			try {
				chunkReciver.Add(ct);
				Thread.sleep(500);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
