package Packet;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;

import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.PcapPacket;

public class ByteHandler {

	private static JBuffer buffer;
	private static int size = 0;
	private static int lengthHeader = 0;
	private static int length = 0;
	
	public static void initialize(PcapPacket p, ArrayList<Integer> listTcp, ArrayList<Integer> listEth, ArrayList<Integer> listIp4, int counter) {
		setBuffer(p);
		setSize(p.size());
		setHeaderLength(listEth.get(counter) + listIp4.get(counter) + listTcp.get(counter));
		setLength(getSize() - getHeaderLength());
	}
	
	public static void setBuffer(PcapPacket p) {
		buffer = p;
	}
	
	public static JBuffer getBuffer() {
		return buffer;
	}
	
	public static void setSize(int s) {
		size = s;
	}
	
	public static int getSize() {
		return size;
	}
	
	public static void setHeaderLength(int lh) {
		lengthHeader = lh;
	}
	
	public static int getHeaderLength() {
		return lengthHeader;
	}
	
	public static void setLength(int l) {
		length = l;
	}
	
	public static int getLength() {
		return length;
	}
	
	public static String getDestination() {
		
		String hexString = "";
		//System.out.printf("Destination: ");
		for (int i = 0; i < 6; i++) {
			if (buffer.getUByte(i) > 15) {
				//System.out.printf("%h ", buffer.getUByte(i));
				hexString = hexString + " " + Integer.toHexString(buffer.getUByte(i));
			} else {
				//System.out.printf("0%h ", buffer.getUByte(i));
				hexString = hexString + " 0" + Integer.toHexString(buffer.getUByte(i));
			}
		}
		return hexString;
	}
	
	public static String getSource() {
		
		String hexString = "";
		//System.out.printf("Source: ");
		for (int i = 6; i < 12; i++) {
			if (buffer.getUByte(i) > 15) {
				//System.out.printf("%h ", buffer.getUByte(i));
				hexString = hexString + " " + Integer.toHexString(buffer.getUByte(i));
				
			} else {
				//System.out.printf("0%h ", buffer.getUByte(i));
				hexString = hexString + " 0" + Integer.toHexString(buffer.getUByte(i));
			}
		}
		return hexString;
	}
	
	public static String getSourceIP() {
		
		String hexString = "";
		//System.out.printf("Source: ");
		for (int i = 26; i < 30; i++) {
			if (buffer.getUByte(i) > 15) {
				//System.out.printf("%h ", buffer.getUByte(i));
				hexString = hexString + " " + Integer.toHexString(buffer.getUByte(i));
				
			} else {
				//System.out.printf("0%h ", buffer.getUByte(i));
				hexString = hexString + " 0" + Integer.toHexString(buffer.getUByte(i));
			}
		}
		return hexString;
	}
	
	public static String getDestIP() {
		
		String hexString = "";
		//System.out.printf("Source: ");
		for (int i = 30; i < 34; i++) {
			if (buffer.getUByte(i) > 15) {
				//System.out.printf("%h ", buffer.getUByte(i));
				hexString = hexString + " " + Integer.toHexString(buffer.getUByte(i));
				
			} else {
				//System.out.printf("0%h ", buffer.getUByte(i));
				hexString = hexString + " 0" + Integer.toHexString(buffer.getUByte(i));
			}
		}
		return hexString;
	}
	
	public static String getAckNumber() {
		
		String hexString = "";
		for (int i = 42; i < 46; i++) {
			hexString = hexString + Integer.toHexString(buffer.getUByte(i));
		}
		return hexString;
	}

	public static String getSeqNumber() {
		
		String hexString = "";
		for (int i = 38; i < 42; i++) {
			hexString = hexString + Integer.toHexString(buffer.getUByte(i));
		}
		return hexString;
	}

	public static Long getWindowSize() {
		
		String hexString = "";
		for (int i = 48; i < 50; i++) {
			hexString = hexString + Integer.toHexString(buffer.getUByte(i));
		}
		return Long.parseLong(hexString, 16);
	}
	
	public static Long getMss() {
		
		String hexString = "";
		for (int i = 56; i <= 57; i++) {
			hexString = hexString + Integer.toHexString(buffer.getUByte(i));
		}
		return Long.parseLong(hexString, 16);
	}
	
	public static Long getTimestamp(String action) {
		
		int start = 0;
		int end = 0;
	
		switch (action) {
		
			case Constants.ACK:
				start = 58;
				end = 61;
				break;
			case Constants.FIN_ACK:
				start = 58;
				end = 61;
				break;
			case Constants.PSH_ACK:
				start = 58;
				end = 61;
				break;
			case Constants.SYN:
				start = 66;
				end = 69;
				break;
			case Constants.SYN_ACK:
				start = 62;
				end = 65;
				break;
			default:
				break;
		}
	
		String hexString = "";
		for (int i = start; i <= end; i++) {
			try {
				hexString = hexString + Integer.toHexString(buffer.getUByte(i));
			} catch (BufferUnderflowException bue) {
				System.out.println(bue);
			}
		}
		return Long.parseLong(hexString, 16);
	}
	
	public static String httpCheck() {
		
		String hexString = "";
		for (int i = 54; i <= 56; i++) {
			try {
				hexString = hexString + Integer.toHexString(buffer.getUByte(i));
			} catch (BufferUnderflowException bue) {
				System.out.println(bue);
			}
		}
		if (hexString.equals("474554")) {
			return "GET";
		}
		
		for (int i = 13; i <= 14; i++) {
			try {
				hexString = hexString + Integer.toHexString(buffer.getUByte(i));
			} catch (BufferUnderflowException bue) {
				System.out.println(bue);
			}
		}
		if (hexString.equals("4f4b")) {
			return "OK";
		}
		
		return "";
	}
}
