package diSys.Networking;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;

import diSys.Common.Item;
import diSys.Executor.ExecuterRemoteData;


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
		
	private BlockingQueue<ITEM> recievedItems;
	public RemoteItemReceiver(BlockingQueue<ITEM> itemsQueue,int port) throws Exception {
		super(GlobalId,port);
		recievedItems = itemsQueue;
		
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
		return erd;
	}
}
