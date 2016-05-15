package Packet;

import java.math.BigInteger;
import java.util.ArrayList;

import org.jnetpcap.*;
import org.jnetpcap.packet.*;
import org.jnetpcap.util.PcapPacketArrayList;

import Objects.PacketHttp;
import Objects.PacketQuic;

public class ExtractQuic {
	
	public static ArrayList<PacketQuic> start(String trace) {
		
		StringBuilder errbuf = new StringBuilder();
		Pcap pcap = Pcap.openOffline(trace, errbuf);
		
		PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new PcapPacketHandler<PcapPacketArrayList>() {
			
			public void nextPacket(PcapPacket packet, PcapPacketArrayList PaketsList) {
				
				PaketsList.add(packet);
			}
		};
		
		PcapPacketArrayList packets = new PcapPacketArrayList();
		pcap.loop(-1,jpacketHandler,packets);
		
		ArrayList<PacketQuic> packetList= new ArrayList<PacketQuic>();
		
		for (PcapPacket p : packets) {
			ByteHandler.setBuffer(p);
			PacketQuic packet = new PacketQuic();
			//Set IP Source and Destination
			packet.setIpSource(ByteHandler.getSpdyIpSource()); //26-29 (Same indices for HTTP)
			packet.setIpDest(ByteHandler.getSpdyIpDest()); //30-33 (Same indices for HTTP)
			//Set timestamp
			packet.setTimestamp(p.getCaptureHeader().timestampInMillis());
			//Set size
			packet.setSize(p.getTotalSize());
			//Fix to complete flags 8 bits with padding
			String lengthBits = new BigInteger(ByteHandler.getQuicCidLength(), 16).toString(2);
			int length = 8 - lengthBits.length();
			String padding = "";
			for (int i = 0; i < length; i++) {
				padding = padding + "0"; 
			}
			lengthBits = padding + lengthBits;
			//Set version
			String bitsSubVersion = lengthBits.substring(7, 8);
			packet.setVersion(bitsSubVersion);
			//Set reset
			String bitsSubReset = lengthBits.substring(6, 7);
			packet.setReset(bitsSubReset);
			String bitsSubCid = lengthBits.substring(4, 6);
			String bitsSubSeq = lengthBits.substring(2, 4);
			//Set reserved
			String bitsSubReserved = lengthBits.substring(0, 2);
			packet.setReserved(bitsSubReserved);
			int lengthCid = (int)Math.pow(2, (Integer.parseInt(bitsSubCid, 2)));
			int lengthSeq = (int)Math.pow(2, (Integer.parseInt(bitsSubSeq, 2)));
			int lengthFlags =  lengthCid;
			if (bitsSubVersion.equals("1")) {
				lengthFlags = lengthFlags + 4;
			}
			//Set CID
			packet.setCid(ByteHandler.getQuicCid(lengthCid));
			//Set Sequence
			packet.setSeq(ByteHandler.getQuicSequence(lengthFlags, lengthSeq));
			//Set HTTP packet info	
			packetList.add(packet);
		}
		return packetList;
	}
}