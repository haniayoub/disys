package diSys.Executor;

import diSys.Common.FileLogger;

public class HeartBeat extends Thread
{
	HeartBeat(String logFile)
	{
		this.fileLogger = new FileLogger(logFile, true);
	}
	private boolean stop = false;
	private int SleepTime = 1000 * 60 * 5;
	private FileLogger fileLogger;
	public void run()
	{
		while(!stop)
		{
			fileLogger.TraceInformation("Heart Beat... Alive...");
			try 
			{
				Thread.sleep(SleepTime);
			} 
			catch (InterruptedException e) 
			{
				System.out.println("Could not sleep...");
				e.printStackTrace();
			}
		}
	}
	public void stopHeartBeat()
	{
		stop = true;
	}
}
