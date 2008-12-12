package CalcExecuterDemo;

import diSys.Common.IExecutor;

public class Calculator implements IExecutor<CalcTask,CalcResult>{
	@Override
	public CalcResult run(CalcTask task) throws Exception {
		System.out.println("sum Task :"+task.getId());
		CalcResult cr=new CalcResult(task.getId());
		cr.res=task.x+task.y;
		System.out.println("calculating:"+task.toString());
		if(task.getId()==3) {
			System.out.println("exception "+task.toString());
			throw  new Exception("task number "+task.getId()+" i cant sorry");
		}
		return cr;
	}
	public String toString(){
		return "Sum Calculator";
	}

}