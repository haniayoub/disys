package SystemAnalysis;

import diSys.Common.ATask;

@SuppressWarnings("serial")
public class MatrixMulTask extends ATask<MatrixMulResult>{
	public MatrixMulTask(long id) {
		super(id);
		for(int i=0; i<LENGTH; i++)
		{
			arr1[i] = getRandomArray(LENGTH);
			arr2[i] = getRandomArray(LENGTH);
		}
	}

	public MatrixMulTask() {
		super(0);
	}

	public static final int  LENGTH = 100;
	private int arr1[][] = new int[LENGTH][LENGTH];
	private int arr2[][] = new int[LENGTH][LENGTH];
	
	@Override
	public MatrixMulResult Run() throws Exception {
		int[][] res = new int[LENGTH][LENGTH];
		for(int i = 0; i < LENGTH; i++)
			for(int j = 0; j < LENGTH-1; j++)
		        for(int k = 0; k < LENGTH; k++)
		        {
		        	System.out.println(i + " " + j + " " + k);
		        	res[i][j] += arr1[i][k]*arr2[k][j];
		        }
		System.out.println("**********finished multiplying Metrices!*********");
		return new MatrixMulResult(this.getId(), res);
	}
	
	public static int[] getRandomArray(int length)
	{
		int[] arr = new int[length];
		java.util.Random rand =  new java.util.Random(System.currentTimeMillis());
	    for (int i=0; i<length; i++) 
	    { 
	    	arr[i] = rand.nextInt() % length; 
	    }
	    return arr;
	}
}
