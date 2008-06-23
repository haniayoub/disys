package ex3.matchmaker;

import java.util.ArrayList;
import java.util.List;
import ex3.Constants.State;

/**
 * Description:
 * This class is a Class to store some information about a single Executer once we get a 
 * message from that executer  
 */
public class ExecutorInfo {
	String Name;//bind name of the executor

	State state;// the state of the executor (Idle/busy)

	//list of capabilities of the executor
	List<Character> availableActions = new ArrayList<Character>();

	/**
	 * this constructs an ExecutorInfo object with the given information
	 * @param bindName the excuter bind name
	 * @param s the state of the excuter
	 * @param capabilities the operatin that the excutor can handle
	 */
	public ExecutorInfo(String bindName, State s, List<Character> capabilities) {
		state = s;

		// add all of the operations that this Executer is able to perform
		for (int i = 0; i < capabilities.size(); i++) {
			availableActions.add(capabilities.get(i));

		}
	}

}
