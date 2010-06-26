package diSys.Common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;

import diSys.AutoUpdate.JarClassLoader;

@SuppressWarnings("serial")
public class SystemUpdates implements Serializable {
	private byte[] jarBytes;
	public HashMap<String, byte[]> IncludeJars;
	public String[] taskTypes ;
	public SystemUpdates(String updateJarPath)
			throws Exception {
	jarBytes = null;
	JarClassLoader jcl=null;
	try {
		File jar = new File(updateJarPath);
		if (!jar.canRead()){
			throw new Exception("Can't read file :"+updateJarPath);
		}
		jcl = new JarClassLoader(jar);
	} catch (Exception e) {
		diSys.Common.Logger.TraceError(e.getMessage(), e);
		throw e;
	}
	
	taskTypes = jcl.getSubClassesof(ATask.class,
			false);
	diSys.Common.Logger.TraceInformation("Reading update jar File :"
				+ updateJarPath);
	jarBytes = FileManager.ReadFileBytes(updateJarPath);
	}
	

	public String[] GetTaskTypes() {
		return taskTypes;
	}

	public byte[] UpdateJar() {
		return jarBytes;
	}

	public void setIncludeJars(File[] files) {
		IncludeJars = new HashMap<String, byte[]>();
		for (File f : files) {
			try {
				IncludeJars.put(f.getName(), FileManager.ReadFileBytes(f
						.getAbsolutePath()));
				try {
					JarClassLoader.AddUrlToSystem(f.toURI().toURL());
				} catch (MalformedURLException e) {
				}
			} catch (FileNotFoundException e) {
			}
		}
	}

	public void VerfiyUpdates(String updateJarPath, String executerClassName)
			throws Exception {
		diSys.Common.Logger.TraceInformation("Verifying Updates Jar:"
				+ updateJarPath + " Executer Class" + executerClassName);
		File jar = new File(updateJarPath);
		if (!jar.exists()) {
			diSys.Common.Logger.TraceError(updateJarPath + " Do not exist ",
					null);
			throw new Exception(updateJarPath + " Do not exist ");
		}
	}
	public String toString(){
		String s = "system updates : ";
		for (String str:GetTaskTypes()){
			s+="\nTask Class :- "+str;
		}
		return s;
	}
}
