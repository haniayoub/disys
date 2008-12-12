package CalcExecuterDemo;

import diSys.Common.Item;

@SuppressWarnings("serial")
public class CalcTask extends Item{
	public CalcTask(final long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public CalcTask(final long id, int prio) {
		super(id, prio);
	}
	public int x;
	public int y;
	
	public String toString(){
	return "CalcTask: id:"+this.getId()+" x="+x+" y="+y;
	}
}