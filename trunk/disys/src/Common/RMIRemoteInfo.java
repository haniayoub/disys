package Common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RMIRemoteInfo extends RemoteInfo implements Serializable{
private int port;
private String rmiId;
public RMIRemoteInfo(String ip, int port, String id) {
	super(ip);
	this.port = port;
	rmiId = id;
}
public int Port() {
	return port;
}
public String RMIId() {
	return rmiId;
}
public String GetRmiAddress(){
return	"rmi://"+ip+":"+port +"/"+rmiId;
}
}

