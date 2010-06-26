import diSys.Common.Item;
@SuppressWarnings("serial")
public class DownloadTask extends Item {
	
	public String url;
	
	public DownloadTask() {
		super(0);
		this.url="";
	}
	
	public DownloadTask(long id, String url) {
		super(id);
		this.url=url;
	}
	
	public String toString(){
		return "Task ID: " + this.getId();
	}
}
