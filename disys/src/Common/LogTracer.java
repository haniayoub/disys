package Common;

import java.util.LinkedList;

public class LogTracer {
	 private LinkedList<String> Lines;
	 private int MaxSize;
	 public LogTracer(int maxLines){
		 Lines=new LinkedList<String>();
		 MaxSize=maxLines;
	 }
	 public void addLine(String line){
	 Lines.addFirst(line);
	 if (Lines.size()>MaxSize)Lines.removeLast();
	 }
	 public String toString(){
		 String $="";
		 for(String line:Lines) $+=line+"\n";
		 return $;
	 }
}
