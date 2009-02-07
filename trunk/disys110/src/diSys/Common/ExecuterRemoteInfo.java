package diSys.Common;

import java.io.Serializable;

import diSys.Networking.RMIItemCollector;
import diSys.Networking.RemoteItemReceiver;


@SuppressWarnings("serial")
public class ExecuterRemoteInfo implements Serializable {
	private RMIRemoteInfo itemRecieverInfo;
	private RMIRemoteInfo resultCollectorInfo;

	public ExecuterRemoteInfo(String ip, int itemRecieverPort,
			int resultCollectorPort) {
		itemRecieverInfo = new RMIRemoteInfo(ip, itemRecieverPort,
				RemoteItemReceiver.GlobalId);
		resultCollectorInfo = new RMIRemoteInfo(ip, resultCollectorPort,
				RMIItemCollector.GlobalId);
	}

	public RMIRemoteInfo getItemRecieverInfo() {
		return itemRecieverInfo;
	}

	public RMIRemoteInfo getResultCollectorInfo() {
		return resultCollectorInfo;
	}

	public String toString() {
		return "Executer:" + resultCollectorInfo.ip + " IRPort = "
				+ itemRecieverInfo.Port() + " RCPort = "
				+ resultCollectorInfo.Port();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((itemRecieverInfo == null) ? 0 : itemRecieverInfo.hashCode());
		result = prime
				* result
				+ ((resultCollectorInfo == null) ? 0 : resultCollectorInfo
						.hashCode());
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
		final ExecuterRemoteInfo other = (ExecuterRemoteInfo) obj;
		if (itemRecieverInfo == null) {
			if (other.itemRecieverInfo != null)
				return false;
		} else if (!itemRecieverInfo.equals(other.itemRecieverInfo))
			return false;
		if (resultCollectorInfo == null) {
			if (other.resultCollectorInfo != null)
				return false;
		} else if (!resultCollectorInfo.equals(other.resultCollectorInfo))
			return false;
		return true;
	}
}
