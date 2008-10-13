package Common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RemoteInfo implements Serializable {
	protected String ip;

	public RemoteInfo(String ip) {
		super();
		this.ip = ip;
	}

	public String Ip() {
		return ip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		final RemoteInfo other = (RemoteInfo) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		return true;
	}
}
