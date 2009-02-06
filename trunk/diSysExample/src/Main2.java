import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while(true) {
		//  open up standard input 
		      BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
		     String s=null;
			try {
				s = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    // System.out.println(s);
			
			try {
			//	Runtime.getRuntime().
				Process p = Runtime.getRuntime().exec(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(s.startsWith("exit")) break;
		}
	}

}
