package diSys.Common;

import java.io.FileNotFoundException;

public class SystemUpdates {
	String executerClassName;
	byte[] jarBytes;
	public SystemUpdates(String updateJarPath,String executerClassName) throws FileNotFoundException{
		jarBytes=null;
		this.executerClassName=executerClassName;
		if(updateJarPath!=null&&this.executerClassName!=null){
		diSys.Common.Logger.TraceInformation("Reading update jar File :"+updateJarPath);
		jarBytes = FileManager.ReadFileBytes(updateJarPath);
		}
	}
	
}
