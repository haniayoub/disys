import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class dummy {

	/**
	 * @param args
	 */
	
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
	

	public static void main(String[] args) {
		int n=1000;
		int[][] arr1=new int[n][n]; 
		int[][] arr2=new int[n][n];
		int[][] arr3=new int[n][n];
		DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
		System.out.println("Running benchmark :" + formatter.format(new Date()));
		for (int i=0;i<n;i++)
		{
			arr1[i]=getRandomArray(n);
		 	arr2[i]=getRandomArray(n);
		}
		
		for(int i = 0; i < n; i++) {
		      for(int j = 0; j < n-1; j++) {
		        for(int k = 0; k < n; k++)
		          arr3[i][j] += arr1[i][k]*arr2[k][j];
		        }
		}
		/*
		for(int i = 0; i < n; i++) {
		      for(int j = 0; j < n; j++) {
		    	  System.out.print(arr3[i][j]);
		      }
		      System.out.println();
		}*/
		System.out.println("Done :" + formatter.format(new Date()));
		
	 }
}
