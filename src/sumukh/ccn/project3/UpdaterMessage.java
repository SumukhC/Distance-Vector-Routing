package sumukh.ccn.project3;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UpdaterMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2512820880904472717L;
	private String senderName;
	private int senderPortNo;
	private double costToSender;
	private Map<String, Station> sendersStationInfo;
	private Map<String, Station> networkInfo;
	// private int noOfNodesInNetwork;

	public UpdaterMessage(String argSenderName, int argSenderAddress, double argCostToSender,
			Map<String, Station> argSenderNeighbors, Map<String, Station> argNetworkInfo) {
		this.senderName = argSenderName;
		this.senderPortNo = argSenderAddress;
		this.costToSender = argCostToSender;
		this.sendersStationInfo = new HashMap<>(argSenderNeighbors);
		this.networkInfo = new HashMap<>(argNetworkInfo);
	}

	public UpdaterMessage() {
		super();
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public int getSenderAddress() {
		return senderPortNo;
	}

	public void setSenderAddress(int senderAddress) {
		this.senderPortNo = senderAddress;
	}

	public Map<String, Station> getSenderNeighbors() {
		return sendersStationInfo;
	}

	public void setSenderNeighbors(HashMap<String, Station> sendersStationInfo) {
		this.sendersStationInfo = sendersStationInfo;
	}

	public Map<String, Station> getNetworkInfo() {
		return networkInfo;
	}

	public void setNetworkInfo(Map<String, Station> networkInfo) {
		this.networkInfo = networkInfo;
	}

	public double getCostToSender() {
		return costToSender;
	}

	public void setCostToSender(double costToSender) {
		this.costToSender = costToSender;
	}

}
