package diSys.SystemManager;

import diSys.Common.SystemUpdates;

public class Version{
	public final int version;
	public final SystemUpdates updates;
	
	public Version(SystemUpdates updates,int versionNumber){
		version=versionNumber;
		this.updates=updates;
	}
	public String toString(){
		if(updates==null)return "Version:"+version+" Updates:null";
		return "Version:"+version+" Class:"+updates.ExecuterClassName();
	}
}
