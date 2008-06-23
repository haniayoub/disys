/* Implementation of the Matchmker.
 * Add packages and methods as you wish.
 */
package ex3.matchmaker;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import ex3.Constants.*;

@SuppressWarnings("serial")
/**
 * matchmaker implementaion
 * 
 */
public class MatchmakerImpl extends UnicastRemoteObject implements Matchmaker {

	private int regPort;

	private final static int INITIAL_PORT = 3001, MAX_PORT = 30000;

	/**
	 * our variables
	 * 
	 * @throws RemoteException
	 */

	// a hash table containing the operations as keys , and queues of executers
	// that
	// can perform the operation as data
	private ConcurrentHashMap<Character, ConcurrentLinkedQueue<String>> OpExecutors;

	// a hash table with the busy executers keyes by their bind names
	private ConcurrentHashMap<String, ExecutorInfo> BusyExec;

	// a hash table with the idle executers keyes by their bind names
	private ConcurrentHashMap<String, ExecutorInfo> IdleExec;

	// a counter that holds the max ID given until now
	private int IdCounter;

	// used to synchronise
	private Object lock = new Object();

	/**
	 * the constructor creates a registry and binds the match maker on it and
	 * creates the data structure of the operatins task executers queues and
	 * idle excuters and busy excuters queues
	 * 
	 * @throws RemoteException
	 */
	public MatchmakerImpl() throws RemoteException {
		regPort = createRegistry();
		System.out.println("Registry port is: " + regPort);
		try {
			Naming.rebind("//:" + regPort + "/matchmaker", this);
		} catch (Exception e) {
			System.out.println("Matchmaker failed to bind. Exiting.");
			System.exit(1);
		}

		// Implement the rest of the constructor here.
		// You may add parameters to this method.
		BusyExec = new ConcurrentHashMap<String, ExecutorInfo>();
		IdleExec = new ConcurrentHashMap<String, ExecutorInfo>();
		OpExecutors = new ConcurrentHashMap<Character, ConcurrentLinkedQueue<String>>();
		IdCounter = 0;
	}

	/*
	 * This method creates a registry on localhost, and prints the port that is
	 * used for it. Do not change this method.
	 */
	private int createRegistry() throws RemoteException {
		RemoteException failureException = null;
		for (int i = INITIAL_PORT; i <= MAX_PORT; ++i) {
			try {
				LocateRegistry.createRegistry(i);
				System.out.println();
				return i;
			} catch (RemoteException e) {
				failureException = e;
				System.out.print(".");
			}
		}
		System.err.println("Failed to create registry");
		throw failureException;
	}

	public synchronized int assignExcutorID() throws RemoteException {

		return IdCounter++;
	}

	/**
	 * this function adds a given excecutor to the lists of every operation the
	 * executor is capable of doing
	 * 
	 * @param bindName
	 *            the name of the executor
	 * @param Caps
	 *            list of chars 0- the capabilities of the executor
	 */
	private void AddExeToOps(String bindName, List<Character> Caps) {
		for (int i = 0; i < Caps.size(); i++) {
			char c = Caps.get(i);
			if (OpExecutors.containsKey(c))
				OpExecutors.get(c).add(bindName);
			else {
				OpExecutors.put(c, new ConcurrentLinkedQueue<String>());
				OpExecutors.get(c).add(bindName);
			}
		}
	}

	/**
	 * this function removes the given executor from every operation queue he
	 * could perform
	 * 
	 * @param bindName
	 *            the name of the executor to remove
	 * @param Caps
	 *            the executors capabilities
	 */
	private void removeExeToOps(String bindName, List<Character> Caps) {
		for (int i = 0; i < Caps.size(); i++) {
			OpExecutors.get(Caps.get(i)).remove(bindName);
		}
	}

	/**
	 * this method is called by the excuter when it wants to update it self at
	 * the matchmaker
	 */
	public void updateExecutor(String bindName, State state,
			List<Character> capabilities) throws RemoteException {

		if (BusyExec.containsKey(bindName))// if the Exec was busy
		{
			if (state == State.IDLE)// and changing his status to idle
			{
				// remove it from the hash table of buse execs
				BusyExec.remove(bindName);
				// add him to the operation's queues
				AddExeToOps(bindName, capabilities);
				// add him to the idle hash table
				IdleExec.put(bindName, new ExecutorInfo(bindName, state,
						capabilities));
			}

		} else {
			if (IdleExec.containsKey(bindName))// if he was idle
			{
				ExecutorInfo temp = IdleExec.get(bindName);

				// if he just wants to update his capabilities
				if (state == State.IDLE) {
					removeExeToOps(bindName, temp.availableActions);
					AddExeToOps(bindName, capabilities);
					IdleExec.put(bindName, new ExecutorInfo(bindName, state,
							capabilities));
				} else {// if he's changing to busy
					removeExeToOps(bindName, temp.availableActions);
					IdleExec.remove(bindName);
					BusyExec.put(bindName, new ExecutorInfo(bindName, state,
							capabilities));
				}
			} else {// if this is the fist time he enter's the matchMaker
				AddExeToOps(bindName, capabilities);
				IdleExec.put(bindName, new ExecutorInfo(bindName, state,
						capabilities));
			}

		}

	}

	// Implement your own methods here.
	/**
	 * this method tackes an operation as argumind and returns a bind name of an
	 * idle excuter
	 */
	public String FindExe(Character operation) throws RemoteException {
		if (OpExecutors.containsKey(operation)) {

			String s;
			synchronized (lock) {
				s = OpExecutors.get(operation).peek();
				if (s != null)
					updateFortask(s);
			}
			return s;
		} else
			return null;

	}

	public synchronized void updateFortask(String bindName)
			throws RemoteException {
		ExecutorInfo temp = IdleExec.get(bindName);
		removeExeToOps(bindName, temp.availableActions);

		IdleExec.remove(bindName);
		BusyExec.put(bindName, new ExecutorInfo(bindName, State.BUSY,
				temp.availableActions));
	}

	/**
	 * the method takes as argument an operation and returns the idle excuter
	 * which can excute the operation op
	 * 
	 * @param op
	 *            the operation
	 * @return the number of the IDLE excuters
	 * @throws RemoteException
	 */
	public int excutersnum(char operation) throws RemoteException {
		if (OpExecutors.get(operation) != null)
			return OpExecutors.get(operation).size();
		else
			return 0;
	}

	public static void main(String[] args) {
		try {
			MatchmakerImpl m = new MatchmakerImpl();
		} catch (RemoteException e) {
			System.out.print(".");
		}

	}

}