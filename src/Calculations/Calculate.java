package Calculations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import Objects.Packet;
import Objects.PacketQuic;
import Objects.PacketSpdy;
import Objects.Results;

public class Calculate {
	
	/**
	 * HTTP Calculate average rtt, latency, and throughput
	 * @param packet (HTTP)
	 * @return HTTP results
	 */
	public static Results startHttp(ArrayList<Packet> packets) {
		
		Results results = new Results();
		results.setTotalPackets(packets.size());
		results = calcAverageRttHttp(packets, results);
		results = calcLatencyHttp(packets, results);
		results = calcThroughputHttp(packets, results);
		return results;
	}
	
	/**
	 * SPDY Calculate average rtt, latency, and throughput
	 * @param packet (SPDY)
	 * @return SPDY results
	 */
	public static Results startSpdy(ArrayList<PacketSpdy> packets) {
		
		Results results = new Results();
		results.setTotalPackets(packets.size());
		results = calcAverageRttSpdy(packets, results);
		results = calcLatencySpdy(packets, results);
		results = calcThroughputSpdy(packets, results);
		return results;
	}
	
	/**
	 * QUIC Calculate average rtt, latency, and throughput
	 * @param packet (QUIC)
	 * @return QUIC results
	 */
	public static Results startQuic(ArrayList<PacketQuic> packets) {
		
		Results results = new Results();
		results.setTotalPackets(packets.size());
		results = calcAverageRttQuic(packets, results);
		results = calcLatencyQuic(packets, results);
		results = calcThroughputQuic(packets, results);
		return results;
	}
	
	/**
	 * HTTP Calculate the throughput
	 * @param packets
	 * @param results
	 * @return results
	 */
	public static Results calcThroughputHttp(ArrayList<Packet> packets, Results results) {
		
		int allBytes = 0;
		for (int i = 0; i < packets.size(); i++) {
			allBytes = allBytes + packets.get(i).getSize();
		}
		results.setThroughput(roundToNearestHundreth(allBytes / results.getLatency()));
		results.setTotalBytes(allBytes);
		return results;
	}
	
	/**
	 * HTTP Calculate the average RTT
	 * @param packet
	 * @param results
	 * @return results
	 */
	public static Results calcAverageRttHttp(ArrayList<Packet> packets, Results results) {
		ArrayList<String> destIps = new ArrayList<String>();
		//Get first source IP
		String sourceIp = packets.get(0).getIpSource();
		//Get first destination IP
		destIps.add(packets.get(0).getIpDest());
		//Get all destination IPs
		String destIp = "";
		boolean addIp = true;
		for (int i = 0; i < packets.size(); i++) {
			destIp = packets.get(i).getIpDest();
			addIp = true;
			//Only check dest IP if it's not the source IP
			if (!(destIp.contains(sourceIp))) {
				//Loop through destination IPs and only add new destIp if it is not already in the ArrayList
				for (int j = 0; j < destIps.size(); j++) {
					if (destIps.get(j).contains(destIp)) {
						addIp = false;
					}
				}
			} else {
				addIp = false;
			}
			if (addIp) {
				destIps.add(destIp);
			}
		}
		//Loop through each destination IP
		for (String ip : destIps) {
			Packet previousPacket = new Packet();
			//Loop through packet ArrayList and get the time difference between each server timestamp
			for (Packet p : packets) {
				if (p.getIpDest().contains(ip)) {
					if (previousPacket.getIpDest() != "") {
						p.setRtt(p.getTimestamp() - previousPacket.getTimestamp());
						previousPacket = p;
					} else {
						previousPacket = p;
					}
				}
			}
		}
		//Get all of the RTTs and find the average
		Long size = 0L;
		Long total = 0L;
		for (Packet p : packets) {
			if (p.getRtt() > 0) {
				total = total + p.getRtt();
				size++;
			}
		}
		Long averageRtt = total / size;
		results.setRtt(averageRtt);
		results.setNumberOfConnections(destIps.size());
		return results;
	}
	
	/**
	 * HTTP Calculate the latency
	 * @param packet
	 * @param results
	 * @return results
	 */
	public static Results calcLatencyHttp(ArrayList<Packet> packet, Results results) {
		double allTime = 0;
		//Get first packet
		double start = packet.get(0).getTimestamp();
		//Get last packet
		double end = packet.get(packet.size()-1).getTimestamp();
		allTime = end - start;
		results.setLatency(allTime);
		return results;
	}
	
	/**
	 * SPDY Calculate the throughput
	 * @param packets
	 * @param results
	 * @return results
	 */
	public static Results calcThroughputSpdy(ArrayList<PacketSpdy> packets, Results results) {
		
		int allBytes = 0;
		for (int i = 0; i < packets.size(); i++) {
			allBytes = allBytes + packets.get(i).getSize();
		}
		results.setThroughput(roundToNearestHundreth(allBytes / results.getLatency()));
		results.setTotalBytes(allBytes);
		return results;
	}
	
