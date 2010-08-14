package SystemAnalysis;

import diSys.Common.ATask;
import diSys.Executor.DummyBenchmarkClass;

@SuppressWarnings("serial")
public class MatrixMulTask extends ATask<MatrixMulResult>{
	int iterations = 1;
	public MatrixMulTask(long id,int iterations) {
		super(id);
		this.iterations = iterations;
	}

	/*public MatrixMulTask() {
		super(0);
	}*/

	public static final int  LENGTH = 100;
	private int arr1[][] = null; 
	private int arr2[][] = null;
	
	@Override
	public MatrixMulResult Run() throws Exception {
		arr1 = new int[LENGTH][LENGTH];
		arr2 = new int[LENGTH][LENGTH];
		for(int i=0; i<LENGTH; i++)
		{
			arr1[i] = getRandomArray(LENGTH);
			arr2[i] = getRandomArray(LENGTH);
		}
		
		int[][] res = new int[LENGTH][LENGTH];
		//for (int bb = 0 ; bb < iterations ; bb++)
		for(int i = 0; i < LENGTH; i++)
			for(int j = 0; j < LENGTH-1; j++)
		        for(int k = 0; k < LENGTH; k++)
		        {
		        	res[i][j] += arr1[i][k]*arr2[k][j];
		        }
		double avg = 0;
		DummyBenchmarkClass dumm = null;
		for(int c = 0; c < iterations; c++)
			for(int i = 0; i < LENGTH-1; i++)
			for(int j = 0; j < LENGTH-1; j++)
			{
				dumm = new DummyBenchmarkClass();	
				avg = avg + (1.0*res[i][j]/res[i+1][j+1])/(LENGTH*LENGTH*LENGTH) ;
			}
		System.out.println("**********finished multiplying Metrices avg is :"+avg);
		return new MatrixMulResult(this.getId(), avg);
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
