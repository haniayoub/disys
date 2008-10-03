package CalcExecuterDemo;

import Common.Item;

class CalcResult extends Item{
	public CalcResult(long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public String toString(){
	return "Calc Result:"+res;
	}
	public int res;
}