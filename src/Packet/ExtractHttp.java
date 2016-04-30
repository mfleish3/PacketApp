package Packet;

import java.util.ArrayList;

import org.jnetpcap.*;
import org.jnetpcap.packet.*;
import org.jnetpcap.util.PcapPacketArrayList;

import Objects.Packet;

public class ExtractHttp {
	
	public static ArrayList<Packet> start(String trace, String option) {
		
		StringBuilder errbuf = new StringBuilder();
		Pcap pcap = Pcap.openOffline(trace, errbuf);
		
		PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new PcapPacketHandler<PcapPacketArrayList>() {
			
			public void nextPacket(PcapPacket packet, PcapPacketArrayList PaketsList) {
				
				PaketsList.add(packet);
			}
		};
		
		PcapPacketArrayList packets = new PcapPacketArrayList();
		pcap.loop(-1,jpacketHandler,packets);
		
		ArrayList<Packet> packetList= new ArrayList<Packet>();
		
		for (PcapPacket p : packets) {
			
			ByteHandler.setBuffer(p);
			Packet packet = new Packet();
			
			//Set IP Source and Destination
			packet.setIpSource(ByteHandler.getSpdyIpSource()); //26-29 (Same indices for HTTP)
			packet.setIpDest(ByteHandler.getSpdyIpDest()); //30-33 (Same indices for HTTP)
			
			//Set timestamp
			packet.setTimestamp(p.getCaptureHeader().timestampInMillis());
			
			//Set size
			packet.setSize(p.getTotalSize());
			
			//Set HTTP packet info	
			packetList.add(packet);
		}
		return packetList;
	}
}