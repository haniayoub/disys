package diSys.Common;

import java.io.IOException;

public abstract class Common {
	public static int getRandom(int min, int max)
	{
		int diff = max - min + 1;
		int rand = (int)(Math.random()*1000000);
		rand = rand % diff;
		rand = rand + min;
		return rand;
	}
	
	public static void runCmd(String cmd) throws IOException
	{
		Runtime rt = Runtime.getRuntime();
		String command = "cmd /c start " + cmd;
		rt.exec(command);
	}
/*	
	public static void sendMail(String to, String msg)
	{
		String mailSender = "C:\\Run_Queue_System\\Mail_Sender\\Mail_Sender.exe";
		try 
		{
			runCmd(mailSender + " " +
				   "\"" + msg + "\"" + " " + 
				   "\"RunQueue\"" + " " +
				   "\"" + "DisysExecuterError" + "\"" + " " +
				   "\"" + to + "\"");
		} 
		catch (IOException e) 
		{
			System.out.println("Could not send mail");
		}
	}
*/
}