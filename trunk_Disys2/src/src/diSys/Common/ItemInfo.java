package diSys.Common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ItemInfo implements Serializable{
	public ItemInfo(String itemString, int itemHashCode, long itemID, int priority)
	{
		this.itemString = itemString;
		this.itemHashCode = itemHashCode;
		this.itemID = itemID;
		this.priority = priority;
	}

	public final String itemString;
	public final int itemHashCode;
	public final long itemID;
	public int priority;
	public String toString()
	{
		return itemString; 
	}
	public String getToolTipText() {
		return "Priority: " + priority + " , " + "ID: " + itemID + System.getProperty("line.separator");
	}
}
