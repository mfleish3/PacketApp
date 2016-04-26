package Packet;

import java.math.BigInteger;
import java.util.ArrayList;

import org.jnetpcap.*;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.*;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Http.Request;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.util.PcapPacketArrayList;

import Objects.Packet;

public class Analyze {
	
	private static ArrayList<Connection> connections = new ArrayList<Connection>();
	
	public static ArrayList<Packet> start(String trace, String option) {
		
		String clientSource = "";
		String serverSource = "";
		ArrayList<Integer> httpList = new ArrayList<Integer>();
		ArrayList<Integer> udpList = new ArrayList<Integer>();
		ArrayList<Integer> icmpList = new ArrayList<Integer>();
		ArrayList<Integer> tcpList = new ArrayList<Integer>();
		
		ArrayList<Packet> client = new ArrayList<Packet>();
		ArrayList<Packet> server = new ArrayList<Packet>();
		
		ArrayList<Integer> listTcp = new ArrayList<Integer>();
		ArrayList<Integer> listEth = new ArrayList<Integer>();
		ArrayList<Integer> listIp4 = new ArrayList<Integer>();
		
		StringBuilder errbuf = new StringBuilder();
		Pcap pcap = Pcap.openOffline(trace, errbuf);
		
		PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new PcapPacketHandler<PcapPacketArrayList>() {
			
			int noHeaderCounter = 0;
			int index = 0;
			
			public void nextPacket(PcapPacket packet, PcapPacketArrayList PaketsList) {
				
				Tcp tcp = new Tcp();
				Ethernet eth = new Ethernet();
				Ip4 ip4 = new Ip4();
				Udp udp = new Udp();
				Http http = new Http();
				Icmp icmp = new Icmp();
				
				if (packet.hasHeader(icmp)) {
					packet.getHeader(icmp);
					listTcp.add(icmp.getLength());
					icmpList.add(index);
				}
				
				if (packet.hasHeader(http)) {
					packet.getHeader(http);
					if (http.fieldValue(Request.RequestMethod) != null) {
						httpList.add(index);
					}
				}
				
				if (packet.hasHeader(tcp)) {
					packet.getHeader(tcp);
					listTcp.add(tcp.getLength());
					tcpList.add(index);
				} else if (packet.hasHeader(udp)) { 
					packet.getHeader(udp);
					listTcp.add(udp.getLength());
					udpList.add(index);
				} else {
					noHeaderCounter++;
//					System.out.println("No Header: " + noHeaderCounter + " Size: " + listTcp.size());
				}
				
				if (packet.hasHeader(eth)) {  
			         packet.getHeader(eth);
			         listEth.add(eth.getLength());
			    } else {
			    	noHeaderCounter++;
//			    	System.out.println("No Header: " + noHeaderCounter + " Size: " + listEth.size());
			    }
				
				if (packet.hasHeader(ip4)) {
					packet.getHeader(ip4);
					listIp4.add(ip4.getLength());
				} else if ((packet.hasHeader(tcp)||packet.hasHeader(udp))&&(packet.hasHeader(eth))) {
					listIp4.add(0);
				} else {
					noHeaderCounter++;
//					System.out.println("No Header: " + noHeaderCounter + " Size: " + listIp4.size());
				}
				
				PaketsList.add(packet);
				index++;
			}
		};
		
		PcapPacketArrayList packets = new PcapPacketArrayList();
		pcap.loop(-1,jpacketHandler,packets);
		
		int counter = 0;
		ArrayList<Packet> packetList= new ArrayList<Packet>();
		
		boolean init = true;
		Long timestamp = null;
		BigInteger currentSeq = null;
		BigInteger currentAck = null;
		int conn = 0;
		boolean print;
		
		if (option.contains("a")) {
			System.out.println("Part A #1");
			System.out.println();
		}
		
		for (PcapPacket p : packets) {
			print = false;
			ByteHandler.initialize(p, listTcp, listEth, listIp4, counter);
			JBuffer buffer = ByteHandler.getBuffer();
			
			counter++;
			if (option.contains("a")) {
				System.out.println("Packet #" + counter);
			}
			Packet packet = new Packet();
			currentSeq = new BigInteger(ByteHandler.getSeqNumber(), 16 );
			packet.setSeq(currentSeq);
			packet.setNumber(counter);
			
			if ((tcpList.contains(counter-1) && !(httpList.contains(counter-1)))) {
				
				//Check packet
				if (buffer.getUByte(47) == 2) { 			//02 - SYN
					if (option.contains("a")) {
						packet.setType("SYN");
						System.out.println("Type: SYN");
					}
					packet.setSource(Constants.SYN);
					clientSource = ByteHandler.getSourceIP();
					timestamp = ByteHandler.getTimestamp(Constants.SYN);
					packet.setTime(timestamp);
					//Set connection number for packet
					packet.setConnection(conn);
					Connection c = new Connection();
					c.setNum(conn);
					c.setBytes(ByteHandler.getSize());
					//Add new connection
					connections.add(c);
					conn++;
					print = true;
				} else if (buffer.getUByte(47) == 18) { 	//12 - SYN, ACK
					if (option.contains("a")) {
						packet.setType("SYN, ACK");
						System.out.println("Type: SYN, ACK");
					}
					packet.setSource(Constants.SYN_ACK);
					currentAck = new BigInteger(ByteHandler.getAckNumber(), 16 );
					serverSource = ByteHandler.getSourceIP();
					timestamp = ByteHandler.getTimestamp(Constants.SYN_ACK);
					packet.setTime(timestamp);
					Packet pack;
					//Loop through client and find previous request
					for (int i = 0; i < client.size(); i++) {
						pack = client.get(i);
						BigInteger one = new BigInteger("1");
						if (pack.getSeq().equals(currentAck.subtract(one))) {
							//Set correct connection
							packet.setConnection(pack.getConnection());
							int index =  pack.getConnection();
							//Add bytes to total
							connections.get(index).setBytes(connections.get(index).getBytes() + ByteHandler.getSize());
							print = true;
							break;
						}
					}
					
					
				} else if (buffer.getUByte(47) == 16) {		//10 - ACK
					if (option.contains("a")) {
						packet.setType("ACK");
						System.out.println("Type: ACK");
					}
					packet.setSource(Constants.ACK);
					currentAck = new BigInteger(ByteHandler.getAckNumber(), 16 );
					timestamp = ByteHandler.getTimestamp(Constants.ACK);
					packet.setTime(timestamp);
					print = true;
				} else if (buffer.getUByte(47) == 24) { 	//18 - PSH, ACK
					if (option.contains("a")) {
						packet.setType("PSH, ACK");
						System.out.println("Type: PSH, ACK");
					}
					currentAck = new BigInteger(ByteHandler.getAckNumber(), 16 );
					timestamp = ByteHandler.getTimestamp(Constants.PSH_ACK);
					packet.setTime(timestamp);
					print = true;
				} else if (buffer.getUByte(47) == 17) {		//11 - FIN, ACK
					if (option.contains("a")) {	
						packet.setType("FIN, ACK");
						System.out.println("Type: FIN, ACK");
					}
					currentAck = new BigInteger(ByteHandler.getAckNumber(), 16 );
					timestamp = ByteHandler.getTimestamp(Constants.FIN_ACK);
					packet.setTime(timestamp);
					print = true;
				} else if (buffer.getUByte(47) == 4) {		//4 - RST
					if (option.contains("a")) {
						packet.setType("RST");
						System.out.println("Type: RST");
					}
					break;
				} else {
					
				}
				
				Packet pack;
				//Check if it is client or server
				if (ByteHandler.getSourceIP().equals(clientSource)) {
					
					//See if sequence number equals opposites ack number
					for (int i = 0; i < server.size(); i++) {
						pack = server.get(i);
						if (packet.getSeq().equals(pack.getAck())) {
//							System.out.println("Connection: " + pack.getConnection() + " " + packet.getNumber() + " and " + pack.getNumber());
							packet.setConnection(pack.getConnection());
							print = true;
							break;
						}
					}
					
				} else if (ByteHandler.getSourceIP().equals(serverSource)) {

					//See if sequence number equals opposites ack number
					for (int i = 0; i < client.size(); i++) {
						pack = client.get(i);
						if (packet.getSeq().equals(pack.getAck())) {
//							System.out.println("Connection: " + pack.getConnection() + " " + packet.getNumber() + " and " + pack.getNumber());
							packet.setConnection(pack.getConnection());
							if (counter == 58) {
								packet.setRet(true);
								print = true;
							}
							break;
						}
					}
					
				} else {
					if (option.contains("a")) {
						System.out.println("Not part of this TCP connection");
						System.out.println();
						continue;
					}
				}
				
				packet.setSeq(new BigInteger(ByteHandler.getSeqNumber(), 16 ));
				packet.setAck(new BigInteger(ByteHandler.getAckNumber(), 16 ));
				int size = (ByteHandler.getSize());
				packet.setSize(size);
				packet.setDestination(ByteHandler.getDestination());
				packet.setIpDest(ByteHandler.getDestIP());
				packet.setIpSource(ByteHandler.getSourceIP());
				packet.setSource(ByteHandler.getSource());
				if (!option.contains("a")) {
					packet.setMss(ByteHandler.getMss());
				}
				
				if (print) {
					packet.setAck(new BigInteger(ByteHandler.getAckNumber(), 16 ));
					packet.setDestination(ByteHandler.getDestination());
					packet.setIpDest(ByteHandler.getDestIP());
					packet.setIpSource(ByteHandler.getSourceIP());
					packet.setSource(ByteHandler.getSource());
					if (option.contains("a")) {
						System.out.println("Source: " + ByteHandler.getSource());
						System.out.println("Destination: " + ByteHandler.getDestination());
						System.out.printf("Window Size: %d", ByteHandler.getWindowSize());
						System.out.printf("\nSeq: %s (%d)\n", ByteHandler.getSeqNumber(), new BigInteger(ByteHandler.getSeqNumber(), 16 ));
						System.out.printf("Ack: %s (%d)\n", ByteHandler.getAckNumber(), new BigInteger(ByteHandler.getAckNumber(), 16 ));
						System.out.println();
					}
					//Add to correct client/server ArrayList
					if (packet.getIpSource().equals(clientSource)) {
//						System.out.println("Adding to client for index: " + (counter) + " and Connection: " + packet.getConnection());
						client.add(packet);
					} else if (packet.getIpSource().equals(serverSource)) {
//						System.out.println("Adding to server for index: " + (counter) + " and Connection: " + packet.getConnection());
						server.add(packet);
					} else {
						
					}
					try {
						//Add packet to correct connection
						connections.get(packet.getConnection()).getPacketList().add(packet);
					} catch (IndexOutOfBoundsException ioobe) {
						System.out.println("[Analyze] Out of bounds for connection ArrayList");
					}
				}
			} else if ((httpList.contains(counter-1))) { 
				String httpType = ByteHandler.httpCheck();
				if (httpType.contains("GET")) {
					if (option.contains("a")) {
						System.out.println("HTTP GET");
					}
				}
				if (httpType.contains("OK")) {
					if (option.contains("a")) {
						System.out.println("HTTP OK");
					}
				}
				if (option.contains("a")) {
					System.out.println();
				}
			} else {
				if (option.contains("a")) {
					System.out.println("ICMP");
					System.out.println();
				}
			}
			
			packetList.add(packet);
			
		}
				
		return packetList;
	}
	
	public static ArrayList<Connection> getConnections() {
		return connections;
	}
}