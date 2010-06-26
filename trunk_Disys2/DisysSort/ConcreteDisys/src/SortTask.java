import java.util.Arrays;

import diSys.Common.ATask;

public class SortTask extends ATask<SortResult> {

	public SortTask(long id, int[] arr) 
	{
		super(id);
		this.arr = arr;
	}

	private static final long serialVersionUID = 1L;
	
	int[] arr;
	@Override
	public SortResult Run() {
		Arrays.sort(arr);
		return new SortResult(this.getId(), arr);
		
	}

}
