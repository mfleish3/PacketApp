package Packet;

import java.math.BigInteger;
import java.util.ArrayList;

import org.jnetpcap.*;
import org.jnetpcap.packet.*;
import org.jnetpcap.util.PcapPacketArrayList;

import Objects.PacketHttp;

public class ExtractHttp {
	
	public static ArrayList<PacketHttp> start(String trace) {
		
		StringBuilder errbuf = new StringBuilder();
		Pcap pcap = Pcap.openOffline(trace, errbuf);
		
		PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new PcapPacketHandler<PcapPacketArrayList>() {
			
			public void nextPacket(PcapPacket packet, PcapPacketArrayList PaketsList) {
				
				PaketsList.add(packet);
			}
		};
		
		PcapPacketArrayList packets = new PcapPacketArrayList();
		pcap.loop(-1,jpacketHandler,packets);
		
		ArrayList<PacketHttp> packetList= new ArrayList<PacketHttp>();
		
		for (PcapPacket p : packets) {
			ByteHandler.setBuffer(p);
			PacketHttp packet = new PacketHttp();
			//Set IP Source and Destination
			packet.setIpSource(ByteHandler.getSpdyIpSource()); //26-29 (Same indices for HTTP)
			packet.setIpDest(ByteHandler.getSpdyIpDest()); //30-33 (Same indices for HTTP)
			//Set timestamp
			packet.setTimestamp(p.getCaptureHeader().timestampInMillis());
			//Set size
			packet.setSize(p.getTotalSize());
			//Set seq
			packet.setSeq(new BigInteger(ByteHandler.getSeqNumber(), 16 ));
			//Set ack
			packet.setAck(new BigInteger(ByteHandler.getAckNumber(), 16 ));
			//Set mss
			packet.setMss(ByteHandler.getMss());
			//Set HTTP packet info	
			packetList.add(packet);
		}
		return packetList;
	}
}