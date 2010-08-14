package SystemAnalysis;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

import diSys.Common.ATask;
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
	
	@Override
	public String toString(){
		return "Task ID: " + this.getId();
	}
	private String downloadAndParseFile(String url) throws Exception {
		/*HttpResult result = HttpDownloader.download(url);
		if (result.getErrorResponse() != null) {
			throw new Exception("Error downloading " + url + ": " + result.getErrorResponse());
		}
		Parser parser = Parser.createParser(new String(result.getData(), "UTF-8"), "UTF-8");
		StringBean sb = new StringBean();
		parser.visitAllNodesWith(sb);*/
		String text = "";
		InputStream is = null;
		DataInputStream dis;
		String line;
		URL  link = new URL(url);
		    is = link.openStream();  // throws an IOException
		    dis = new DataInputStream(new BufferedInputStream(is));

		    while ((line = dis.readLine()) != null) {
		        System.out.println(line);
		        text=text+"\n"+line;
		    }
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
