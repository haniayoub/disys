package diSys.Common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FileLogger {
	
	private String path;
	private boolean onlyFile;
	private final String logPrefix= "";
	
	public FileLogger(String path) {
		this.onlyFile = false;
		this.path = path;
	}
	public FileLogger(String path, boolean onlyFile) {
		this.onlyFile = onlyFile;
		this.path = path;
	}
	
	public static DateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yy HH:mm:ss");

	public void TraceClean(String message) {
		String DateHeader = dateHeader();
		String msg = DateHeader + message;
		if(!onlyFile)
			System.out.println(msg);
		Log(msg);
	}
	
	public void TraceInformation(String message) {
		String DateHeader = dateHeader();
		String msg = DateHeader + "Information:" + message;
		if(!onlyFile)
			diSys.Common.Logger.TraceInformation(message);
		Log(msg);
	}
	
	public void TraceWarning(String message) {
		String DateHeader = dateHeader();
		String msg = DateHeader + "Warning:" + message;
		if(!onlyFile)
			diSys.Common.Logger.TraceWarning(message, null);
		Log(msg);
	}
	
	public void TraceWarning(String message, Exception e) {
		String DateHeader = dateHeader();
		TraceException(e);
		String msg = DateHeader + "Warning:" + message;
		if(!onlyFile)
			diSys.Common.Logger.TraceWarning(message, e);
		Log(msg);
	}

	public void TraceError(String message) {
		String DateHeader = dateHeader();
		String msg = DateHeader + "ERROR:" + message;
		if(!onlyFile)
			diSys.Common.Logger.TraceError(message, null);
		Log(msg);
	}
	
	public void TraceError(String message, Exception e) {
		String DateHeader = dateHeader();
		TraceException(e);
		String msg = DateHeader + "ERROR:" + message;
		if(!onlyFile)
			diSys.Common.Logger.TraceError(message, e);
		Log(msg);
	}
	
	public void TerminateSystem(String message, Exception e) {
		String DateHeader = dateHeader();
		TraceException(e);
		String msg = DateHeader + "TERMINATING:" + message;
		if(!onlyFile)
		{
			System.out.println(DateHeader + message);
			System.out.println("Terminating.");
		}
		Log(msg);
		System.exit(1);
	}

	public void TraceException(Exception e) {
		if (e != null) {
			e.printStackTrace();
			if(!onlyFile)
				diSys.Common.Logger.TraceException(e);
			Log(e.getMessage());
		}
	}
	
	public String dateHeader()
	{
		return logPrefix + dateFormat.format(new java.util.Date()) + "-";
	}
	
	public void writeToFile(String msg)
	{
		FileWriter fstream = null;
		try 
		{
			fstream = new FileWriter(path, true);
		} 
		catch (IOException e) 
		{
			System.out.println("ERROR: seems file doesn't exists: " + path);
			e.printStackTrace();
			return;
		}
		BufferedWriter out = new BufferedWriter(fstream);
		try 
		{
			out.write(msg);
		} 
		catch (IOException e) 
		{
			System.out.println("ERROR: seems file isn't writable: " + path);
			e.printStackTrace();
		}
		try 
		{
			out.close();
		} 
		catch (IOException e) 
		{
			System.out.println("ERROR: couldn't close file: " + path);
			e.printStackTrace();
		}
	}
	
	public void Log(String msg)
	{
		writeToFile(msg+System.getProperty("line.separator"));
	}
}