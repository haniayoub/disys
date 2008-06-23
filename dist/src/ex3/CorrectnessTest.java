package ex3;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import ex3.executor.Operation;


public class CorrectnessTest {
	private final static String USAGE = "Usage: java CorrectnessTest <input_file> <your_results>"; 
	
	private HashMap<Integer,String> readResults(String resultsFile) throws IOException{
		HashMap<Integer,String> map = new HashMap<Integer,String>();
		BufferedReader fr = new BufferedReader(new FileReader(resultsFile));
		String line;
		while( (line = fr.readLine()) != null){
			if(line.length() == 0){
				continue;
			}
			StringTokenizer t = new StringTokenizer(line,Constants.DELIMITER);
			Integer key = Integer.valueOf(t.nextToken());
			String value = t.nextToken();
			map.put(key,value);
		}		
		fr.close();
		return map;
	}
	
		
	private HashMap<Integer,String> calculateResults(String inputFile) throws IOException{
		HashMap<Integer,String> map = new HashMap<Integer,String>();
		BufferedReader fr = new BufferedReader(new FileReader(inputFile));
		String line;
		while( (line = fr.readLine()) != null){
			if(line.length() == 0){
				continue;
			}
			StringTokenizer t = new StringTokenizer(line,Constants.DELIMITER);
			Integer key = Integer.valueOf(t.nextToken());
			Character op = t.nextToken().charAt(0);
			int a = Integer.valueOf(t.nextToken());
			int b = Integer.valueOf(t.nextToken());
			String value = Operation.doOp(op,a,b);
			map.put(key,value);
		}		
		fr.close();
		return map;
	}
	
	public static void main(String[] args) throws IOException{		
		if(args.length != 2){
			System.err.println(USAGE);
			return;
		}
		CorrectnessTest test = new CorrectnessTest();
		try{
			HashMap<Integer,String> res1 = test.calculateResults(args[0]);
			HashMap<Integer,String> res2 = test.readResults(args[1]);
			if(res1.size() != res2.size()){
				System.err.println("Incorrect number of results");
				return;
			}
			for(Integer key1: res1.keySet()){
				String val1 = res1.get(key1);
				String val2 = res2.get(key1);
				if(!val1.equals(val2)){
					System.err.println("Error in result with ID:" + key1);
					System.err.println("Expected >" + val1 + "<\n" +
									   "Received >" + val2);
					return;
				}
			}
		}catch(Throwable e){
			e.printStackTrace();
			System.err.println(USAGE);
			return;
		}
			
		System.out.println("SUCCESS");
	}
}
