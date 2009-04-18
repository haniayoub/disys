package diSys.Common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class FileManager {
	
	public static byte[] ReadFileBytes(String filePath) throws FileNotFoundException{
		byte[] $=null;
		LinkedList<Byte> byteList=new LinkedList<Byte>();
		FileInputStream fis=new FileInputStream(filePath);
		try {
			while(fis.available()>0) byteList.addLast((byte)(fis.read()));
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Object[] arr=byteList.toArray();
		$=new byte[byteList.size()];
		for(int i=0;i<byteList.size();i++) $[i]=((Byte)arr[i]).byteValue();
		return $;
	}
	public static void WriteFile(String filePath,byte[] bytes) throws FileNotFoundException 
	{
		FileOutputStream fos=new FileOutputStream(filePath);
		try {
			fos.write(bytes);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public static String ReadLine(String versionFile) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(versionFile));
		String line=in.readLine();
		in.close();
		return line;
	}
}
