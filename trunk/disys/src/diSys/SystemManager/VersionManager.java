package diSys.SystemManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import diSys.Common.JarFileReader;


public class VersionManager {
	public static final String UpdateExtension = "ujar";
	public static final String ClassNameExtension = "clsn";
	
	private final String UpdateDir;
	private Version LastVersion;
	
	public VersionManager(String Dir){
		UpdateDir=Dir;
		File f=new File(UpdateDir);
		f.mkdir();
		SetLastVersion();	
	}
	public Version getLastVersion() {
		return LastVersion;
	}
	public String addVersion(byte[] jarFile,String className){
		int newVer=LastVersion.version+1;
		diSys.Common.Logger.TraceInformation("adding new version "+newVer+" new Executer ["+className+"]");
		String newVerFile=getJarFileName(newVer);
		String classNameFile=getClassNameFileName(newVer);
		diSys.Common.Logger.TraceInformation("Saving New Update Jar to :"+newVerFile + " and "+classNameFile);
		try {
			JarFileReader.WriteFile(newVerFile,jarFile);
			File f=new File(classNameFile);
			try {
				f.createNewFile();
				BufferedWriter bw=new BufferedWriter(new FileWriter(f));
				bw.write(className);
				bw.close();
			} catch (IOException e) {
				diSys.Common.Logger.TraceWarning("Failed to Create Class Name File:"+classNameFile,e);
			}
		} catch (FileNotFoundException e1) {
			diSys.Common.Logger.TraceWarning("Failed to Create jar File:"+newVerFile,e1);
		}
		LastVersion=new Version(jarFile,className,newVer);
		return "new Version "+LastVersion+" Saved Successfully";
	}
	public Version GetVersion(int version){
		String lastClassName=GetClassName(version);
		try {
			return new Version(JarFileReader.ReadFileBytes(getJarFileName(version)),lastClassName,version);
		} catch (FileNotFoundException e) {
			diSys.Common.Logger.TraceWarning("Failed to read update File:"+getJarFileName(version),null);
			return null;
		}
	}
	private void SetLastVersion(){
		int lastVerNum=getLastVersionNumber();
		String lastClassName=GetClassName(lastVerNum);
		try {
			LastVersion=new Version(JarFileReader.ReadFileBytes(getJarFileName(lastVerNum)),lastClassName,lastVerNum);
		} catch (FileNotFoundException e) {
			diSys.Common.Logger.TraceWarning("Failed to read update File:"+getJarFileName(lastVerNum),null);
			LastVersion=new Version(null,null,0);
		}
	}
	private String getJarFileName(int ver){
		return UpdateDir+"\\"+ver+"."+UpdateExtension;
	}
	private String getClassNameFileName(int ver){
		return UpdateDir+"\\"+ver+"."+ClassNameExtension;
	}
	
	private String GetClassName(int version) {
		String FileName=UpdateDir+"\\"+version+"."+ClassNameExtension;
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
	}
	
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
