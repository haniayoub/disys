package diSys.SystemManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import diSys.Common.ExecuterRemoteInfo;


public class ExecutersList {
	private String fileName;

	public ExecutersList(String FileName) {
		fileName = FileName;
	}

	public LinkedList<ExecuterRemoteInfo> LoadExecutersList() {
		LinkedList<ExecuterRemoteInfo> $ = new LinkedList<ExecuterRemoteInfo>();
		FileReader fr;
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			diSys.Common.Logger.TraceWarning("Executers List File" + fileName
					+ " Not Found .", e);
			return $;
		}
		diSys.Common.Logger.TraceInformation("Loading executers From File : "
				+ fileName);
		BufferedReader br = new BufferedReader(fr);
		String Line;
		try {
			while ((Line = br.readLine()) != null) {
				String[] args = Line.split(" ");
				if (args.length < 3)
					continue;
				String ip = args[0];
				int irPort = Integer.parseInt(args[1]);
				int icPort = Integer.parseInt(args[2]);
				$.add(new ExecuterRemoteInfo(ip, irPort, icPort));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return $;
	}

	public void addExecuterToFile(String address, int irPort, int rcPort) {

		LinkedList<ExecuterRemoteInfo> $ = LoadExecutersList();
		ExecuterRemoteInfo Eri = new ExecuterRemoteInfo(address, irPort, rcPort);
		if ($.contains(Eri))
			return;
		$.add(Eri);
		WriteListToFile($);

	}

	private void WriteListToFile(LinkedList<ExecuterRemoteInfo> $) {
		FileWriter fw;
		try {
			fw = new FileWriter(fileName);
		} catch (FileNotFoundException e) {
			diSys.Common.Logger.TraceWarning("Executers List File" + fileName
					+ " Not Found .", e);
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		BufferedWriter bw = new BufferedWriter(fw);
		for (ExecuterRemoteInfo ri : $) {
			String Line = ri.getItemRecieverInfo().Ip() + " "
					+ ri.getItemRecieverInfo().Port() + " "
					+ ri.getResultCollectorInfo().Port();
			// Common.Logger.TraceInformation("Adding executer"+Line+" to File :
			// "+ExecutersList);
			try {

				bw.write(Line + "\r\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void remove(ExecuterRemoteInfo exir) {
		LinkedList<ExecuterRemoteInfo> $ = LoadExecutersList();
		$.remove(exir);
		WriteListToFile($);
	}

}
