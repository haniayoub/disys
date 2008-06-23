package ex3.taskqueue;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import ex3.executor.Executor;
import ex3.Constants;

/*******************************************************************************
 * 
 * @author saeed + mohamed workerThread : this class represents a worker thread
 *         whitch works on a single operation task queue the worker thread is
 *         responsible for connecting with the match maker and getting a
 *         suitable excutor
 */
@SuppressWarnings("serial")
public class workerThread extends Thread {
	// all the tasks that this thread should execute
	// instance of the Task Queue
	TaskQueue taskQ;

	// the address of the match maker
	String address;

	// used to synchronize
	Object lock = new Object();

	// used to synchrinize
	Object numOfTaskslock = new Object();

	// the operation this thread is handeling
	char Op;

	// the id of the thread
	int threadId;

	// an instance of the threadPool that created this thread
	taskThreadPool father;

	/**
	 * this constructor creates a thread with the given info
	 * 
	 * @param id
	 *            id of the thread
	 * @param Tq
	 *            reference to the Task Queue
	 * @param ad
	 *            address of the match maker
	 * @param op
	 *            the operation to handle
	 * @param f
	 *            a reference to the thread pool
	 */
	public workerThread(int id, TaskQueue Tq, String ad, char op,
			taskThreadPool f) {
		taskQ = Tq;
		address = ad;
		Op = op;
		threadId = id;
		father = f;
	}

	/**
	 * Description : this function is the work of the worker thread the thread
	 * tries to poll a task from the operation queue if he fails more than ones
	 * or if he dont find an excuter then he asks the threadTaskPool to kill him
	 */
	public void run() {

		// as long as there are tasks that hasn't finished
		while (true) {

			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}
			Task t = null;
			// pop a task from the queue
			t = taskQ.opTasks.get(Op).poll();

			if (t == null) {
				// if there are no tasks available ask father to be killed
				father.killme(threadId);
				continue;
			}

			String res = null;
			try {
				// request an executor
				res = taskQ.matchmaker.FindExe(t.Op);

			} catch (RemoteException e1) {

				e1.printStackTrace();
				// error occured , return the task to the queue
				taskQ.opTasks.get(Op).add(t);
				continue;
			}
			if (res == null) {
				// error occured , return the task to the queue

				taskQ.opTasks.get(Op).add(t);
				// no executors available , the thread is unnecessery
				father.killme(threadId);
				continue;
			} else {
				Executor exe = null;
				try {
					// contact the exocutor
					exe = (Executor) Naming.lookup("//" + address + "/" + res);

					String result;
					try {
						result = exe.execute(t.Op, t.a, t.b);

					} catch (Exception e) {
						// error occured , return the task to the queue
						taskQ.opTasks.get(Op).add(t);
						continue;
					}
					if (result == null || result.equals("")) {
						// error occured , return the task to the queue
						taskQ.opTasks.get(Op).add(t);
						continue;
					}

					if (result.equals("EXECUTOR_ERROR")) {
						// error occured , return the task to the queue
						taskQ.opTasks.get(Op).add(t);
						continue;
					}

					try {
						synchronized (taskQ.outFile) {
							// write the result to the output file
							taskQ.outFile.write(t.ID + Constants.DELIMITER
									+ result + "\r\n");
						}
					} catch (IOException e) {
						System.out.println("Unable to write to file!");
						// if failed to write to the file , execute the task again
						taskQ.opTasks.get(Op).add(t);
						continue;
					}
					synchronized (numOfTaskslock) {
						//decrease the number of tasks
						taskQ.numOfTasks--;
					}

				} catch (Exception e) {
					System.out.println(threadId
							+ "Thread failed to find EXECUTER: "
							+ e.getMessage());
					System.out.println("Exiting.");
					e.printStackTrace();
					System.exit(1);
				}

			}

		}
	}
}
