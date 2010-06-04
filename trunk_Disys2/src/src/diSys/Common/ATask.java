package diSys.Common;

public abstract class ATask<RESULT extends Item> extends Item {

	public ATask(long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public abstract RESULT Run();
}
