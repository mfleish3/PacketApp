package Calculations;

import java.util.ArrayList;
import Objects.Packet;
import Objects.PacketSpdy;
import Objects.Results;

public class Calculate {

	static int totalBytes = 0;
	static double allTime = 0;
	
	/**
	 * HTTP Calculate average rtt, latency, and throughput
	 * @param packet (HTTP)
	 * @return HTTP results
	 */
	public static Results startHttp(ArrayList<Packet> packet) {
		
		Results results = new Results();
		results = calcAverageRttHttp(packet, results);
		results = calcLatencyHttp(packet, results);
		results = calcThroughputHttp(packet, results);
		
		return results;
	}
	
	/**
	 * SPDY Calculate average rtt, latency, and throughput
	 * @param packet (SPDY)
	 * @return SPDY results
	 */
	public static Results startSpdy(ArrayList<PacketSpdy> packet) {
		
		Results results = new Results();
		results = calcAverageRttSpdy(packet, results);
		results = calcLatencySpdy(packet, results);
		results = calcThroughputSpdy(packet, results);
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
	public static Results calcAverageRttHttp(ArrayList<Packet> packet, Results results) {
		//Loop through packet ArrayList and get the time difference between each server timestamp
		for (int i = 1; i < packet.size(); i++) {
			packet.get(i).setRtt(packet.get(i).getTimestamp() - packet.get(i-1).getTimestamp());
		}
		Long total = 0L;
		for (int i = 1; i < packet.size(); i++) {
			total = total + packet.get(i).getRtt();
		}
		Long averageRtt = total / (packet.size()-1);
		results.setRtt(averageRtt);
		return results;
	}
	
	/**
	 * HTTP Calculate the latency
	 * @param packet
	 * @param results
	 * @return results
	 */
	public static Results calcLatencyHttp(ArrayList<Packet> packet, Results results) {
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
	public static Results calcAverageRttSpdy(ArrayList<PacketSpdy> packet, Results results) {
		//Loop through packet ArrayList and get the time difference between each server timestamp
		for (int i = 1; i < packet.size(); i++) {
			packet.get(i).setRtt(packet.get(i).getTimestamp() - packet.get(i-1).getTimestamp());
		}
		Long total = 0L;
		for (int i = 1; i < packet.size(); i++) {
			total = total + packet.get(i).getRtt();
		}
		Long averageRtt = total / (packet.size()-1);
		results.setRtt(averageRtt);
		return results;
	}
	
	/**
	 * SPDY Calculate the latency
	 * @param packet
	 * @param results
	 * @return results
	 */
	public static Results calcLatencySpdy(ArrayList<PacketSpdy> packet, Results results) {
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
