package diSys.Networking;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RMISecurityManager;

import diSys.Common.FileManager;
import diSys.Common.Logger;

public class Security {
	static final String SecurityFileText="grant {\n permission java.security.AllPermission;\n}; ";
	static final String FileName="security.policy";
	public static void ConfigureSecuritySettings(){
		
		Logger.TraceInformation("Configuring Security ...");
		System.setProperty("java.security.policy",FileName);
		if (!(new File(FileName).exists())){
			try {
				FileManager.WriteFile(FileName, SecurityFileText.getBytes());
			} catch (FileNotFoundException e) {
				Logger.TraceError("Error Creating Security File "+FileName, null);
			}
		}
		if(System.getSecurityManager() == null) {
		     System.setSecurityManager(new RMISecurityManager());
		  }
		Logger.TraceInformation("Configuring Security Complete ");
		
	}
}
