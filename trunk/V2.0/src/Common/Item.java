package Common;

import java.io.Serializable;

/**
 * the Base Class of each Item in the System i,e task,result,chunk ...
 * @author saeed
 *
 */
@SuppressWarnings("serial")
public class Item implements Serializable{

	
	private final long id;

	public Item(final long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
}
