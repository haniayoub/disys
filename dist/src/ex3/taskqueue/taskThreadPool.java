package ex3.taskqueue;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/*******************************************************************************
 * 
 * @author saeed + mohamed taskThreadPool : this class responsible of single
 *         Operation task queue thread Pool
 */
@SuppressWarnings("serial")
public class taskThreadPool extends Thread {

	// the maximum number of threads allowed
	final int maxThreads = 50;

	// a hash table containing the threads
	ConcurrentHashMap<Integer, Thread> thread;

	// the operation this thread pool is responsible for
	char Op;

	// the address of the match maker
	String address;

	// used to assign IDs to the threads
	int threadkey = 0;

	// instance of the Task Queue
	TaskQueue taskQ;

	// the number of tasks in the Queue
	int tasknum;

	/**
	 * this constructor creates a thread pool with the given info
	 * 
	 * @param Tq
	 *            a reference to the Task Queue
	 * @param op
	 *            the operation the pool will be responsible for
	 * @param adds
	 *            the address of the match maker
	 */
	public taskThreadPool(TaskQueue Tq, char op, String adds) {
		Op = op;
		taskQ = Tq;
		tasknum = taskQ.opTasks.get(op).size();
		address = adds;
		// the thread pool data structure
		thread = new ConcurrentHashMap<Integer, Thread>();
	}

	/***************************************************************************
	 * killme: this method is called when one of the worker threads dont have
	 * any task to do , to remove it from the pool
	 */
	public synchronized void killme(int id) {
		Thread t = thread.get(id);
		// remove the thread from the pool
		thread.remove(id);
		// terminate the thread
		t.stop();
	}

	/***************************************************************************
	 * killme: this function is called in TaskQueue to run a single operatin
	 * thread pool handler .
	 */
	public void run() {
		int exnum = 0;
		try {
			exnum = taskQ.matchmaker.excutersnum(Op);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// create threads as the minimum of available executors and the number
		// of tasks
		for (int i = 0; i < exnum && i < this.maxThreads && i < tasknum; i++) {
			Thread t;
			thread.put(threadkey, t = new workerThread(threadkey++, taskQ,
					address, Op, this));
			t.start();
		}

		// this flag is used to make the pool sleep when it has nothing to do
		boolean sleep = false;

		while (tasknum != 0) {

			try {
				if (sleep)
					sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			sleep = true;
			try {
				exnum = taskQ.matchmaker.excutersnum(Op);
			} catch (RemoteException e) {

				e.printStackTrace();
			}

			// create a new thread if there is an available executor and there
			// is tasks
			// waiting to be executed and we didnt reach the maximum number
			// of threads allowed
			if (exnum > thread.size()
					&& taskQ.opTasks.get(Op).size() > thread.size()
					&& thread.size() < maxThreads) {
				Thread t;
				thread.put(threadkey, t = new workerThread(threadkey++, taskQ,
						address, Op, this));
				t.start();
				sleep = false;
			}
			if (thread.isEmpty() && taskQ.opTasks.get(Op).size() == 0) {
				break;
			}

		}

	}
}
