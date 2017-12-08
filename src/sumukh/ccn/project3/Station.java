package sumukh.ccn.project3;

import java.io.Serializable;

public class Station implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7913765297765761744L;
	/**
	 * 
	 */
	private String host;
	private String name;
	private int portNo;
	private double cost;
	private String nextHopRouter;

	public Station(String argHost, String argName, int argPortNo, double argCost, String argNextHopHost) {
		super();
		this.host = argHost;
		this.name = argName;
		this.portNo = argPortNo;
		this.cost = argCost;
		this.nextHopRouter = argNextHopHost;
	}

	public Station(String argHost, String argName, int argPortNo, double argCost) {
		this.host = argHost;
		this.name = argName;
		this.portNo = argPortNo;
		this.cost = argCost;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPortNo() {
		return portNo;
	}

	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getNextHopRouter() {
		return nextHopRouter;
	}

	public void setNextHopRouter(String nextHopRouter) {
		this.nextHopRouter = nextHopRouter;
	}

}
