package WorkersSystem.WorkER;
import java.util.concurrent.BlockingQueue;

import Common.Item;


public abstract class AWorker<T extends Item,R extends Item> implements Runnable , Cloneable{

	public BlockingQueue<T> WorkItems;
	public BlockingQueue<R> Results;
	private boolean stop=false;
	public AWorker(BlockingQueue<T> wi,BlockingQueue<R> rq){
	WorkItems=wi;
	Results=rq;
	}
	public void RunWorker(){
		System.out.println("Running new Worker : "+WorkItems.size());
		while(!stop){
			R res;
			T task;
			try {
			task=WorkItems.take();
			} catch (InterruptedException e) {
			continue;
			}
			if (task==null)continue;
			
			res=doItem(task);
			if (res==null||Results==null) continue;
			while(true){
			try {
				Results.put(res);
				break;
			} catch (InterruptedException e) {
				continue;
			}
			}
		}
	}
	
	public void run(){
		RunWorker();
	}
	
	public void halt(){
		stop=true;
	}
	
	@SuppressWarnings("unchecked")
	public AWorker<T,R> clone() throws CloneNotSupportedException{
	return (AWorker<T,R>) super.clone();
	}
	
	public abstract R doItem(T task);
	
}
