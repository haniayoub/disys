package Common;
public class Loger {
	final static int MaxLogLines=100;
	
	public static LogTracer logTracer=new LogTracer(MaxLogLines);
	
	public static void TraceInformation(String message){
			System.out.println(message);
			logTracer.addLine(message);
	}
	public static void TraceWarning(String message,Exception e){
		TraceException(e);
		String msg="Warning:"+message;
		System.out.println(msg);
		if(e!=null)logTracer.addLine(e.getMessage());
		logTracer.addLine(msg);
	}
	public static void TraceError(String message,Exception e){
		TraceException(e);
		
		String msg="ERROR:"+message;
		System.out.println(msg);
		if(e!=null)logTracer.addLine(e.getMessage());
		logTracer.addLine(msg);
	}
	public static void TerminateSystem(String message,Exception e){
	TraceException(e);
	System.out.println(message);
	System.out.println("Terminating .");
	if(e!=null)logTracer.addLine(e.getMessage());
	logTracer.addLine(message+"\n"+"Terminating .");
	System.exit(1);
	}
	public static void TraceException(Exception e){
		if(e!=null){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
}
