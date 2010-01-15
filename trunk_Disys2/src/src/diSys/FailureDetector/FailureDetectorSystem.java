package diSys.FailureDetector;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import diSys.Common.RMIRemoteInfo;
import diSys.SystemManager.SystemManager;

public class FailureDetectorSystem {
	
	private static LinkedList<RMIRemoteInfo> irs = new LinkedList<RMIRemoteInfo>();
	private static LinkedList<RMIRemoteInfo> rcs = new LinkedList<RMIRemoteInfo>();
	private static RMIRemoteInfo sys = null;
	
	private static Thread checker;
	private static Thread updater;

	public static void main(String[] args) throws IOException 
	{
		if(args.length < 3)
		{
			PrintUsage();
			return;
		}
		if(!new File(args[2]).isFile())
		{
			FailureDetectorLog.getLog().TraceError(args[2] + " is not a file.");
			PrintUsage();
			return;
		}
		
		FailureDetectorLog.getLog().TraceInformation("Failure detector Started...");
		sys = new RMIRemoteInfo(args[0],Integer.parseInt(args[1]), SystemManager.GlobalID);
		FailureDetectorLog.getLog().TraceInformation("    System Manager: " + sys.Ip() + " " + sys.Port() + " added");
		updater = new Thread(new ExecutersUpdater(args[2], irs, rcs, sys));
		checker = new Thread(new HeartBeatChecker(irs, rcs, sys));
		
		updater.start();
		checker.start();
	}

	private static void PrintUsage()
	{
		System.out.println("-------------------------------[Failure Detector]--------------------------");
		System.out.println("[FailureDetector] [SystemManagerAddress] [SystemManagerPort][ExecutersFile]");
		System.out.println("[SystemManagerAddress]: system manager IP or Machine name");
		System.out.println("[SystemManagerPort]   : system manager port");
		System.out.println("ExecuterFile format:");
		System.out.println("  line<n>:  ExecuterIP      Port1 Port2");
		System.out.println("---------------------------------------------------------------------------");
		System.out.println();
	}
}
