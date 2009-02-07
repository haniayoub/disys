package diSys.Common;

public class Exceptions {

	@SuppressWarnings("serial")
	public static class ClientException extends Exception{
		public ClientException(){
			super();
		}
		public ClientException(String s){
			super(s);
		}

	}
	
}
