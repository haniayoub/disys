import diSys.Common.ATask;

public class MergeTask extends ATask<MergeResult> {

	public MergeTask(long id, int[] arrA, int[] arrB) 
	{
		super(id);
		this.arrA = arrA;
		this.arrB = arrB;
	}
	
	public MergeTask(SortResult sr1, SortResult sr2)
	{
		super(0);
		this.arrA = sr1.arr;
		this.arrB = sr2.arr;
	}
	
	public MergeTask(MergeResult mr1, MergeResult mr2)
	{
		super(0);
		this.arrA = mr1.arr;
		this.arrB = mr2.arr;
	}

	private static final long serialVersionUID = 1L;
	
	private int[] arrA;
	private int[] arrB;
	
	@Override
	public MergeResult Run() {
		int[] arrC = new int[arrA.length + arrB.length];
		merge(arrA, arrB, arrC);
		return new MergeResult(this.getId(), arrC);
	}
	
	public void merge(int[] A, int[] B, int[] C) {
	     int i, j, k, m, n;
	     i = 0;
	     j = 0;
	     k = 0;
	     m = A.length;
	     n = B.length;
	     while (i < m && j < n) {
	           if (A[i] <= B[j]) {
	                 C[k] = A[i];
	                 i++;
	           } else {
	                 C[k] = B[j];
	                 j++;
	           }
	           k++;
	     }
	     if (i < m) {
	           for (int p = i; p < m; p++) {
	                 C[k] = A[p];
	                 k++;
	           }
	     } else {
	           for (int p = j; p < n; p++) {
	                 C[k] = B[p];
	                 k++;
	           }
	     }
	}
}
