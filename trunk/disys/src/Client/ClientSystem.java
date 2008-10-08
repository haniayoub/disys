package Client;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import WorkersSystem.WorkER.WorkerSystem;

import CalcExecuterDemo.CalcTask;
import CalcExecuterDemo.CalcResult;
import Common.Chunk;
import Common.Item;
import Common.ItemPrinter;
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
	ChunkCreator<TASK> chunkCreatorWorker=new ChunkCreator<TASK>(tasks,taskChunks,10); 
	ItemPrinter<Chunk<TASK>> itemPrinterWorker=new ItemPrinter<Chunk<TASK>>(taskChunks,null);
	ChunkScheduler<TASK> chunkScheduler;

	
	public ClientSystem(String SysManagerAddress,int port) {
		super();
		RemoteSysManagerInfo=new RemoteInfo(SysManagerAddress,port,"systemManager0");
		chunkScheduler=new ChunkScheduler<TASK>(RemoteSysManagerInfo,taskChunks,taskChunks);
		ws.add(chunkCreatorWorker,1);
		ws.add(itemPrinterWorker, 1);
		ws.add(chunkScheduler,1);
		ws.startWork();
	}
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
	ClientSystem<CalcTask, CalcResult> cs=new ClientSystem<CalcTask, CalcResult>("LocalHost",3000);
	for(int i=0;i<94;i++) cs.tasks.add(CreateRandomTask());
	Thread.sleep(10000);
	}
	private static CalcTask CreateRandomTask(){
		final Random generator = new Random( 19580427 );
		final CalcTask ct=new CalcTask(generator.nextInt(1000));
			ct.x=generator.nextInt(1000);
			ct.y=generator.nextInt(1000);
		return ct;
		}
}
