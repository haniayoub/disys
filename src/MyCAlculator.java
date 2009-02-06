import diSys.Common.IExecutor;


public class MyCAlculator implements IExecutor<CalcTask,CalcResult> {

	@Override
	public CalcResult run(CalcTask arg0) throws Exception {
		CalcResult r=new CalcResult(arg0.getId());
		r.res=arg0.x+arg0.y;
		System.out.println(arg0.x+" + "+arg0.y+"="+r.res);
		return r;
	}
	public String toString(){
	return "My Calculator";
	}

}
