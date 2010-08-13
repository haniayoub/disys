package SystemAnalysis;

import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import diSys.Common.ATask;
import downloadLibs.HttpDownloader;
import downloadLibs.HttpResult;
@SuppressWarnings("serial")
public class DownloadTask extends ATask<DownloadResult> {
	
	public String url;
	
	public DownloadTask() {
		super(0);
		this.url=getRandomURL();
	}
	
	public DownloadTask(long id, String url) {
		super(id);
		this.url=url;
	}
	
	public String toString(){
		return "Task ID: " + this.getId();
	}
	private String downloadAndParseFile(String url) throws Exception {
		HttpResult result = HttpDownloader.download(url);
		if (result.getErrorResponse() != null) {
			throw new Exception("Error downloading " + url + ": " + result.getErrorResponse());
		}
		Parser parser = Parser.createParser(new String(result.getData(), "UTF-8"), "UTF-8");
		StringBean sb = new StringBean();
		parser.visitAllNodesWith(sb);
		String text = sb.getStrings();
		return text;
	}

	@Override
	public DownloadResult Run() throws Exception {
		DownloadTask task=this;
		DownloadResult res=new DownloadResult(task.getId());
		
		System.out.println("trying to download url: " + task.url);
		
		//Do the job...
		res.text = this.downloadAndParseFile(task.url);
		res.url = task.url;
		
		System.out.println("url: " + task.url + " downloaded succesfully!!!" );
		return res;
	}
	
	private static String getRandomURL()
	{
		//TODO: implement
		return "http://www.mozilla.com/en-US/firefox/2.0.0.4/releasenotes/";
	}
}
