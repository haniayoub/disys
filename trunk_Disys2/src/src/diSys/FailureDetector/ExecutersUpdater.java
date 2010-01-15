package diSys.FailureDetector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import diSys.Common.RMIRemoteInfo;
import diSys.Networking.RMIItemCollector;
import diSys.Networking.RemoteItemReceiver;

public class ExecutersUpdater implements Runnable {
	
	private boolean done = false;
	private int sleepTime = 1000*60*10; //10 minutes

	private String executersFile;
	private LinkedList<RMIRemoteInfo> irs;
	private LinkedList<RMIRemoteInfo> rcs;
	
	public ExecutersUpdater(String executersFile, 
							LinkedList<RMIRemoteInfo> irs, 
							LinkedList<RMIRemoteInfo> rcs, 
							RMIRemoteInfo sys)
	{
		this.executersFile = executersFile;
		this.irs = irs;
		this.rcs = rcs;
		FailureDetectorLog.getLog().TraceInformation("ExecutersUpdater has been created");
	}
	
	public synchronized void run() 
	{
		FailureDetectorLog.getLog().TraceInformation("ExecutersUpdater started...");
		while (!done)
		{
			try {updateExecuters();} catch (IOException e) {e.printStackTrace();}
			sleep(sleepTime);
		}
	}
	
	private synchronized void updateExecuters() throws IOException 
	{
		FailureDetectorLog.getLog().TraceInformation("Updating Executers...");
		FileReader fr;
		try 
		{ 
			fr = new FileReader(executersFile); 
		} 
		catch (FileNotFoundException e) 
		{
			FailureDetectorLog.getLog().TraceError(executersFile + " is not a file.");
			e.printStackTrace();
			return;
		}
		BufferedReader reader = new BufferedReader(fr);
		String line = null;
	
		LinkedList<RMIRemoteInfo> tmpIrs = new LinkedList<RMIRemoteInfo>();
		LinkedList<RMIRemoteInfo> toRemoveFromIrs = new LinkedList<RMIRemoteInfo>();
		LinkedList<RMIRemoteInfo> toAddToIrs = new LinkedList<RMIRemoteInfo>();
		LinkedList<RMIRemoteInfo> tmpRcs = new LinkedList<RMIRemoteInfo>();
		LinkedList<RMIRemoteInfo> toRemoveFromRcs = new LinkedList<RMIRemoteInfo>();
		LinkedList<RMIRemoteInfo> toAddToRcs = new LinkedList<RMIRemoteInfo>();
		while ((line = reader.readLine()) != null) 
		{
			String[] lineArgs = line.split(" ");
			String ip = lineArgs[0];
			int port1 =Integer.parseInt(lineArgs[1]);
			int port2 =Integer.parseInt(lineArgs[2]);
			
			tmpIrs.add(new RMIRemoteInfo(ip, port1, RemoteItemReceiver.GlobalId));
			tmpRcs.add(new RMIRemoteInfo(ip, port2, RMIItemCollector.GlobalId));
		}
		for(RMIRemoteInfo ir : tmpIrs)
			if(!irs.contains(ir))
			{
				toAddToIrs.add(ir);
				FailureDetectorLog.getLog().TraceInformation("    Executer IR added: " + ir.Ip() + " " + ir.Port());
			}
		for(RMIRemoteInfo ir : irs)
			if(!tmpIrs.contains(ir))
			{
				toRemoveFromIrs.add(ir);
				FailureDetectorLog.getLog().TraceInformation("    Executer IR removed: " + ir.Ip() + " " + ir.Port());
			}
		for(RMIRemoteInfo rc : tmpRcs)
			if(!rcs.contains(rc))
			{
				toAddToRcs.add(rc);
				FailureDetectorLog.getLog().TraceInformation("    Executer RC added: " + rc.Ip() + " " + rc.Port());
			}
		for(RMIRemoteInfo rc : rcs)
			if(!tmpRcs.contains(rc))
			{
				toRemoveFromRcs.add(rc);
				FailureDetectorLog.getLog().TraceInformation("    Executer RC removed: " + rc.Ip() + " " + rc.Port());
			}
		
		irs.addAll(toAddToIrs);
		irs.removeAll(toRemoveFromIrs);
		rcs.addAll(toAddToRcs);
		rcs.removeAll(toRemoveFromRcs);
		
		FailureDetectorLog.getLog().TraceInformation("Finished Updating Executers...");
	}
	
	private  void sleep(final int period) 
	{
		try {
			Thread.sleep(period);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void halt() 
	{
		done = true;
	}	
}
