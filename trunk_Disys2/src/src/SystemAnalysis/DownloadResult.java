package SystemAnalysis;

import diSys.Common.Item;

@SuppressWarnings("serial")
public class DownloadResult extends Item {
	
	public String text;
	public String url;
	
	public DownloadResult(long id) {
		super(id);
	}
}
