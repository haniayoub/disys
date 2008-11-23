package Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Logger {
	final static int MaxLogLines=200;
	
	public static LogTracer logTracer=new LogTracer(MaxLogLines);
	
	public static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	public static void TraceInformation(String message){
        String DateHeader=dateFormat.format(new java.util.Date())+"-";    
		System.out.println(DateHeader+message);
		logTracer.addLine(DateHeader+message);
	}
	public static void TraceWarning(String message,Exception e){
		String DateHeader=dateFormat.format(new java.util.Date())+"-";
		TraceException(e);
		String msg=DateHeader+"Warning:"+message;
		System.out.println(msg);
		if(e!=null)logTracer.addLine(e.getMessage()+"\n");
		logTracer.addLine(msg);
	}
	public static void TraceError(String message,Exception e){
		String DateHeader=dateFormat.format(new java.util.Date())+"-";
		TraceException(e);
		String msg=DateHeader+"ERROR:"+message;
		System.out.println(msg);
		if(e!=null)logTracer.addLine(e.getMessage());
		logTracer.addLine(msg);
	}
	public static void TerminateSystem(String message,Exception e){
	String DateHeader=dateFormat.format(new java.util.Date())+"-";
	TraceException(e);
	System.out.println(DateHeader+message);
	System.out.println("Terminating .");
	if(e!=null)logTracer.addLine(e.getMessage());
	logTracer.addLine(DateHeader+message+"\n"+"Terminating .");
	System.exit(1);
	}
	public static void TraceException(Exception e){
		if(e!=null){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
}
