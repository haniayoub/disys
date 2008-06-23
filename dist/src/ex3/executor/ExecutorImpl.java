package ex3.executor;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.Integer;
import java.util.Random;

import ex3.Constants.*;
import ex3.matchmaker.Matchmaker;




@SuppressWarnings("serial")
public class ExecutorImpl extends UnicastRemoteObject implements Executor {

	public Random random=new Random();
	
	private boolean selfInitiatedStateChange=false;
	private List<Character> availableActions = new ArrayList<Character>();
	private int myID;
	State state;

	private Matchmaker matchmaker = null;  
	
	private String logFile = "Executor";	
	public final static String ERROR_MESSAGE = "EXECUTOR_ERROR";

	public ExecutorImpl(String regHost,int regPort) throws Exception{
		try { 
            matchmaker  = (Matchmaker)Naming.lookup("//" + 
                         regHost + ":" + regPort+ "/matchmaker"); 
		} catch (Exception e) { 
            System.out.println("Executor failed to find matchmaker: " + 
                               e.getMessage());
            System.out.println("Exiting."); 
            e.printStackTrace(); 
            System.exit(1);
        } 
        try { 
            myID = matchmaker.assignExcutorID(); 
        } catch (Exception e) { 
            System.out.println("Executor failed receive ID: " + 
                                    e.getMessage());
            System.out.println("Exiting.");
            e.printStackTrace();
            System.exit(1);
        } 
		try{
			
			Naming.rebind("//:" + regPort + "/Executor" + myID,this);
		}catch(Exception e){
			Naming.bind("//:" + regPort + "/Executor" + myID,this);
		}
		state = State.IDLE;
	}
	
	private void registerWithMatchmaker() {
		try {
			matchmaker.updateExecutor("Executor" + myID,state,availableActions);
			System.out.println("Executor" + myID + " op lis : "+availableActions);
		} catch (Exception e){ 
			 System.out.println("Executor failed to register with matchmaker: " + 
                     e.getMessage());
			 System.out.println("Exiting.");
			 e.printStackTrace();
			 System.exit(1);	 
		}  	
	}
	
	private void log(String result){
		try {
			FileWriter fw = new FileWriter(logFile,true);
			fw.write(new SimpleDateFormat().format(new Date()) + " - " + result+ "\n");
			fw.close();
		} catch (IOException e) {}
		
	}

	
	private synchronized void switchState(State newState) 
						throws RemoteException{
		
		if (state == State.BUSY && newState == state){
			if (!selfInitiatedStateChange){
				throw new RuntimeException("Correctness error: attempting " +
					"to invoke task on busy executor");
			}else{
				throw new RemoteException("Executor is busy");
			}
		}

		
		state = newState;
		try{
			if (state == State.IDLE){
				matchmaker.updateExecutor("Executor" + myID,state,availableActions);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println("Failed to update matchmaker, exiting");
			System.exit(1);
		}
	}
	
	private void addOperation(char op){
		availableActions.add(op);
	}
	
	private void removeOperation(char op){
		availableActions.remove(op);
	}
	
	/**
	 * This is the main function called by remote entities
	 * @throws RemoteException
	 */
	public String execute(char op, int a,int b) throws RemoteException{
		switchState(State.BUSY);
		try{
			if (!availableActions.contains(op)){
				throw new RuntimeException("Correctness error: " +
						"attempting to invoke unsupported operation");
			}
			log(op+":"+a+":"+b);
			System.out.print(this.myID+ " : "+a+" "+op+" "+b + "   ");
			return Operation.doOp(op,a,b); 
		}catch(IllegalArgumentException ex){
			throw new RemoteException("This action "+op+" is not supported");
		}
		finally{
			switchState(State.IDLE);
		}
	}
	
	public static void main(String[] args) throws Exception{
		if (args.length < 2) {
			System.err.println("Executor requires host address and port number.");
			System.err.println("Exiting.");
			System.exit(1);
		}
		int port = (new Integer(args[1])).intValue();
		ExecutorImpl e = new ExecutorImpl(args[0],port);
		for(int i=2;i<args.length;i++){
			e.addOperation(args[i].charAt(0));
		}
		e.registerWithMatchmaker();
	}
}
