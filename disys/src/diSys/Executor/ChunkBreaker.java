package diSys.Executor;

import java.util.concurrent.BlockingQueue;

import diSys.Common.Chunk;
import diSys.Common.Item;
import diSys.Common.RemoteItem;
import diSys.SystemManager.AutoUpdateTask;
import diSys.SystemManager.CleanExitTask;
import diSys.WorkersSystem.WorkER.AWorker;


/**
 * Reads chunks from A Queue and break them into Items and put them in Items Queue 
 * @author saeed
 *
 * @param <ITEM>
 */
public class ChunkBreaker<ITEM extends Item> extends AWorker<Chunk<ITEM>,RemoteItem<ITEM>> {

	boolean updateState=false;
	@SuppressWarnings("unchecked")
	public ChunkBreaker(BlockingQueue<Chunk<ITEM>> wi, BlockingQueue<RemoteItem<ITEM>> rq, ExecuterSystem es) {
		super(wi, rq);
		this.es = es;
	}
	
	@SuppressWarnings("unchecked")
	ExecuterSystem es;

	@SuppressWarnings("unchecked")
	@Override
	public RemoteItem<ITEM> doItem(Chunk<ITEM> chunk) {
		Item[] items=chunk.getItems();
		diSys.Common.Logger.TraceInformation("New Chunk size["+items.length+"] recived from ["+chunk.getClientRemoteInfo()+"]");
		for(Item item:items){
			if(item instanceof CleanExitTask)
			{
				diSys.Common.Logger.TraceInformation("Exit Command [Task] recieved");
				//TODO: free all buffers
				//System.out.print("Executer has been killed, exiting...");
				//System.exit(0);
				diSys.Common.Logger.TerminateSystem("Executer has been killed, exiting...", null);
			}
			if(item instanceof AutoUpdateTask)
			{
				diSys.Common.Logger.TraceInformation("Update Command [Task] recieved , update to :"+((AutoUpdateTask)item).updates.ExecuterClassName());
				try{
					if(es.GetVersion()==((AutoUpdateTask)item).version){
						diSys.Common.Logger.TraceInformation("System at version "+es.GetVersion()+"is up to date no need to update . ");
						return null;
					}
					if(!updateState){
					updateState=true;
					es.PrepareToUpdate((AutoUpdateTask)item);
					
					}else{
						
						
						diSys.Common.Logger.TraceWarning("Executer Already in update state", null);
						return null;
					}
					//es.updateExecuters((AutoUpdateTask)item);
					
					//es.GetItemReciver().Version=((AutoUpdateTask)item).version;
				}
				catch(Exception e){
					updateState=false;
					diSys.Common.Logger.TraceWarning("Executer System Could not update executers", e);
				}
				return null;
			}
			RemoteItem ri=new  RemoteItem(item,chunk.getClientRemoteInfo());
			this.Results.add(ri);
		}
		return null;
	}
}
