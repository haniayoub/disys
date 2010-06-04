package diSys.Client;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask; 

import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.Item;
import diSys.Networking.NetworkCommon;

public class FailureDetector extends TimerTask {

	private int interval;
	private ClientSystem client;
	private HashMap<ExecuterRemoteInfo, LinkedList<Item>> tasksDB = 
		new HashMap<ExecuterRemoteInfo, LinkedList<Item>>();
	
	public FailureDetector(int interval, ClientSystem client) 
	{
		this.interval = interval;
		this.client = client;
		diSys.Common.Logger.TraceInformation("FailureDetector has been initialized");
	}
	
	public synchronized void start()
	{
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, new Date(), interval);
		diSys.Common.Logger.TraceInformation("FailureDetector is now running (interval: " + interval + " seconds)");
	}
	
	public synchronized void add(Item task, ExecuterRemoteInfo itemRecieverInfo)
	{
		if(!tasksDB.containsKey(itemRecieverInfo))
			tasksDB.put(itemRecieverInfo, new LinkedList<Item>());
		
		LinkedList<Item> tasks = tasksDB.get(itemRecieverInfo);
		tasks.add(task);
	}
	
	public synchronized void remove(Item res)
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
	public synchronized void run() {
		diSys.Common.Logger.TraceInformation("FailureDetector::Run()");
		Set<ExecuterRemoteInfo> executers = tasksDB.keySet();
		for(ExecuterRemoteInfo eri : executers)
			if(!isOnline(eri))
				reschedule(eri);
	}

	private synchronized void reschedule(ExecuterRemoteInfo eri) 
	{
		diSys.Common.Logger.TraceInformation("Trying to reschedule tasks on Executer: " + eri.getName());
		LinkedList<Item> tasks = tasksDB.get(eri);
		for(Item task : tasks)
		{
			diSys.Common.Logger.TraceInformation("Rescheduling task: "+ task.getId());
			client.AddTask(task);
		}
		tasksDB.remove(eri);
	}

	private synchronized boolean isOnline(ExecuterRemoteInfo eri) 
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
}
