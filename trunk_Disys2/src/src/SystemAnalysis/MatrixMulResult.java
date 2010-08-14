package SystemAnalysis;

import diSys.Common.Item;

@SuppressWarnings("serial")
public class MatrixMulResult extends Item{
	
	//public int result[][] = new int[MatrixMulTask.LENGTH][MatrixMulTask.LENGTH];
	public double avg = 0;
	public MatrixMulResult(long id, double avg) {
		super(id);
		this.avg = avg;
		//this.result = result;
	}
}