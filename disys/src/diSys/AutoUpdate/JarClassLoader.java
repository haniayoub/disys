package diSys.AutoUpdate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
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
    public static URLClassLoader sysloader=(URLClassLoader)java.lang.ClassLoader.getSystemClassLoader();
	
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
    @SuppressWarnings("unchecked")
    private static final Class[] parameters = new Class[] {URL.class};
    @SuppressWarnings({ "unchecked", "static-access" })
    public void AddUrlToSystem(URL url){
    	
    	
		
			sysloader = (URLClassLoader)java.lang.ClassLoader.getSystemClassLoader();
			sysloader.newInstance(new URL[]{url},this);
			Class sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            diSys.Common.Logger.TraceInformation("adding Url to System Class Loader :"+url);
            method.invoke(sysloader, new Object[] {url});
        } catch (Throwable t) {
            t.printStackTrace();
            diSys.Common.Logger.TraceError("Error while adding URL :"+url,null);
        }
        /*
        for(String clazz:getClasses()){
            try {
              diSys.Common.Logger.TraceInformation("Loading Class:"+clazz);
              //this.loadClass(clazz);
              //Class.
              //sysloader.newInstance(new URL[]{url},this);
              Class.forName(clazz,false,sysloader);
            }
            catch (ClassNotFoundException e) {
    			diSys.Common.Logger.TraceWarning("Class not Found:"+clazz, e);
    		}
            catch(Exception e){
            	diSys.Common.Logger.TraceWarning("Error loading class:"+clazz +" , "+e.getMessage(),null);
            }
            
        }
        diSys.Common.Logger.TraceInformation("Done Loading!!");
  */
      }
}
