import diSys.Common.Item;


public class MergeResult extends Item{

	private static final long serialVersionUID = 1L;

	public int[] arr;
	public MergeResult(long id, int[] arr) {
		super(id);
		this.arr = arr;	
	}
	
	public MergeResult(SortResult sr)
	{
		super(sr.getId());
		this.arr = sr.arr;
	}
}