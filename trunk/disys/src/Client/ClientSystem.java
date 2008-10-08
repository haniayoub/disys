package Client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import WorkersSystem.WorkER.WorkerSystem;
import Common.Chunk;
import Common.Item;
import Common.RemoteInfo;

public class ClientSystem<TASK extends Item,RESULT extends Item> {

	RemoteInfo RemoteSysManagerInfo;
	public BlockingQueue<TASK> tasks = 
		new LinkedBlockingQueue<TASK>();
	public BlockingQueue<RESULT> results= 
		new LinkedBlockingQueue<RESULT>();
	public BlockingQueue<Chunk<TASK>> taskChunks = 
		new LinkedBlockingQueue<Chunk<TASK>>();
	WorkerSystem ws=new WorkerSystem();
	ChunkCreator<TASK> chunkCreatorWorker=new ChunkCreator<TASK>(tasks,taskChunks,1000); 
	//ItemPrinter<Chunk<TASK>> itemPrinterWorker=new ItemPrinter<Chunk<TASK>>(taskChunks,null);
	ChunkScheduler<TASK> chunkScheduler;

	
	public ClientSystem(String SysManagerAddress,int sysManagerport) {
		super();
		RemoteSysManagerInfo=new RemoteInfo(SysManagerAddress,sysManagerport,"systemManager0");
		chunkScheduler=new ChunkScheduler<TASK>(RemoteSysManagerInfo,taskChunks,taskChunks);
		ws.add(chunkCreatorWorker,1);
		//ws.add(itemPrinterWorker, 1);
		ws.add(chunkScheduler,1);
		ws.startWork();
	}
	}
