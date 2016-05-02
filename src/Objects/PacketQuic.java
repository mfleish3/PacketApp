package Objects;

import java.util.ArrayList;

public class PacketQuic {

	private String ipSource;
	private String ipDest;
	private int contentType;
	private int length;
	private ArrayList<String> streams;
	private Long timestamp;
	private Long rtt;
	private int size;
	private String version;
	private String reset;
	private String cid;
	private String seq;
	private String reserved;
	
	public PacketQuic () {
		this.ipSource = "";
		this.ipDest = "";
		this.contentType = 0;
		this.version = "";
		this.length = 0;
		this.streams = new ArrayList<String>();
		this.timestamp = 0L;
		this.rtt = 0L;
		this.size = 0;
		this.version = "";
		this.reset = "";
		this.cid = "";
		this.seq = "";
		this.reserved = "";
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
	
	public String getVersion() {
		return this.version;
	}
	
	public String getReset() {
		return this.reset;
	}
	
	public String getCid() {
		return this.cid;
	}
	
	public String getSeq() {
		return this.seq;
	}
	
	public String getReserved() {
		return this.reserved;
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
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void setReset(String reset) {
		this.reset = reset;
	}
	
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	public void setSeq(String seq) {
		this.seq = seq;
	}
	
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
}
