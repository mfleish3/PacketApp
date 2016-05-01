package Objects;

public class Results {

	private double rtt;
	private double throughput;
	private double latnecy;
	private int totalBytes;
	private int numberOfConnections;
	private int totalPackets;
	
	public Results() {
		this.rtt = 0;
		this.throughput = 0;
		this.latnecy = 0;
		this.totalBytes = 0;
		this.numberOfConnections = 0;
		this.totalPackets = 0;
	}
	
	public int getTotalPackets() {
		return this.totalPackets;
	}
	
	public int getNumberOfConnections() {
		return this.numberOfConnections;
	}
	
	public double getRtt() {
		return this.rtt;
	}
	
	public double getThroughput() {
		return this.throughput;
	}
	
	public double getLatency() {
		return this.latnecy;
	}
	
	public int getTotalBytes() {
		return this.totalBytes;
	}
	
	public void setTotalPackets(int totalPackets) {
		this.totalPackets = totalPackets;
	}
	
	public void setNumberOfConnections(int numberOfConnections) {
		this.numberOfConnections = numberOfConnections;
	}
	
	public void setRtt(double rtt) {
		this.rtt = rtt;
	}
	
	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}

	public void setLatency(double latency) {
		this.latnecy = latency;
	}	
	
	public void setTotalBytes(int totalBytes) {
		this.totalBytes = totalBytes;
	}
}
