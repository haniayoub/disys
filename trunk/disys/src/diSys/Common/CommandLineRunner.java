package diSys.Common;

import java.io.IOException;

public class CommandLineRunner {
	
	public static void Run(String Command){
		String command="cmd /c start "+Command;
		diSys.Common.Logger.TraceInformation("Executing :"+command);
		
		Runtime rt = Runtime.getRuntime();
        try {
			rt.exec(command);
		} catch (IOException e) {
			diSys.Common.Logger.TraceError("Failed to execute :"+command,e);
		}
      
      
	
	}

}
