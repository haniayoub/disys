package diSys.Common;

import java.util.concurrent.BlockingQueue;

import diSys.WorkersSystem.WorkER.AWorker;

public class ItemPrinter<ITEM extends Item> extends AWorker<ITEM,ITEM> {

	public ItemPrinter(BlockingQueue<ITEM> qr,BlockingQueue<ITEM> qt){
		super(qr, qt);
	}
	
	@Override
	public ITEM doItem(ITEM task) {
		System.out.println("Item: "+task.toString());
		return null;
	}
	

}
