package ex3.taskqueue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import ex3.Constants;
import ex3.matchmaker.Matchmaker;

/*******************************************************************************
 * 
 * @author saeed + mohamed TaskQueue : this class responsible of running the
 *         operations Task Pools and connecting with the match maker and opining
 *         the shared file and closing it when all the pools finish the Tasks
 */
@SuppressWarnings("serial")
public class TaskQueue extends Thread {
	ConcurrentHashMap<Character, ConcurrentLinkedQueue<Task>> opTasks;

	Matchmaker matchmaker = null;

	FileWriter outFile;

	int numOfTasks;

	/**
	 * the constructor is responsible for creating the thread pools and running
	 * them
	 * 
	 * @param inputFile
	 *            the input file name
	 * @param regHost
	 *            the Host name of the matchmaker
	 * @param regPort
	 *            the port name of the match maker
	 */
	public TaskQueue(String inputFile, String regHost, int regPort) {
		numOfTasks = 0;
		opTasks = new ConcurrentHashMap<Character, ConcurrentLinkedQueue<Task>>();
		enqueueTasks(inputFile);

		try {
			matchmaker = (Matchmaker) Naming.lookup("//" + regHost + ":"
					+ regPort + "/matchmaker");
		} catch (Exception e) {
			System.out.println("Executor failed to find matchmaker: "
					+ e.getMessage());
			System.out.println("Exiting.");
			e.printStackTrace();
			System.exit(1);
		}
		try {
			outFile = new FileWriter(inputFile + ".out");
		} catch (IOException e) {

			e.printStackTrace();
		}

		Object[] cc = this.opTasks.keySet().toArray();
		taskThreadPool pools[] = new taskThreadPool[cc.length];
		// create Thread pools for the operations' queues
		for (int i = 0; i < cc.length; i++) {
			pools[i] = new taskThreadPool(this, (Character) cc[i], regHost
					+ ":" + regPort);
			pools[i].start();
		}

	}

	/**
	 * this method take as argument an input file and enques all the task in the
	 * opTasks queue accordint to the operation .
	 * 
	 * @param inputFile
	 *            the input file name
	 */
	private void enqueueTasks(String inputFile) {
		try {
			BufferedReader buffReader = new BufferedReader(new FileReader(
					inputFile));

			while (buffReader.ready()) {
				String line = buffReader.readLine();

				if ((line.length() == 0) || (line.charAt(0) == '#'))
					continue;

				StringTokenizer tokenizer = new StringTokenizer(line,
						Constants.DELIMITER);
				int taskId = Integer.parseInt(tokenizer.nextToken());
				String task = tokenizer.nextToken();
				char op = task.charAt(0);
				int a = Integer.parseInt(tokenizer.nextToken());
				int b = Integer.parseInt(tokenizer.nextToken());
				Task opInfo = new Task(taskId, a, b, op);
				if (!opTasks.containsKey(op)) {

					opTasks.put(op, new ConcurrentLinkedQueue<Task>());
					opTasks.get(op).add(opInfo);
					numOfTasks++;
					continue;
				}
				opTasks.get(op).add(opInfo);

				numOfTasks++;

			}
		} catch (FileNotFoundException e) {
			System.out.println("in TaskQueue: Input file not found");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("in TaskQueue: Cant read from input file");
		}
	}

	public static void main(String[] args) {

		TaskQueue Tq = new TaskQueue(args[2], args[0], Integer
				.parseInt((args[1])));
		try {
			Tq.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		try {
			while (Tq.numOfTasks != 0)
				try {
					sleep(1000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			Tq.outFile.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		System.out.println("finished!!!!");
	}
}
