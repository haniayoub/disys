package CalcExecuterDemo;

import java.io.IOException;
import java.net.URL;

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
		JarClassLoader jcl=new JarClassLoader(new URL("jar:"+filePath+"!/"),filePath);
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
