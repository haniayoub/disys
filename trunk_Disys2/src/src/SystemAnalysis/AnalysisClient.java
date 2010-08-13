package SystemAnalysis;

import java.rmi.RemoteException;
import java.util.HashMap;
import diSys.Client.RemoteClient;
import diSys.Common.ATask;
import diSys.Common.SystemStatistics;
@SuppressWarnings("unchecked")
public class AnalysisClient {
	
	private static int chunkSize;
	private int numOfTasks;
	private static String address;
	private static int port;
	private HashMap<RemoteClient, TaskType> clients = new HashMap<RemoteClient, TaskType>();
	
	public enum TaskType {
		DOWNLOAD {	@Override
					public RemoteClient getClient() { return new RemoteClient<DownloadTask, DownloadResult>(address, port, chunkSize); }
					@Override
					public ATask getRandomTask() { return new DownloadTask(); }
		}, 
		MatrixMul {	@Override
					public RemoteClient getClient() { return new RemoteClient<MatrixMulTask, MatrixMulResult>(address, port, chunkSize); }
					@Override
					public ATask getRandomTask() {
						return new MatrixMulTask();
					}
		};
		public abstract RemoteClient getClient();
		public abstract ATask getRandomTask();
	};
	
	public AnalysisClient(String address, int port, int chunkSize, int numOfTasks, TaskType[] types)
	{
		AnalysisClient.address = address;
		AnalysisClient.port = port;
		AnalysisClient.chunkSize = chunkSize;
		this.numOfTasks = numOfTasks;
		
		for(TaskType tt : types)
			clients.put(tt.getClient(), tt);
		for(RemoteClient client : clients.keySet())
			client.Start();
	}
	
	public void run()
	{
		for(RemoteClient client : clients.keySet())
			try {
				client.clientSystem.sysManager.ResetSystemStatistics();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		for(int i=0; i<numOfTasks; i++)
			for(RemoteClient client : clients.keySet())
				client.AddTask(clients.get(client).getRandomTask());
		for(int i=0; i<numOfTasks; i++)
			for(RemoteClient client : clients.keySet())
				try {client.GetResult();} 
				catch (Exception e) { e.printStackTrace();}
		for(RemoteClient client : clients.keySet())
			client.Stop();
		for(RemoteClient client : clients.keySet())		
		{
			SystemStatistics ss = null;
			
			try { ss = client.clientSystem.sysManager.GetSystemStatistics(); } 
			catch (RemoteException e) { e.printStackTrace(); }
			System.out.println(ss.toString());
		}
	}
	
	public static void main(String[] args) throws Exception {
		AnalysisClient ac = new AnalysisClient("hayoub-mobl2", 5555, 1, 100, new TaskType[] {TaskType.MatrixMul});
		ac.run();
	}
}
