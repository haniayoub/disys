package ex3.matchmaker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import ex3.Constants.*;

/**
 * 
 * @author saeed + mohamed
 *	THE match maker class 
 */
public interface Matchmaker extends Remote {
	public int assignExcutorID() throws RemoteException;

	public void updateExecutor(String bindName, State state,
			List<Character> capabilities) throws RemoteException;

	// Add your methods for communication with Task Queues here
	/**
	 *  this method tackes an operation as argumind and returns
	 *  a bind name of an idle excuter
	 */
	public String FindExe(Character operation) throws RemoteException;

	/**
	 *  this function is called when the match make want to assign a task for
	 *  excuter
	 * @param bindName excuter bind name
	 * @throws RemoteException 
	 */
	public void updateFortask(String bindName) throws RemoteException;

	/**
	 *  the method takes as argument an operation and returns the 
	 *  idle excuter which can excute the operation op
	 * @param op the operation
	 * @return the number of the IDLE excuters 
	 * @throws RemoteException
	 */
	public int excutersnum(char op) throws RemoteException;;
}
