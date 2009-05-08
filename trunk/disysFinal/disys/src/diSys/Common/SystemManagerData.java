package diSys.Common;

import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class SystemManagerData implements Serializable {
	public LinkedList<ExecuterRemoteInfo> activeExecuters;
	public LinkedList<ExecuterRemoteInfo> enActiveExecuters;
	public LinkedList<ClientRemoteInfo> Clients;
	public int Version = 0;

	public SystemManagerData() {
		activeExecuters = new LinkedList<ExecuterRemoteInfo>();
		enActiveExecuters = new LinkedList<ExecuterRemoteInfo>();
		Clients = new LinkedList<ClientRemoteInfo>();
	}

}
