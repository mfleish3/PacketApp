package Packet;

import java.math.BigInteger;

public class Packet {

	private String type;
	private String req;
	private String resp;
	private BigInteger seq;
	private BigInteger ack;
	private String source;
	private String destination;
	private int size;
	private String ipSource;
	private String ipDest;
	private int connection;
	private Long timestamp;
	private int number;
	private boolean ret;
	private String seqString;
	private Long mss;
	
	public Packet() {
		this.type = "";
		this.req = "";
		this.resp = "";
		this.source = "";
		this.destination = "";
		this.ipSource = "";
		this.ipDest = "";
		this.connection = 0;
		this.timestamp = 0L;
		this.number = 0;
		this.ret = false;
		this.seqString = "";
		this.mss = 0L;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setMss(Long mss) {
		this.mss = mss;
	}
	
	public Long getMss() {
		return this.mss;
	}
	
	public void setSeqString(String ack) {
		this.seqString = ack;
	}
	
	public String getSeqString() {
		return this.seqString;
	}
	
	public void setRet(boolean ret) {
		this.ret = ret;
	}
	
	public boolean getRet() {
		return this.ret;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	public int getNumber() {
		return this.number;
	}
	
	public void setTime(Long time) {
		this.timestamp = time;
	}
	
	public Long getTIme() {
		return this.timestamp;
	}
	
	public void setConnection(int conn) {
		this.connection = conn;
	}
	
	public int getConnection() {
		return this.connection;
	}
	
	public void setIpSource(String ips) {
		this.ipSource = ips;
	}
	
	public void setIpDest(String ipd) {
		this.ipDest = ipd;
	}
	
	public void setDestination(String dest) {
		this.destination = dest;
	}
	
	public void setResponse(String resp) {
		this.resp = resp;
	}
	
	public void setRequest(String req) {
		this.req = req;
	}
	
	public void setSeq(BigInteger bigInteger) {
		this.seq = bigInteger;
	}
	
	public void setAck(BigInteger bigInteger) {
		this.ack = bigInteger;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getResponse() {
		return this.resp;
	}
	
	public String getRequest() {
		return this.req;
	}
	
	public BigInteger getSeq() {
		return this.seq;
	}
	
	public BigInteger getAck() {
		return this.ack;
	}
	
	public String getSource() {
		return this.source;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public String getDestination() {
		return this.destination;
	}
	
	public String getIpSource() {
		return this.ipSource;
	}
	
	public String getIpDest() {
		return this.ipDest;
	}
}
