package CalcExecuterDemo;

import Common.IExecutor;

public class Calculator2 implements IExecutor<CalcTask, CalcResult>{
	@Override
	public CalcResult run(CalcTask task) throws Exception {
	    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@hla shbab:"+task.getId());
		CalcResult cr=new CalcResult(task.getId());
		cr.res=task.x+task.y;
		System.out.println("calculating:"+task.toString());
		if(task.getId()==8) {
			System.out.println("shit exception "+task.toString());
			throw  new Exception("oooh task number 4 ,,,, i cant sorry");
		}
		return cr;
	}
	

	public String toString(){
		return "Calculator 2222";
	}
}
