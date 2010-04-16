package diSys.Executor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BenchMarkTask extends TimerTask {
	private DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
	private int interval;
	private ExecuterSystem es;
	public BenchMarkTask(int interval,ExecuterSystem es){
		this.interval = interval;
		this.es = es;
	}
	public void start(){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, new Date(), 1000*interval);
	}
	public static void main(String[] args) {
		// 
		// Create an instance of TimerTask implementor.
		//
		BenchMarkTask task = new BenchMarkTask(3,null);
		task.start();
		
	}
	
	/**
	 * This method is the implementation of a contract defined in the TimerTask
	 * class. This in the entry point of the task execution.
	 */
	public void run() {
		//
		// To make the example simple we just print the current time.
		//	
		
		System.out.println("Running benchmark :" + formatter.format(new Date()));
	}
 }
