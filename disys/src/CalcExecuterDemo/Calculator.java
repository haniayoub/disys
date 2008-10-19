package CalcExecuterDemo;

import Common.IExecutor;

public class Calculator implements IExecutor<CalcTask,CalcResult>{
	@Override
	public CalcResult run(CalcTask task) {
		CalcResult cr=new CalcResult(task.getId());
		cr.res=task.x+task.y;
		return cr;
	}

}