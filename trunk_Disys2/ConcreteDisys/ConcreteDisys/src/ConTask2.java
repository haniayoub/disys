import diSys.Common.ATask;


public class ConTask2 extends ATask<ConResult2> {
	String toPrint;
	public ConTask2(long id,String toPrint) {
		super(id);
		// TODO Auto-generated constructor stub
		this.toPrint=toPrint;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ConResult2 Run() {
		System.out.println(toPrint);
		return new ConResult2(0);
	}

}
