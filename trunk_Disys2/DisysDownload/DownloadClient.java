import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import diSys.Client.RemoteClient;
import diSys.Common.SystemUpdates;


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


public class DownloadClient {
	
	private static String rootDir_;
	private static DocIndexRepository downloadedDocs_;
	
	public static void main(String[] args) throws Exception {
		// Parameters: <System Manager Address> <Port> <Chunk Size> <URL List File> <Download Directory> 
		
		//Initialization 
		String sysManagerAddress = "localhost";//args[0];
		int port = 5555;//Integer.parseInt(args[1]);
		int chunkSize = 5; //args[2];
		RemoteClient<DownloadTask, DownloadResult> client =
			new RemoteClient<DownloadTask, DownloadResult>(sysManagerAddress, port, chunkSize);

		BufferedReader input = 
			FileHandler.openFileReader("list.txt"); //args[3];
		DownloadClient.rootDir_ =  "Downloadssss";//args[4];
		DownloadClient.downloadedDocs_ = new DocIndexRepository(DownloadClient.rootDir_, false); 
		
		
		
		
		client.Start();
		
		System.out.println("Num of Tasks: " + client.GetTaskNum());
	
		//add DownloadTasks
		LinkedList<DownloadTask> downloadsList = new LinkedList<DownloadTask>();
		long id = 0;
		while(true){
			String line = input.readLine();
			if (line == null) break;
			String url = line;
			if (DownloadClient.downloadedDocs_.containsKey(url))
				continue;
			downloadsList.add(new DownloadTask(id++,url));
		}
		int i=0;
		for (DownloadTask dt:downloadsList){
			if(i%4==0){i++;dt.setPriority(1); continue;} // skip every fourth task 
			client.AddTask(dt);
			i++;
		}
		
		//get DownloadResults
		for(i=0; i<id; i++)
		{
			try{
				System.out.println("Num of Tasks: " + client.GetTaskNum());
				DownloadResult dr;
				if(i%4==0) { // submit every fourth task and wait for it result
					dr = client.DoTask(downloadsList.get(i));
				}else{
					//Wait for any result
					 dr = client.GetResult(100, TimeUnit.SECONDS);
				}
				System.out.println("Result id:"+dr.getId());
				String downloadedText = dr.text; 
				String downloadedUrl = dr.url;
				int docIndex = DownloadClient.downloadedDocs_.allocateDocIndex();
				DownloadClient.downloadedDocs_.put(downloadedUrl, docIndex);
				String fullFileName = DownloadClient.rootDir_ + '/' + DocNameHandler.getFileNamePrefix(docIndex) +  ".txt.gz";
				new File(fullFileName).getParentFile().mkdirs();
				OutputStream outputFileStream = FileHandler.openFileOutput(fullFileName);
				outputFileStream.write(downloadedText.getBytes("UTF-8"));
				outputFileStream.close();
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		DownloadClient.downloadedDocs_.close();
		
		//stop DownloadClient
		client.Stop();
		System.out.println("Client Done!");
		}
}
