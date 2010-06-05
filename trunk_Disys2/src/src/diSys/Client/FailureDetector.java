package diSys.Client;

import java.util.Date;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask; 
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;
import diSys.Networking.NetworkCommon;

public class FailureDetector<TASK extends Item> extends TimerTask {

	private int interval;
	private PriorityBlockingQueue<TASK> systemTasks;
	private ConcurrentHashMap<ExecuterRemoteInfo, LinkedList<Item>> tasksDB = 
		new ConcurrentHashMap<ExecuterRemoteInfo, LinkedList<Item>>();
	
	public FailureDetector(int interval, PriorityBlockingQueue<TASK> systemTasks) 
	{
		this.interval = interval;
		this.systemTasks = systemTasks;
	}
	
	public void start()
	{
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, new Date(), interval);
		diSys.Common.Logger.TraceInformation("FailureDetector is now running (interval: " + interval + " seconds)");
	}
	
	public void add(Item task, ExecuterRemoteInfo itemRecieverInfo)
	{
		if(!tasksDB.containsKey(itemRecieverInfo))
			tasksDB.put(itemRecieverInfo, new LinkedList<Item>());
		
		LinkedList<Item> tasks = tasksDB.get(itemRecieverInfo);
		tasks.add(task);
	}
	
	public void remove(Item res)
	{	
		Set<ExecuterRemoteInfo> executers = tasksDB.keySet();
		for(ExecuterRemoteInfo eri : executers)
		{
			LinkedList<Item> tasks = tasksDB.get(eri);
			for(Item t : tasks)
				if(t.getId() == res.getId())
					tasks.remove(t);
		}
	}
	
	@Override
	public void run() {
		diSys.Common.Logger.TraceInformation("Checking online\\offline executers...");
		Set<ExecuterRemoteInfo> executers = tasksDB.keySet();
		for(ExecuterRemoteInfo eri : executers)
			if(!isOnline(eri))
				reschedule(eri);
	}

	@SuppressWarnings("unchecked")
	private void reschedule(ExecuterRemoteInfo eri) 
	{
		diSys.Common.Logger.TraceInformation("Trying to reschedule tasks on Executer: " + eri.getName());
		LinkedList<Item> tasks = tasksDB.get(eri);
		for(Item task : tasks)
		{
			diSys.Common.Logger.TraceInformation("Rescheduling task: "+ task.getId());
			systemTasks.add((TASK)task);
		}
		tasksDB.remove(eri);
	}

	private boolean isOnline(ExecuterRemoteInfo eri) 
	{
		if(	NetworkCommon.loadRMIRemoteObject(eri.getItemRecieverInfo()) == null)
		{
			diSys.Common.Logger.TraceWarning("Executer: " + eri.getName() + " Port: " + eri.getItemRecieverInfo() + " is down", null);
			return false;
		}
		if(NetworkCommon.loadRMIRemoteObject(eri.getResultCollectorInfo()) == null)
		{
			diSys.Common.Logger.TraceWarning("Executer: " + eri.getName() + " Port: " + eri.getResultCollectorInfo() + " is down", null);
			return false;
		}
		return true;
	}

	public void Stop() {
		this.cancel();
		
	}
}
