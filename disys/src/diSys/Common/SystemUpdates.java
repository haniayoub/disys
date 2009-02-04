package diSys.Common;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import diSys.AutoUpdate.JarClassLoader;


@SuppressWarnings("serial")
public class SystemUpdates implements Serializable{
	private String executerClassName;
	private byte[] jarBytes;
	public SystemUpdates(String updateJarPath,String executerClassName) throws Exception{
		jarBytes=null;
		if(!VerfiyUpdates(updateJarPath, executerClassName)){
			throw new Exception("Updates Verification Failed");
		}
		diSys.Common.Logger.TraceInformation("Update Verification : OK ...");
		this.executerClassName=executerClassName;
		if(updateJarPath!=null&&this.executerClassName!=null){
		diSys.Common.Logger.TraceInformation("Reading update jar File :"+updateJarPath);
		jarBytes = FileManager.ReadFileBytes(updateJarPath);
		}
	}
	
	public String ExecuterClassName(){
		return executerClassName;
	}
	
	public byte[] UpdateJar(){
		return jarBytes;
	}
	
	private static boolean VerfiyUpdates(String updateJarPath,String executerClassName){
		diSys.Common.Logger.TraceInformation("Verifying Updates Jar:"+updateJarPath+" Executer Class"+executerClassName);
		JarClassLoader jcl;
		try {
		
			jcl = new JarClassLoader(new File(updateJarPath));
		} catch (MalformedURLException e) {
			diSys.Common.Logger.TraceError(e.getMessage(), e);
			return false;
		}
		try {
			Object o=jcl.GetClass(executerClassName).newInstance();
			if(!(o instanceof IExecutor)) {
				diSys.Common.Logger.TraceError(executerClassName+" Do not implement "+IExecutor.class.getName() +" Interface ...",null);
				return false;
			}
		} catch (InstantiationException e) {
			diSys.Common.Logger.TraceError(executerClassName+" instantiation Failed please Check your implementation" , e);
			return false;
		} catch (IllegalAccessException e) {
			diSys.Common.Logger.TraceError(e.getMessage(), e);
			return false;
		} catch (ClassNotFoundException e) {
			diSys.Common.Logger.TraceError(executerClassName+" was not found in the Jar: " +updateJarPath, null);
			return false;
		}
		return true;
	
	}
	
}
