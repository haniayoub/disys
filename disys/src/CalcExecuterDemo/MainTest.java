package CalcExecuterDemo;

import java.io.File;
import java.io.IOException;

import AutoUpdate.JarClassLoader;
import Common.IExecutor;


public class MainTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		String filePath = "D:\\study\\projects\\Distrebuted\\WorkSpace\\disys\\Release\\NewJar.jar";
		//String jarPath="jar:file://"+filePath+"!/";
		//URL url=new File(filePath).toURI().toURL();
		//System.out.println(url.toString());
		JarClassLoader jcl=new JarClassLoader(new File(filePath));
		for(String s:jcl.getSubClassesof(IExecutor.class,false)) {
			System.out.println(s);
			IExecutor ex=null;
			try {
				ex=(IExecutor) jcl.loadClass(s).newInstance();
			
				System.out.println(ex.toString());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}

}
