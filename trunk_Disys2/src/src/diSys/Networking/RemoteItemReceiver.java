package diSys.Networking;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import diSys.Common.ExecuterStatistics;
import diSys.Common.Item;
import diSys.Common.ItemInfo;
import diSys.Executor.ExecuterRemoteData;
import diSys.Executor.ExecuterSystem;


/**
 * ITEM receiver is RMI object which waits for clients to send to it items and
 * put them in a queue
 * 
 * @author saeed
 * 
 * @param <ITEM>
 */
@SuppressWarnings("serial")
public class RemoteItemReceiver<ITEM extends Item> extends RMIObjectBase
		implements IRemoteItemReceiver<ITEM> {
	public int Version=-1; 
	public static final String GlobalId = "itemReciever";
	public final int BufferCapacity = 1000;
	public double executionPower = -1;
	private PriorityBlockingQueue<ITEM> tasks;
	private BlockingQueue<ITEM> recievedItems;
	@SuppressWarnings("unchecked")
	private ExecuterSystem es;
	@SuppressWarnings("unchecked")
	public RemoteItemReceiver(BlockingQueue<ITEM> itemsQueue, PriorityBlockingQueue<ITEM> tasks ,int port, ExecuterSystem es) throws Exception {
		super(GlobalId,port);
		this.recievedItems = itemsQueue;
		this.tasks = tasks;
		this.es = es;
		//Logger.TraceInformation("++++++++++--++++++++++++++++"+this.getClass().getClassLoader().toString());
		
	}

	@Override
	public void Add(ITEM item) throws RemoteException {
		item.setOwner(diSys.Networking.NetworkCommon.GetClientHost());
		recievedItems.add(item);
		}

	@Override
	public RemoteData getExecuterData() throws RemoteException {
		ExecuterRemoteData erd = new ExecuterRemoteData();
		erd.numOfTasks = recievedItems.size();
		erd.Version = this.Version; 
		erd.log=diSys.Common.Logger.logTracer.toString();
		erd.BC = BufferCapacity;
		erd.BS = tasks.size();
		erd.EP = executionPower; 
		return erd;
	}

	@Override
	public LinkedList<ItemInfo> getQueueInfo() throws RemoteException {
		LinkedList<ItemInfo> $= new LinkedList<ItemInfo>();
		LinkedList<ITEM> tmp= new LinkedList<ITEM>();
		while(!tasks.isEmpty())
		{
			ITEM i = tasks.poll();
			$.add(new ItemInfo(i.toString(), i.hashCode(), i.getId(), i.getPriority()));
			tmp.add(i);
		}
		while(!tmp.isEmpty())
		{
			tasks.put(tmp.poll());
		}
		return $;
	}
	
	public void changePriority(int itemHashCode, int newPriority)throws RemoteException 
	{
		ITEM tmp = null;
		for(ITEM i : tasks)
		{
			if(i.hashCode() == itemHashCode)
			{
				tmp = i;
			}
		}
		tasks.remove(tmp);
		tmp.setPriority(newPriority);
		tasks.offer(tmp);
	}

	@Override
	public void removeTask(int itemHashCode) throws RemoteException {
		ITEM tmp = null;
		for(ITEM i : tasks)
		{
			if(i.hashCode() == itemHashCode)
			{
				tmp = i;
			}
		}
		tasks.remove(tmp);
	}

	@Override
	public ItemInfo getCurrentTask() throws RemoteException {
		Item i = es.getCurrentTask();
		if(i == null)
			return null;
		else
			return (new ItemInfo(i.toString(), i.hashCode(), i.getId(), i.getPriority()));
	}
/*
	@Override
	public void moveDownTask(int itemHashCode) throws RemoteException {
		ITEM toMove = null;
		ITEM toMoveAfter = null;
		boolean flag = false;
		for(ITEM i : tasks)
		{
			if(i.hashCode() == itemHashCode)
			{
				toMove = i;
				flag = true;
			}
			else if(flag == true)
			{
				toMoveAfter = i;
				flag = false;
				break;
			}
		}

		PriorityBlockingQueue<ITEM> tmpTasks = new PriorityBlockingQueue<ITEM>(
				100, new Comparator<ITEM>() {
					public int compare(ITEM t1, ITEM t2) {
						if(t1.getPriority() == t2.getPriority()) return 0;
						return t1.getPriority() > t2.getPriority() ? 1 : -1;
					}
				});
		for(ITEM i : tasks)
		{
			if(i.hashCode() == toMove.hashCode())
			{
				tasks.remove(i);
				tmpTasks.offer(toMoveAfter);
			}
			else if(i.hashCode() == toMoveAfter.hashCode())
			{
				tasks.remove(i);
				tmpTasks.offer(toMove);
			}
			else
			{
				tmpTasks.offer(i);
				tasks.remove(i);
			}
		}
		for(ITEM i : tmpTasks)
		{
			tasks.offer(i);
		}
	}

	@Override
	public void moveUpTask(int itemHashCode) throws RemoteException {
		ITEM toMove = null;
		ITEM toMoveBefore = null;
		for(ITEM i : tasks)
		{
			toMoveBefore = toMove;
			toMove = i;
			if(i.hashCode() == itemHashCode)
				break;
		}
		PriorityBlockingQueue<ITEM> tmpTasks = new PriorityBlockingQueue<ITEM>(
				100, new Comparator<ITEM>() {
					public int compare(ITEM t1, ITEM t2) {
						if(t1.getPriority() == t2.getPriority()) return 0;
						return t1.getPriority() > t2.getPriority() ? 1 : -1;
					}
				});
		for(ITEM i : tasks)
		{
			if(i.hashCode() == toMoveBefore.hashCode())
			{
				tasks.remove(i);
				tmpTasks.offer(toMove);
			}
			else if(i.hashCode() == toMove.hashCode())
			{
				tasks.remove(i);
				tmpTasks.offer(toMoveBefore);
			}
			else
			{
				tmpTasks.offer(i);
				tasks.remove(i);
			}
		}
		for(ITEM i : tmpTasks)
		{
			tasks.offer(i);
		}
	}
*/
	@Override
	public int getMaxPriority() throws RemoteException {
		if(tasks.isEmpty())
			return 50;
		int res = Integer.MIN_VALUE;
		for(Item i : tasks)
		{
			res = (i.getPriority() > res) ? i.getPriority() : res; 
		}
		return res;
	}

	@Override
	public int getMinPriority() throws RemoteException {
		if(tasks.isEmpty())
			return 50;
		int res = Integer.MAX_VALUE;
		for(Item i : tasks)
		{
			res = (i.getPriority() < res) ? i.getPriority() : res; 
		}
		return res;
	}

	@Override
	public ExecuterStatistics GetStatistics() throws RemoteException {
		// TODO Auto-generated method stub
		es.ExecStatistics.BefferSize = es.chunkReceiver.getQueueInfo().size();
		es.ExecStatistics.BufferCapacity = es.chunkReceiver.BufferCapacity;
		es.ExecStatistics.numOfWorkerThreads = ExecuterSystem.numOfThreads;
		return es.ExecStatistics;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void ResetStatistics() throws RemoteException {
		diSys.Common.Logger.TraceInformation("Resetting Executer statistics ...");
		es.ExecStatistics = new ExecuterStatistics();
	}	
	
	
}