package diSys.Common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClientRemoteInfo extends RemoteInfo implements Serializable {
	private long id;

	public ClientRemoteInfo(String ip, long id) {
		super(ip);
		this.id = id;
	}
	
	public long Id() {
		return id;
	}

	public String toString() {
		return "Client://" + ip + "/" + id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ClientRemoteInfo other = (ClientRemoteInfo) obj;
		if (id != other.id)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		return true;
	}
}
