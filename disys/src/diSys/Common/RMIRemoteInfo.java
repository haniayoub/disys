package diSys.Common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RMIRemoteInfo extends RemoteInfo implements Serializable {
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

	public String GetRmiAddress() {
		return "rmi://" + ip + ":" + port + "/" + rmiId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + port;
		result = prime * result + ((rmiId == null) ? 0 : rmiId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RMIRemoteInfo other = (RMIRemoteInfo) obj;
		if (port != other.port)
			return false;
		if (rmiId == null) {
			if (other.rmiId != null)
				return false;
		} else if (!rmiId.equals(other.rmiId))
			return false;
		return true;
	}
	
	public String toString()
	{
		return rmiId;
	}
}
