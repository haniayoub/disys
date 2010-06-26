import diSys.Common.IExecutor;

//from Max
import java.io.BufferedReader;
import java.io.File;
import java.io.OutputStream;

import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;

import downloadLibs.DocIndexRepository;
import downloadLibs.DocNameHandler;
import downloadLibs.FileHandler;
import downloadLibs.HttpDownloader;
import downloadLibs.HttpResult;
//

public class DownloadExecuter implements IExecutor<DownloadTask,DownloadResult> {
	@Override
	public DownloadResult run(DownloadTask arg0) throws Exception {
		DownloadTask task=arg0;
		DownloadResult res=new DownloadResult(task.getId());
		
		System.out.println("trying to download url: " + task.url);
		
		//Do the job...
		res.text = this.downloadAndParseFile(task.url);
		res.url = task.url;
		
		System.out.println("url: " + task.url + " downloaded succesfully!!!" );
		return res;
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
}
