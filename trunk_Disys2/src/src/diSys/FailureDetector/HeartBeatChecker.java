package diSys.FailureDetector;

import java.util.LinkedList;

import diSys.Common.MailPoster;
import diSys.Common.RMIRemoteInfo;
import diSys.Networking.NetworkCommon;

public class HeartBeatChecker implements Runnable{

	private LinkedList<RMIRemoteInfo> irs;
	private LinkedList<RMIRemoteInfo> rcs;
	private RMIRemoteInfo sys;
	
	private boolean done;
	private int sleepTime = 1000*60*10; //10 minutes
	
	public HeartBeatChecker(LinkedList<RMIRemoteInfo> irs, LinkedList<RMIRemoteInfo> rcs, RMIRemoteInfo sys) 
	{
		this.irs = irs;
		this.rcs = rcs;
		this.sys = sys;
		FailureDetectorLog.getLog().TraceInformation("HeartBeat has been created");
	}

	public synchronized void run() 
	{
		FailureDetectorLog.getLog().TraceInformation("HeartBeat started...");
		while (!done)
		{
			heartBeat();
			sleep(sleepTime);
		}
	}
	
	private synchronized void heartBeat() 
	{
		FailureDetectorLog.getLog().TraceInformation("HeartBeating all executers...");
		for(RMIRemoteInfo ir : irs)
		{
			if(NetworkCommon.loadRMIRemoteObject(ir) == null)
				offlineAction(ir);
			else
				onlineAction(ir);
		}
		for(RMIRemoteInfo rc : rcs)
		{
			if(NetworkCommon.loadRMIRemoteObject(rc) == null)
				offlineAction(rc);
			else
				onlineAction(rc);
		}
		if(NetworkCommon.loadRMIRemoteObject(sys) == null)
			offlineAction(sys);
		else
			onlineAction(sys);
		FailureDetectorLog.getLog().TraceInformation("Finished HeartBeating executers...");
	}

	public synchronized void halt()
	{
		done = true;
	}
	
	private void sleep(final int period) 
	{
		try {
			Thread.sleep(period);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void onlineAction(RMIRemoteInfo rri) 
	{
		FailureDetectorLog.getLog().TraceInformation("    " + rri.Ip() + " " + rri.Port() + " is online...");
		if(rri.isOnline() == false)
		{
			rri.setOnline(true);
			String msg = "Executer: " + rri.Ip() + " Port: " + rri.Port() + 
						 " is now ONLINE";
			MailPoster.SendMail("Failure Detector: Online Executer", msg);
		}
	}

	private  void offlineAction(RMIRemoteInfo rri) 
	{
		FailureDetectorLog.getLog().TraceInformation("    " + rri.Ip() + " " + rri.Port() + " is offline...");
		if(rri.isOnline() == true)
		{
			rri.setOnline(false);
			String msg = "Executer: " + rri.Ip() + " Port: " + rri.Port() + 
						 " is OFFLINE for " + sleepTime/(1000*60) + "minutes now." + MailPoster.newLine + 
						 "Tasks sent to this executer will be missed and won't run";		
			MailPoster.SendMail("Failure Detector: Offline Executer", msg);
		}
	}
}
