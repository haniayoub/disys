package CalcExecuterDemo;

import Common.Item;

@SuppressWarnings("serial")
public class CalcTask extends Item{
	public CalcTask(final long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public int x;
	public int y;
	
	public String toString(){
	return "CalcTask: id:"+this.getId()+" x="+x+" y="+y;
	}
}