package diSys.Common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;

import diSys.AutoUpdate.JarClassLoader;


@SuppressWarnings("serial")
public class SystemUpdates implements Serializable{
	private String executerClassName;
	private byte[] jarBytes;
	private String[] executerClassNames;
	public HashMap<String, byte[]> IncludeJars;
	public SystemUpdates(String updateJarPath,String executerClassName) throws Exception{
		jarBytes=null;
		if(executerClassName!=null){
			this.executerClassNames=new String[]{executerClassName};
			this.executerClassName=executerClassName;
			}else{
				JarClassLoader jcl=null;
				try {
					File jar=new File(updateJarPath);
					jcl = new JarClassLoader(jar);
				} catch (MalformedURLException e) {
					diSys.Common.Logger.TraceError(e.getMessage(), e);
				}
				this.executerClassNames=jcl.getSubClassesof(IExecutor.class, false);
				if(this.executerClassNames.length==0){
					throw new Exception("No sub class of " +IExecutor.class.getName()+" found!");
				}
				this.executerClassName=this.executerClassNames[0];
			}
		if(!VerfiyUpdates(updateJarPath, this.executerClassName)){
			throw new Exception("Updates Verification Failed");
		}
		diSys.Common.Logger.TraceInformation("Update Verification : OK ...");
		
		if(updateJarPath!=null){
		diSys.Common.Logger.TraceInformation("Reading update jar File :"+updateJarPath);
		jarBytes = FileManager.ReadFileBytes(updateJarPath);
		}
	}
	public SystemUpdates(String updateJarPath) throws Exception{
		this(updateJarPath,null);
	}
	public String ExecuterClassName(){
		return executerClassName;
	}
	
	public byte[] UpdateJar(){
		return jarBytes;
	}
	
	
	public void setIncludeJars(File[] files){
		IncludeJars=new HashMap<String, byte[]>();
		for(File f:files){
			try {
				IncludeJars.put(f.getName(), FileManager.ReadFileBytes(f.getAbsolutePath()));
			} catch (FileNotFoundException e) {
			}
		}
	}
	private static boolean VerfiyUpdates(String updateJarPath,String executerClassName){
		diSys.Common.Logger.TraceInformation("Verifying Updates Jar:"+updateJarPath+" Executer Class"+executerClassName);
		File jar=new File(updateJarPath);
		if(!jar.exists()){
			diSys.Common.Logger.TraceError(updateJarPath+" Do not exist ",null);
			return false;
		}
		JarClassLoader jcl;
		
		try {
		
			jcl = new JarClassLoader(jar);
		} catch (MalformedURLException e) {
			diSys.Common.Logger.TraceError(e.getMessage(), e);
			return false;
		}
		//jcl.getSubClassesof(c, includeInterfaces)
		/*try {
			jcl.GetClass(executerClassName).is
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
		}*/
		return true;
	
	}
	public String[] getExecuterClassNames() {
		return executerClassNames;
	}
	public String getExecuterClassName() {
		return executerClassName;
	}
	public void setExecuterClassName(String executerClassName) {
		this.executerClassName = executerClassName;
	}
	
}