	/**
	 * SPDY Calculate the average RTT
	 * @param packet
	 * @param results
	 * @return results
	 */
	public static Results calcAverageRttSpdy(ArrayList<PacketSpdy> packets, Results results) {
		ArrayList<String> destIps = new ArrayList<String>();
		//Get first source IP
		String sourceIp = packets.get(0).getIpSource();
		//Get first destination IP
		destIps.add(packets.get(0).getIpDest());
		//Get all destination IPs
		String destIp = "";
		boolean addIp = true;
		for (int i = 0; i < packets.size(); i++) {
			destIp = packets.get(i).getIpDest();
			addIp = true;
			//Only check dest IP if it's not the source IP
			if (!(destIp.contains(sourceIp))) {
				//Loop through destination IPs and only add new destIp if it is not already in the ArrayList
				for (int j = 0; j < destIps.size(); j++) {
					if (destIps.get(j).contains(destIp)) {
						addIp = false;
					}
				}
			} else {
				addIp = false;
			}
			if (addIp) {
				destIps.add(destIp);
			}
		}
		//Loop through each destination IP
		for (String ip : destIps) {
			PacketSpdy previousPacket = new PacketSpdy();
			//Loop through packet ArrayList and get the time difference between each server timestamp
			for (PacketSpdy p : packets) {
				if (p.getIpDest().contains(ip)) {
					if (previousPacket.getIpDest() != "") {
						p.setRtt(p.getTimestamp() - previousPacket.getTimestamp());
						previousPacket = p;
					} else {
						previousPacket = p;
					}
				}
			}
		}
		//Get all of the RTTs and find the average
		Long size = 0L;
		Long total = 0L;
		for (PacketSpdy p : packets) {
			if (p.getRtt() > 0) {
				total = total + p.getRtt();
				size++;
			}
		}
		Long averageRtt = total / size;
		results.setRtt(averageRtt);
		results.setNumberOfConnections(destIps.size());
		return results;
	}
	
	/**
	 * SPDY Calculate the latency
	 * @param packet
	 * @param results
	 * @return results
	 */
	public static Results calcLatencySpdy(ArrayList<PacketSpdy> packet, Results results) {
		double allTime = 0;
		//Get first packet
		double start = packet.get(0).getTimestamp();
		//Get last packet
		double end = packet.get(packet.size()-1).getTimestamp();
		allTime = end - start;
		results.setLatency(allTime);
		return results;
	}
	
	/**
	 * QUIC Calculate the throughput
	 * @param packets
	 * @param results
	 * @return results
	 */
	public static Results calcThroughputQuic(ArrayList<PacketQuic> packets, Results results) {
		
		int allBytes = 0;
		for (int i = 0; i < packets.size(); i++) {
			allBytes = allBytes + packets.get(i).getSize();
		}
		results.setThroughput(roundToNearestHundreth(allBytes / results.getLatency()));
		results.setTotalBytes(allBytes);
		return results;
	}
	
	/**
	 * QUIC Calculate the average RTT
	 * @param packet
	 * @param results
	 * @return results
	 */
	public static Results calcAverageRttQuic(ArrayList<PacketQuic> packets, Results results) {
		ArrayList<String> destIps = new ArrayList<String>();
		//Get first source IP
		String sourceIp = packets.get(0).getIpSource();
		//Get first destination IP
		destIps.add(packets.get(0).getIpDest());
		//Get all destination IPs
		String destIp = "";
		boolean addIp = true;
		for (int i = 0; i < packets.size(); i++) {
			destIp = packets.get(i).getIpDest();
			addIp = true;
			//Only check dest IP if it's not the source IP
			if (!(destIp.contains(sourceIp))) {
				//Loop through destination IPs and only add new destIp if it is not already in the ArrayList
				for (int j = 0; j < destIps.size(); j++) {
					if (destIps.get(j).contains(destIp)) {
						addIp = false;
					}
				}
			} else {
				addIp = false;
			}
			if (addIp) {
				destIps.add(destIp);
			}
		}
		//Loop through each destination IP
		for (String ip : destIps) {
			PacketQuic previousPacket = new PacketQuic();
			//Loop through packet ArrayList and get the time difference between each server timestamp
			for (PacketQuic p : packets) {
				if (p.getIpDest().contains(ip)) {
					if (previousPacket.getIpDest() != "") {
						p.setRtt(p.getTimestamp() - previousPacket.getTimestamp());
						previousPacket = p;
					} else {
						previousPacket = p;
					}
				}
			}
		}
		//Get all of the RTTs and find the average
		Long size = 0L;
		Long total = 0L;
		for (PacketQuic p : packets) {
			if (p.getRtt() > 0) {
				total = total + p.getRtt();
				size++;
			}
		}
		Long averageRtt = total / size;
		results.setRtt(averageRtt);
		results.setNumberOfConnections(destIps.size());
		return results;
	}
	
	/**
	 * QUIC Calculate the latency
	 * @param packet
	 * @param results
	 * @return results
	 */
	public static Results calcLatencyQuic(ArrayList<PacketQuic> packet, Results results) {
		double allTime = 0;
		//Get first packet
		double start = packet.get(0).getTimestamp();
		//Get last packet
		double end = packet.get(packet.size()-1).getTimestamp();
		allTime = end - start;
		results.setLatency(allTime);
		return results;
	}
	
	/**
	 * Round a given double to the nearest 100th
	 * @param d
	 * @return rounded double
	 */
	public static double roundToNearestHundreth(double d) {
		return Math.round(d * 100.0) / 100.0;
	}
}
