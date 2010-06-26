package diSys.Executor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.DateFormat;

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
		return $;
	}
	private int getBenchmarkTimeSeconds()
	{
		return SystemBenchmark.run();
	}
	
	
	public double getEP()
	{
		return numOfProcessors*processorFreqGHZ*1.0/getBenchmarkTimeSeconds()*Giga;
	}
	
	public static void main(String[] s)
	{
		DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
		PowerCalculator pc= new PowerCalculator();
		while (true){
			System.out.println("Running benchmark :" + formatter.format(new Date()));
			double ep =pc.getEP();
			System.out.println("Executer Power is: " + ep);
			System.out.println("Done :" + formatter.format(new Date()));
		}
	}
}
