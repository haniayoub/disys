package Common;

public class Loger {

	public static void TraceInformation(String message){
			System.out.println(message);
		}
	public static void TraceWarning(String message,Exception e){
		TraceException(e);
		System.out.println("Warning:"+message);
	}
	public static void TraceError(String message,Exception e){
		TraceException(e);
		System.out.println("ERROR:"+message);
	}
	public static void TerminateSystem(String message,Exception e){
	TraceException(e);
	System.out.println(message);
	System.out.println("Terminating .");
	System.exit(1);
	}
	public static void TraceException(Exception e){
		if(e!=null){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
}
