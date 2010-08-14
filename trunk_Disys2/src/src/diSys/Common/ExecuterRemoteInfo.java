package diSys.Common;

import java.io.Serializable;
import java.text.DecimalFormat;

import diSys.Networking.RMIItemCollector;
import diSys.Networking.RemoteItemReceiver;

@SuppressWarnings("serial")
public class ExecuterRemoteInfo implements Serializable {
	private RMIRemoteInfo itemRecieverInfo;
	private RMIRemoteInfo resultCollectorInfo;
	private int version;
	private String name;
	public double EP = -1;
	public int BS = 0;
	public int BC = 100;
	public double PP;
	public double EFF_BE;
	public boolean Enabled = true;

	public ExecuterRemoteInfo(String ip, int itemRecieverPort,
			int resultCollectorPort) {
		itemRecieverInfo = new RMIRemoteInfo(ip, itemRecieverPort,
				RemoteItemReceiver.GlobalId);
		resultCollectorInfo = new RMIRemoteInfo(ip, resultCollectorPort,
				RMIItemCollector.GlobalId);
		version = -1;
		this.name = "NoName";
	}
	public ExecuterRemoteInfo(String ip, int itemRecieverPort,
			int resultCollectorPort, String name) {
		itemRecieverInfo = new RMIRemoteInfo(ip, itemRecieverPort,
				RemoteItemReceiver.GlobalId);
		resultCollectorInfo = new RMIRemoteInfo(ip, resultCollectorPort,
				RMIItemCollector.GlobalId);
		version = -1;
		this.name = name;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public RMIRemoteInfo getItemRecieverInfo() {
		return itemRecieverInfo;
	}

	public RMIRemoteInfo getResultCollectorInfo() {
		return resultCollectorInfo;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat(".###");
		return "Executer: " + name + 
			   "     [ "+"Address:"  + resultCollectorInfo.ip + "   " +
			   			 "IRPort = " + itemRecieverInfo.Port() + "   " +
			   			 "RCPort = " + resultCollectorInfo.Port() + "   " +
			   			 "Ver:" + version + "  EP:"+df.format(EP) +"  BS:"+BS +"  BC:"+BC +" ]";
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
