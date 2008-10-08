package Common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RemoteInfo implements Serializable{
private String ip;
private int port;
private String id;
public RemoteInfo(String ip, int port, String id) {
	super();
	this.ip = ip;
	this.port = port;
	this.id = id;
}
public String Ip() {
	return ip;
}
public int Port() {
	return port;
}
public String Id() {
	return id;
}
public String GetRmiAddress(){
return	"rmi://"+ip+":"+port +"/"+id;
}
}


