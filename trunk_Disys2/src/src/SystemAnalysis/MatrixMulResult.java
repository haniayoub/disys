package SystemAnalysis;

import diSys.Common.Item;

@SuppressWarnings("serial")
public class MatrixMulResult extends Item{
	
	public int result[][] = new int[MatrixMulTask.LENGTH][MatrixMulTask.LENGTH];
	
	public MatrixMulResult(long id, int[][] result) {
		super(id);
		this.result = result;
	}
}