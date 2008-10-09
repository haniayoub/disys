package Common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RemoteInfo implements Serializable{
protected String ip;
public RemoteInfo(String ip) {
	super();
	this.ip = ip;
}
public String Ip() {
	return ip;
}
}


