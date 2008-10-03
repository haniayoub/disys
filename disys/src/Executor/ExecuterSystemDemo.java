package Executor;

import Common.IExecutor;
import Common.Item;

public class ExecuterSystemDemo {

	/**
	 * @param args
	 */
	class CalcTask extends Item{
		public CalcTask(long id) {
			super(id);
			// TODO Auto-generated constructor stub
		}
		public int x;
		public int y;
	}
	class CalcResult extends Item{
		public CalcResult(long id) {
			super(id);
			// TODO Auto-generated constructor stub
		}

		public int res;
	}
	public class Calculator implements IExecutor<CalcTask,CalcResult>{

		public Calculator(){
			
		}
		@Override
		public CalcResult run(CalcTask task) {
			CalcResult cr=new CalcResult(task.getId());
			cr.res=task.x+task.y;
			return cr;
		}
	
	}
	public static void main(String[] args) {
	ExecuterSystemDemo sd=new ExecuterSystemDemo();
	ExecuterSystem es=new ExecuterSystem<Calculator>(sd.new Calculator(), 5); 
	es.Run(args);
	}

}
