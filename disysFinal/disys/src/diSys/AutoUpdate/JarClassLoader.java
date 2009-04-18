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

import diSys.Common.Logger;

public class JarClassLoader extends URLClassLoader {
//    private URL url;
    private JarFile jar;
    private JarClassLoader jcl2;
    private URL url;
    public static URLClassLoader sysloader=(URLClassLoader)java.lang.ClassLoader.getSystemClassLoader();
	
    /**
	 * Creates a new JarClassLoader for the specified url.
	 * 
	 * @param url
	 *            the url of the jar file
     * @throws MalformedURLException 
	 */
    public JarClassLoader(File f) throws MalformedURLException {
    	 super(new URL[] {f.toURI().toURL() },sysloader);
    	 url=f.toURI().toURL();
    	try {
			jar=new JarFile(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 
    }

       public JarClassLoader() {
    		 super(new URL[] {});
	}

	public void Packages(){
   
    	for(Package p:this.getPackages()){
    	System.out.println(p);
    	}
    }
    public JarClassLoader getJcl(){
    	return jcl2;
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
    @Override
    public URL[] getURLs(){
		if(jcl2==null) return super.getURLs();
    	return jcl2.getURLs();
    	
    }
   
    @SuppressWarnings("unchecked")
	@Override
    public Class loadClass(String className) throws ClassNotFoundException{
    	Class c=null;
    	try {
    		/*c=this.findLoadedClass(className);
    		if(c!=null){
    			return c;
    		}*/
    		if(jcl2==null){
    		Logger.TraceInformation("Loading Class "+className+"From jar:"+jar.getName());
			c=this.findClass(className);
    		}else{
    		
    			Logger.TraceInformation("Loading Class "+className+"From jar:"+jcl2.jar.getName());
    			c=jcl2.loadClass(className);
    		}
		} catch (ClassNotFoundException e) {
			Logger.TraceInformation("Loading Class From system:"+className);
			c=super.loadClass(className);
		}
		//Class.forName(className);
		
    	return c;
    }
    public URL fileUrl(){
    	return url;
    }
    //public Class 
    @SuppressWarnings("unchecked")
	public Class GetClass(String name) throws ClassNotFoundException{
    	if(jcl2!=null)return jcl2.findClass(name);
    	return this.findClass(name);
    }
    @SuppressWarnings("unchecked")
	public void ResolveClass(Class c){
    	this.resolveClass(c);
    }
    public void updateClassLoader(File f){
    	try {
    		try {
				this.finalize();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			jcl2=new JarClassLoader(f);
			try {
				jar=new JarFile(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
		}
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
    public static void AddUrlToSystem(URL url){
			sysloader = (URLClassLoader)java.lang.ClassLoader.getSystemClassLoader();
			//sysloader.newInstance(new URL[]{url},this);
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
    }
    public void Finallize(){
    	try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  /*  @Override
    public String toString(){
    return "Jar Class loader :"+jar.getName();
    }
    */
}
