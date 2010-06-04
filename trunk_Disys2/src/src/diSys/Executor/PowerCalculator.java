package diSys.Executor;

import diSys.Common.FileManager;
import java.util.prefs.Preferences;
import java.io.IOException;
import diSys.Common.Common;
 

public class PowerCalculator {
	//TODO: divide by linux\windows, and read processor power from reg or somehow.
	private final int Giga = 1000;
	private static final int defaultFreqGHZ = 1000;
	
	private static int processorFreqGHZ = getProcessorFreqGHZ();
	private static int numOfProcessors = getNumOfProcessors();
	private static int getNumOfProcessors()
	{
		return Runtime.getRuntime().availableProcessors();
	}
	private static int getProcessorFreqGHZ()
	{
		int $ = defaultFreqGHZ;
		try 
		{
			String fileName = "systeminfo.txt";
			Common.runCmd("systeminfo > " + fileName);
			
			try {
				Thread.sleep(1000*15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] lines = FileManager.ReadLines(fileName);
			for(String line : lines)
			{
				String[] words = line.split(" ");
				for(String word : words)
					if(word.contains("~"))
						$ = Integer.parseInt(word.substring(1));
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return $;
	}
	private int getBenchmarkTimeSeconds()
	{
		return SystemBenchmark.run();
	}
	
	private String readReg(String reg)
	{
		Preferences userPref = Preferences.userRoot();
		System.out.println(userPref.getInt("HKEY_LOCAL_MACHINE\\HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\0\\~MHz", -1));
		return "";
	}
	
	
	public double getEP()
	{
		return numOfProcessors*processorFreqGHZ*1.0/getBenchmarkTimeSeconds()*Giga;
	}
	
	public static void main(String[] args)
	{
		PowerCalculator pc = new PowerCalculator();
		System.out.println(pc.readReg(""));
	}
}
