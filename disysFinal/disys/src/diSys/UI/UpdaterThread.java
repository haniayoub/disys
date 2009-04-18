package diSys.UI;

public class UpdaterThread {
	private int period;
	private Thread thread;
	private boolean done=false;
	private boolean pause=false;
	private boolean paused=false;
	public UpdaterThread(int period){
		this.period=period;
		thread=new Thread(new Runnable(){

			@Override
			public void run() {
				ThreadRun();
			}});
	}
	
	public void start(){
		thread.start();
	}
	public void pause(){
		pause=true;
		while(!paused) Thread.yield();
	}
	public void resume(){
		pause=false;
		while(paused) Thread.yield();
	}
	public void stop(){
		thread.start();
		done=true;
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void ThreadRun(){
		while(!done){
			
			while(pause){
				paused=true;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			pause=false;
			Work();
			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void Work(){
		SystemManagerUI.UpdateUI();
	}

	public boolean IsRunning() {
		// TODO Auto-generated method stub
		return thread.isAlive();
	}
	
}
