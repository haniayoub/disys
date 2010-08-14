package diSys.SystemManager;

import diSys.Common.SystemUpdates;

public class Version{
	public final int version;
	public final SystemUpdates updates;
	
	public Version(SystemUpdates updates,int versionNumber){
		version=versionNumber;
		this.updates=updates;
	}
	@Override
	public String toString(){
		if(updates==null)return "Version:"+version+" Updates:null";
		String s="Version:"+version+" Task Class Types :-";
		for (String str:updates.GetTaskTypes()){
			s+="\n"+"Task Class :- "+str;
		}
		return s;
	}
}
