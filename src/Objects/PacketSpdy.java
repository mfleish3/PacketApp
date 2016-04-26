package Objects;

import java.util.ArrayList;

public class PacketSpdy {

	private int contentType;
	private String version;
	private int length;
	private ArrayList<String> streams;
	
	public PacketSpdy () {
		this.contentType = 0;
		this.version = "";
		this.length = 0;
		this.streams = new ArrayList<String>();
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
}
