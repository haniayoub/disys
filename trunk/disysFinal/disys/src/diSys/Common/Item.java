package diSys.Common;

import java.io.Serializable;

/**
 * the Base Class of each Item in the System i,e task,result,chunk ...
 * @author saeed
 *
 */
public class Item implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8535769791644339545L;
	/* priority must be between 1 and 20
	 * when 1 is the highest priority 
	 * and 20 is the lowest priority
	*/
	private Integer priority = 10;
	private Exception e; 
	private String log; 
	private final long id;
	private String owner;
	
	public Item(final long id) {
		super();
		this.id = id;
		this.e=null;
	}
	public Item(final long id, int priority) {
		this(id);
		int prio = priority;
		if(priority < 1)
			prio = 1;
		if(priority > 20)
			prio = 20;
		this.priority = prio;
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
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log=log;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
