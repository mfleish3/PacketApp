package Display;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.util.PcapPacketArrayList;

public class PacketHandler {
	
	private static PacketHandler inst = null;
	
	/**
	 * PacketHandler constructor
	 */
	public PacketHandler() {
	
	}
	
	/**
	 * Singleton to maintain a static reference to one instance of PacketHandler
	 * @return instance of PacketHandler
	 */
	public static PacketHandler getInstance() {
		if(inst == null) {
			inst = new PacketHandler();
		}
		return inst;
	}
	
	/**
	 * Open trace via pcap and analyze each packet
	 * @param trace
	 */
	public void checkPackets(String trace) {
		
		StringBuilder errbuf = new StringBuilder();
		Pcap pcap = Pcap.openOffline(trace, errbuf);
		
		PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new PcapPacketHandler<PcapPacketArrayList>() {
			
			public void nextPacket(PcapPacket packet, PcapPacketArrayList PaketsList) {
				
			}
		};
	}
}
