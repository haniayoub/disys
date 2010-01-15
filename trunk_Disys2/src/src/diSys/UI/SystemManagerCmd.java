package diSys.UI;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;

import diSys.Common.ClientRemoteInfo;
import diSys.Common.ExecuterRemoteInfo;
import diSys.Common.FileManager;
import diSys.Common.ItemInfo;
import diSys.Common.RMIRemoteInfo;
import diSys.Common.SystemManagerData;
import diSys.SystemManager.ISystemManager;
import diSys.SystemManager.SystemManager;


public class SystemManagerCmd {
	
	@SuppressWarnings("unchecked")
	public static ISystemManager sysManager;
	public static RMIRemoteInfo sysmRi;
	public static SystemManagerData sysData;
	
	private static String conFile =  "W:\\Regression2.0\\Tools\\Disys\\cmd\\DisysCmdConnection.txt"; 
	private static String infoFile = "W:\\Regression2.0\\Tools\\Disys\\cmd\\DisysCmdInfo.txt"; 
	private static String newline = "\r\n";
	
	public static void main(String[] args)
	{
		try
		{
			String cmd = args[0];
			if(cmd.equals("connect"))
			{
				String 	hostName 	= args[1];
				int 	port 		= Integer.parseInt(args[2]);
				if(connect(hostName, port))
				{
					System.out.println("Succeed. Now connected.");
					getSysDataInfo();
				}	
				return;
			}
			else if(cmd.equals("disconnect"))
			{
				FileManager.WriteFile(conFile,"".getBytes());
				System.out.println("Disconnected.");
				return;
			}
			else if(cmd.equals("--help") || cmd.equals("-help") || cmd.equals("?") || cmd.equals("\\?"))
			{
				usage();
			}
			
			if(!connect())
				return;
			
			if(cmd.equals("getInfo"))
			{
				getSysDataInfo();
			}
			else if(cmd.equals("getActives"))
			{
				printExecuters(sysData.activeExecuters);
			}
			else if(cmd.equals("getInactives"))
			{
				printExecuters(sysData.enActiveExecuters);
			}
			else if(cmd.equals("getClients"))
			{
				printClients(sysData.Clients);
			}
			else if(cmd.equals("getTasks"))
			{
				String ip = args[1];
				int irPort= Integer.parseInt(args[2]);
				int rcPort= Integer.parseInt(args[3]);
				getTasks(ip, irPort, rcPort);
			}
			else
			{
				usage();
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception thrown...");
			try 
			{
				FileManager.WriteFile(conFile,"0".getBytes());
			} 
			catch (FileNotFoundException e1) 
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	private static void usage() 
	{
		System.out.println("-------------------------------[DisysCmd]----------------------------------");
		System.out.println("Usage: [Command] [Parameters]");
		System.out.println("  - connect [hostName] [port]");
		System.out.println("  - disconnect");
		System.out.println("  - getInfo");
		System.out.println("  - getActives");
		System.out.println("  - getInactives");
		System.out.println("  - getClients");
		System.out.println("example : java -jar DisysCmd.jar HWS128 5555");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println();
	}
	
	@SuppressWarnings("unchecked")
	private static void getTasks(String ip, int irPort, int rcPort) throws RemoteException
	{
		ExecuterRemoteInfo exri = new ExecuterRemoteInfo(ip, irPort, rcPort);
		if(!sysData.activeExecuters.contains(exri))
		{
			if(sysData.enActiveExecuters.contains(exri))
			{
				writeToInfoFile("$" + newline +
								"Executer is not active.");
				System.out.println("Executer is not active.");
			}
			else
			{
				writeToInfoFile("$" + newline +
								"Executer does not exist.");
				System.out.println("Executer does not exist.");
			}
			return;
		}
		
		LinkedList<String> tasks = sysManager.getQueueInfo(exri);
		ItemInfo ii = sysManager.getCurrentTask(exri); 
		tasks.addFirst( ii == null  	? 
							"No running tasks..."	:
							"-> " + ii.toString());
		
		String tasksString = "";
		for(String t : tasks)
		{
			System.out.println(t);
			tasksString = tasksString + t + newline;
		}
		writeToInfoFile(tasksString);
	}

	private static void printClients(LinkedList<ClientRemoteInfo> clients) 
	{
		String clientsString = "";
		if(clients.size() == 0)
		{
			System.out.println("No clients...");
		}
		for(ClientRemoteInfo cri : clients)
		{
			System.out.println("IP: " + cri.Ip() + " - " + "ID: " + cri.Id());
			clientsString = clientsString + cri.Ip() + newline;
		}
		writeToInfoFile(clientsString);
	}

	private static void printExecuters(LinkedList<ExecuterRemoteInfo> executers) 
	{
		String executersString = "";
		if(executers.size() == 0)
		{
			System.out.println("No executers...");
		}
		for(ExecuterRemoteInfo eri : executers)
		{
			System.out.println("Name: " + eri.getName() + " - " + 
							   "IP: " + eri.getResultCollectorInfo().Ip() + " - " +
							   "IRPort: " + eri.getItemRecieverInfo().Port() + " - " +
							   "RCPort: " + eri.getItemRecieverInfo().Port() + " - " +
							   "Ver: " + eri.getVersion());
			executersString = executersString + 
				eri.getName() + " " +
				eri.getResultCollectorInfo().Ip() + " " +
				eri.getItemRecieverInfo().Port() + " " +
				eri.getVersion() + newline;
		}
		writeToInfoFile(executersString);
	}

	private static void getSysDataInfo()
	{
		String hostName = getHostName();
		int port = getPort();
		int version = sysData.Version;
		int activeEx = sysData.activeExecuters.size();
		int inactiveEx = sysData.enActiveExecuters.size();
		int clients = sysData.Clients.size();
		
		System.out.println("Info:"                                   			);
		System.out.println("  Host Name  : " + hostName		       				);
		System.out.println("  Port       : " + port		       	 				);
		System.out.println("  Version    : " + version			   	 			);
		System.out.println("  Active Ex  : " + activeEx							);
		System.out.println("  Inactive Ex: " + inactiveEx						);
		System.out.println("  Clients    : " + clients							);
		
		writeToInfoFile(hostName 	+ newline +
						port 		+ newline +
						version		+ newline +
						activeEx	+ newline +
						inactiveEx	+ newline +
						clients);
	}


	private static void writeToInfoFile(String str){
		try
		{
			FileManager.WriteFile(infoFile,str.getBytes());
		}
		catch(Exception e)
		{
			System.out.println(infoFile + ": File doesn't exist");
			e.printStackTrace();
		}
	}

	private static boolean connect(String hostName, int port) 
	{
		try
		{
			tryToConnect(hostName, port);
		}
		catch(Exception e)
		{
			writeToInfoFile("$" + newline + 
			"Failed to connect to - Host Name: " + hostName + ". Port: " + port);
			System.out.println("Failed to connect to - Host Name: " + hostName + ". Port: " + port);
			return false;
		}
		return true;
	}
	private static boolean connect() 
	{
		String hostName;
		int port;
		try
		{
			hostName = getHostName();
			port = getPort();
		} 
		catch (Exception e) 
		{
			System.out.println("Failed to load hotsName and port");
			e.printStackTrace();
			return false;
		}
		return connect(hostName, port);
	}
	
	private static void tryToConnect(String hostName, int port) throws Exception
	{
		System.out.println("Trying to connect (" + hostName + " " + port + ")...");;
		sysmRi = new RMIRemoteInfo(hostName, port, SystemManager.GlobalID);
		sysManager = diSys.Networking.NetworkCommon.loadRMIRemoteObject(sysmRi);
		
		if(sysManager == null)
		{
			throw new Exception("Could not load System Manager RMI object");
		}
		else
		{
			Integer portI = port; 
			String lines = hostName+"\r\n"+portI.toString();
			FileManager.WriteFile(conFile,lines.getBytes());
			fillDB();
			return;
		}
	}

	private static void fillDB() 
	{
		try 
		{
			if(sysManager == null)
			{
				sysManager = diSys.Networking.NetworkCommon.loadRMIRemoteObject(sysmRi);
			}
			sysData = sysManager.GetData();
		} 
		catch (RemoteException e) 
		{
			System.out.println("Failed to Load system manager Data");
			sysManager=null;
			e.printStackTrace();
		}
	}
	
	private static String getHostName()
	{
		try
		{
			return diSys.Common.FileManager.ReadLines(conFile)[0];
		}
		catch(Exception e)
		{
			System.out.println(conFile + ": File doesn't exist");
			e.printStackTrace();
			return null;
		}
	}
	
	private static int getPort() 
	{
		try
		{
			return Integer.parseInt(diSys.Common.FileManager.ReadLines(conFile)[1]);
		}
		catch(Exception e)
		{
			System.out.println(conFile + ": File doesn't exist");
			e.printStackTrace();
			return -1;
		}
	}
}

