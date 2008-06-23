package ex3.taskqueue;

/*******************************************************************************
 * this class represents a task information
 * 
 * @author saeed +mohamed
 * 
 */
public class Task {
	int a;// first argument

	int b;// second argument

	int ID;// the ID of the task

	char Op;// the operation of the task

	/**
	 * this constructor creates a task with the given info
	 * 
	 * @param id
	 *            the ID
	 * @param x
	 *            argument 1
	 * @param y
	 *            argument 2
	 * @param op
	 *            operation
	 */
	public Task(int id, int x, int y, char op) {
		a = x;
		b = y;
		ID = id;
		Op = op;
	}

}
