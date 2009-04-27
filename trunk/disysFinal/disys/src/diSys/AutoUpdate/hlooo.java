package diSys.AutoUpdate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class hlooo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] lines=new String[]{"19/04/09 06:04:52-Warning:Exce","dakjdkasjdkas"};
		// Create the pattern
		String patternStr = "\\d\\d/\\d\\d/\\d\\d*";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher("");
    
        // Retrieve all lines that match pattern
        for(String line:lines)
        { 
            matcher.reset(line);
            if (matcher.find()) {
            	System.out.println("1");
            }else{

            	System.out.println("2");
            }
        }
        System.out.println(System.getProperty("os.name"));
	}

}
