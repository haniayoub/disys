package diSys.FailureDetector;

import diSys.Common.FileLogger;

public class FailureDetectorLog {
	
	static private FileLogger logger = null;
	
	protected FailureDetectorLog()
	{
		// does nothing
	}
	static public FileLogger getLog()
	{
		if(logger == null)
			logger = new FileLogger("log.txt");  
		return logger;
	}
}
