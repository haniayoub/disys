import diSys.Common.Item;


public class SortResult extends Item{

	private static final long serialVersionUID = 1L;

	public int[] arr;
	public SortResult(long id, int[] arr) {
		super(id);
		this.arr = arr;	
	}
}
