package Common;

import java.io.Serializable;

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
