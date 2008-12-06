package AutoUpdate;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassLoader extends URLClassLoader {
//    private URL url;
    private JarFile jar;
	
    /**
	 * Creates a new JarClassLoader for the specified url.
	 * 
	 * @param url
	 *            the url of the jar file
     * @throws MalformedURLException 
	 */
    public JarClassLoader(File f) throws MalformedURLException {
    	 super(new URL[] {f.toURI().toURL() });
    	try {
			jar=new JarFile(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
    }

       public void Packages(){
   
    	for(Package p:this.getPackages()){
    	System.out.println(p);
    	}
    }
    
    public String[] getClasses(){
    	
    	LinkedList<String> classes=new LinkedList<String>(); 
    	Enumeration<JarEntry> list= jar.entries();
    	while(list.hasMoreElements()){
    	JarEntry je=list.nextElement();
    	String entry=je.getName();
    	if(entry.endsWith(".class")){
    		String className=entry.replace(".class","");
    		className=className.replace("/", ".");
    		classes.add(className);
    	}
    	}
    	return classes.toArray(new String[]{});
    }
    @SuppressWarnings("unchecked")
	public String[] getSubClassesof(Class c,boolean includeInterfaces){
    	LinkedList<String> classes=new LinkedList<String>(); 
    	for(String className:getClasses()) {
	    	try {
				Class clazz=this.loadClass(className);
				clazz.asSubclass(c);
				if(!includeInterfaces&&clazz.isInterface()) continue;
				classes.add(className);
	    	}
	    	catch (ClassCastException e) 
	    	{
	    	}
	    	catch (ClassNotFoundException e) 
	    	{
				
	    	}
    	}
    	return classes.toArray(new String[]{});
    }
    /*
    public void atts() throws IOException{
   	  //URL u = new URL("jar", "", url + "!/");
     ClassLoader fc=new ClassLoader
     java.lang.
    	System.out.println(url);
    	JarURLConnection uc = (JarURLConnection)url.openConnection();
     Attributes attr = uc.getMainAttributes();
     for(Object o:attr.keySet()){
     System.out.println(attr.get(o));
     }
    }*/
}
