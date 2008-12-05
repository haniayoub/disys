package AutoUpdate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassLoader extends URLClassLoader {
    private URL url;
    private JarFile jar;
	private File file;
    /**
	 * Creates a new JarClassLoader for the specified url.
	 * 
	 * @param url
	 *            the url of the jar file
	 */
    public JarClassLoader(URL url,String filePath) {
    	 super(new URL[] {url });
    	 this.url=url;
        // filePath = "jar:file://" + filePath + "!/";
    	file=new File(filePath);
        try {
			jar=new JarFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
    }

    /**
	 * Returns the name of the jar file main class, or null if no "Main-Class"
	 * manifest attributes was defined.
	 */
    public String getMainClassName() throws IOException {
        URL u = new URL("jar", "", url + "!/");
        JarURLConnection uc = (JarURLConnection)u.openConnection();
        Attributes attr = uc.getMainAttributes();
        return attr != null ? attr.getValue(Attributes.Name.MAIN_CLASS) : null;
    }

    /**
	 * Invokes the application in this jar file given the name of the main class
	 * and an array of arguments. The class must define a static method "main"
	 * which takes an array of String arguemtns and is of return type "void".
	 * 
	 * @param name
	 *            the name of the main class
	 * @param args
	 *            the arguments for the application
	 * @exception ClassNotFoundException
	 *                if the specified class could not be found
	 * @exception NoSuchMethodException
	 *                if the specified class does not contain a "main" method
	 * @exception InvocationTargetException
	 *                if the application raised an exception
	 */
    @SuppressWarnings("unchecked")
	public void invokeClass(String name, String[] args)
        throws ClassNotFoundException,
               NoSuchMethodException,
               InvocationTargetException
    {
        Class c = loadClass(name);
        Method m = c.getMethod("main", new Class[] { args.getClass() });
        m.setAccessible(true);
        int mods = m.getModifiers();
        if (m.getReturnType() != void.class || !Modifier.isStatic(mods) ||
            !Modifier.isPublic(mods)) {
            throw new NoSuchMethodException("main");
        }
        try {
            m.invoke(null, new Object[] { args });
        } catch (IllegalAccessException e) {
            // This should not happen, as we have disabled access checks
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
