package CalcExecuterDemo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import AutoUpdate.JarClassLoader;
import Common.IExecutor;
import Common.RemoteItem;
import Executor.TaskExecuter;
import WorkersSystem.WorkER.AWorker;
import WorkersSystem.WorkER.WorkerCollection;

public class MainTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		String filePath = "D:\\study\\projects\\Distrebuted\\WorkSpace\\disys\\Release\\NewJar.jar";
		URL url = new File("jar:file://" + filePath + "!/").toURI().toURL();

		System.out.println(url);
	    JarClassLoader jcl=new JarClassLoader(url,filePath);  
		
		//jcl.atts();
		BlockingQueue<RemoteItem<CalcTask>> tasks = 
				new LinkedBlockingQueue<RemoteItem<CalcTask>>();
		    TaskExecuter<CalcTask,CalcResult,Calculator> taskExecuter = new TaskExecuter<CalcTask,CalcResult,Calculator>(new Calculator(),"qqq111",tasks,null);
		    WorkerCollection ExecutersCollection=new WorkerCollection(taskExecuter,3);
		    ExecutersCollection.startWorking();
		    for (int i=0;i<10;i++){
		    	tasks.add(new RemoteItem<CalcTask>(new CalcTask(i),null));
		    }
		    try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			
		    IExecutor c=null;
			//JarAnalyzer jar = new JarAnalyzer( jarFile );
			try {
				System.out.println("LoadingFromJar ------------------------------------");
				for(String className:jcl.getSubClassesof(IExecutor.class, false)) System.out.println(className);
				//jcl.
				c = (IExecutor) jcl.loadClass("CalcExecuterDemo.Calculator2").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			for(AWorker te:ExecutersCollection.getWorkerList()){
		    	((TaskExecuter)te).UpdateExecuter(c);
		    }
			   System.out.println("***********{SYSTEM IS UP TO DATE}*************");
		    // Sleep(10);
		    try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			
		    
		    for (int i=0;i<10;i++){
		    	tasks.add(new RemoteItem<CalcTask>(new CalcTask(i),null));
		    }
		    //ClassLoader("D:\\study\\projects\\Distrebuted\\WorkSpace\\disys\\Release\\Client.jar");
		
		    //for(String className:jcl.getClasses()) System.out.println(className);
		    System.out.println("***********************************************");
		    for(String className:jcl.getSubClassesof(IExecutor.class, false)) System.out.println(className);
		}

}
