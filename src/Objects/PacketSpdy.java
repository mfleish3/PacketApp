package Objects;

import java.util.ArrayList;

public class PacketSpdy {

	private String ipSource;
	private String ipDest;
	private int contentType;
	private String version;
	private int length;
	private ArrayList<String> streams;
	private Long timestamp;
	private Long rtt;
	private int size;
	
	public PacketSpdy () {
		this.ipSource = "";
		this.ipDest = "";
		this.contentType = 0;
		this.version = "";
		this.length = 0;
		this.streams = new ArrayList<String>();
		this.timestamp = 0L;
		this.rtt = 0L;
		this.size = 0;
	}
	
	public String getIpSource() {
		return this.ipSource;
	}
	
	public String getIpDest() {
		return this.ipDest;
	}
	
	public int getContentType() {
		return this.contentType;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public ArrayList<String> getStreams() {
		return this.streams;
	}
	
	public Long getTimestamp() {
		return this.timestamp;
	}
	
	public Long getRtt() {
		return this.rtt;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void setIpSource(String ipSource) {
		this.ipSource = ipSource;
	}
	
	public void setIpDest(String ipDest) {
		this.ipDest = ipDest;
	}
	
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public void setStreams(ArrayList<String> streams) {
		this.streams = streams;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setRtt(Long rtt) {
		this.rtt = rtt;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
}
