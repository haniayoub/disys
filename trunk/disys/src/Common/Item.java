package Common;

import java.io.Serializable;

/**
 * the Base Class of each Item in the System i,e task,result,chunk ...
 * @author saeed
 *
 */
@SuppressWarnings("serial")
public class Item implements Serializable{

	private Exception e; 
	private String log; 
	private final long id;

	public Item(final long id) {
		super();
		this.id = id;
		this.e=null;
	}
	public long getId() {
		return id;
	}
	public Exception getException() {
		return e;
	}
	public void setException(Exception e) {
		this.e=e;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log=log;
	}
	
}
