package Packet;

import java.util.ArrayList;

public class Connection {

	private int num;
	private int bytes;
	private ArrayList<Packet> packet;
	
	public Connection() {
		this.num = 0;
		this.bytes = 0;
		this.packet = new ArrayList<Packet>();
	}
	
	public void setPacketList(ArrayList<Packet> packet) {
		this.packet = packet;
	}
	
	public ArrayList<Packet> getPacketList() {
		return this.packet;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
	
	public int getNum() {
		return this.num;
	}
	
	public void setBytes(int bytes) {
		this.bytes = bytes;
	}
	
	public int getBytes() {
		return this.bytes;
	}
}
