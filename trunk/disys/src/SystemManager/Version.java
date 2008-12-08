package SystemManager;

public class Version{
	public final int version;
	public final String className;
	public final byte[] jar;
	
	public Version(byte[] jar,String className,int versionNumber){
		version=versionNumber;
		this.className=className;
		this.jar=jar;
	}
	public String toString(){
		return "Version:"+version+" Class:"+className;
	}
}
