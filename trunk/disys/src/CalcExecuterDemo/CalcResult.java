package CalcExecuterDemo;

import Common.Item;

@SuppressWarnings("serial")
class CalcResult extends Item{
	public CalcResult(final long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public String toString(){
	return "Calc Result:"+res;
	}
	public int res;
}