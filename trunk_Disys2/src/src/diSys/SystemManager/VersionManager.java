package diSys.SystemManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import diSys.Common.FileManager;
import diSys.Common.SystemUpdates;


public class VersionManager {
	public static final String UpdateExtension = "ujar";
	public static final String ClassNameExtension = "clsn";
	
	private final String UpdateDir;
	private Version LastVersion;
	private int NumOfVersions;
	public VersionManager(String Dir,int NumOfVersions){
		UpdateDir=Dir;
		this.NumOfVersions=NumOfVersions;
		File f=new File(UpdateDir);
		f.mkdir();
		SetLastVersion();	
	}
	public Version getLastVersion() {
		return LastVersion;
	}
	public String addVersion(SystemUpdates updates){
		if(LastVersion==null) LastVersion=new Version(null,0);
		int newVer=LastVersion.version+1;
		diSys.Common.Logger.TraceInformation("adding new version "+newVer+" Task Types :-");
		for (String str:updates.GetTaskTypes()){
			diSys.Common.Logger.TraceInformation("Task Class :- "+str);
		}
		String newVerFile=getJarFileName(newVer);
		diSys.Common.Logger.TraceInformation("Saving New Update Jar to :"+newVerFile);
		try {
			FileManager.WriteFile(newVerFile,updates.UpdateJar());
		} catch (FileNotFoundException e1) {
			diSys.Common.Logger.TraceWarning("Failed to Create jar File:"+newVerFile,e1);
		}
		String IncludeJarsDir=getIncludeJarsDir(newVer);
		File f=new File(IncludeJarsDir);
	    f.mkdir();
		for(String fname:updates.IncludeJars.keySet()){
			try {
				diSys.Common.Logger.TraceInformation("Saving Include jar File:"+fname);
				FileManager.WriteFile(IncludeJarsDir+fname,updates.IncludeJars.get(fname));
			} catch (FileNotFoundException e) {
				diSys.Common.Logger.TraceError("Include jar File:"+fname+"Not found to save",null);
			}
		}
		File f1=new File(getJarFileName(newVer-NumOfVersions));
		File f2=new File(getClassNameFileName(newVer-NumOfVersions));
		f1.delete();
		f2.delete();
		LastVersion=new Version(updates,newVer);
		return "new Version "+LastVersion+" Saved Successfully";
	}
	private String getIncludeJarsDir(int ver) {
		return UpdateDir+"/IncludeJars"+ver+"/";
	}
	public Version GetVersion(int version){
		try {
			return new Version(new SystemUpdates(getJarFileName(version)),version);
		} catch (Exception e) {
			diSys.Common.Logger.TraceWarning("Failed to read update File:"+getJarFileName(version),e);
			return null;
		}
	}
	private void SetLastVersion(){
		
		int lastVerNum=getLastVersionNumber();
		//String lastClassName=GetClassName(lastVerNum);
		if(lastVerNum==0)
			{
			LastVersion=new Version(null,0);
			diSys.Common.Logger.TraceWarning("System has no updates for executers ... You need to Update the system!",null);
			return;
			}
		try {
			LastVersion=new Version(new SystemUpdates(getJarFileName(lastVerNum)),lastVerNum);
		    File f=new File(UpdateDir+"/IncludeJars"+lastVerNum+"/");
		    diSys.Common.Logger.TraceInformation("Loading include Jars from:"+f.getName());
		    LastVersion.updates.setIncludeJars(f.listFiles());
		} catch (Exception e) {
		}
	}
	private String getJarFileName(int ver){
		return UpdateDir+"/"+ver+"."+UpdateExtension;
	}
	private String getClassNameFileName(int ver){
		return UpdateDir+"/"+ver+"."+ClassNameExtension;
	}
	
/*	private String GetClassName(int version) {
		String FileName=UpdateDir+"/"+version+"."+ClassNameExtension;
		if(!(new File(FileName).exists())) return null;
		FileReader fr;
		try {
			fr=new FileReader(FileName);
		} catch (FileNotFoundException e) {
			diSys.Common.Logger.TraceWarning(FileName+" File Not Found .", e);
			return null;
		}
	 diSys.Common.Logger.TraceInformation("Loading Class Name from : "+FileName);	
	 BufferedReader br=new BufferedReader(fr);
	 String Line=null;
	 try {
		Line=br.readLine();
	 br.close();
	 fr.close();
	 }
	 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 return Line;
	}*/
	
	private int getLastVersionNumber(){
		File fl=new File(UpdateDir);
		File[] files=fl.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(UpdateExtension);
			}
		});
		int maxVer=0;
		int curr=0;
		for(File f:files){
		curr=Integer.parseInt(f.getName().replace("."+UpdateExtension,""));
		if(maxVer<curr)maxVer=curr;
		}
		return maxVer;
	}
	
	

}
