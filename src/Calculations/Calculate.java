package Calculations;

import java.util.ArrayList;
import Packet.Analyze;
import Packet.Connection;
import Objects.Packet;
import Objects.PacketSpdy;
import Objects.Results;

public class Calculate {

	static int totalBytes = 0;
	static double allTime = 0;
		
	public static Results startHttp(ArrayList<Packet> packet) {
		
		ArrayList<Connection> conn = new ArrayList<Connection>();
		Results results = new Results();
		
		conn = Analyze.getConnections();
		results = calcLatency(packet, results);
		results = calcThroughput(results, conn);
		results = calcAverageRtt(results, conn);
		
		return results;
	}
	
	public static Results startSpdy(ArrayList<PacketSpdy> packet) {
		
		Results results = new Results();
		results = calcAverageRttSpdy(packet, results);
		results = calcLatencySpdy(packet, results);
		results = calcThroughputSpdy(packet, results);
		return results;
	}
	
	public static Results calcThroughputSpdy(ArrayList<PacketSpdy> packets, Results results) {
		
		int allBytes = 0;
		for (int i = 0; i < packets.size(); i++) {
			allBytes = allBytes + packets.get(i).getSize();
		}
		results.setThroughput(roundToNearestHundreth(allBytes / results.getLatency()));
		results.setTotalBytes(allBytes);
		return results;
	}
	
	public static Results calcAverageRttSpdy(ArrayList<PacketSpdy> packet, Results results) {
		//Get the server IP
		String serverIp = packet.get(0).getIpDest();
		Long previousTimestamp = packet.get(0).getTimestamp();
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
	 * Calculate the latency for the given trace / packet list
	 * @param packets
	 * @param results
	 * @return results
	 */
	public static Results calcLatency(ArrayList<Packet> packets, Results results) {
		//Get first packet
		double start = packets.get(0).getTIme();
		//Get last packet
		double end = packets.get(packets.size()-1).getTIme();
		allTime = end - start;
		results.setLatency(allTime);
		return results;
	}
	
	/**
	 * Calculate the throughput for the given trace / packet list
	 * @param results
	 * @param conn
	 * @return results
	 */
	public static Results calcThroughput(Results results, ArrayList<Connection> conn) {
		
		int allBytes = 0;
		for (int i = 0; i < conn.size(); i++) {
			totalBytes = 0;
			for (int j = 0; j < conn.get(i).getPacketList().size(); j++) {
				totalBytes = totalBytes + conn.get(i).getPacketList().get(j).getSize();
			}
			allBytes = allBytes + totalBytes;
		}
		results.setThroughput(roundToNearestHundreth(allBytes / allTime));
		results.setTotalBytes(allBytes);
		return results;
	}
	
	/**
	 * Calculate the average RTT for the given trace / packet list
	 * @param results
	 * @param conn
	 * @return results
	 */
	public static Results calcAverageRtt(Results results, ArrayList<Connection> conn) {
		
		String source = conn.get(0).getPacketList().get(0).getSource();
		
		ArrayList<Long> times = new ArrayList<Long>();
		
		ArrayList<Long> total = new ArrayList<Long>();
		
		for (int i = 0; i < conn.size(); i++) {
			
			int size = conn.get(i).getPacketList().size();
			for (int j = 0; j < size; j++) {

				if (conn.get(i).getPacketList().get(j).getSource().equals(source)) {
					times.add(conn.get(i).getPacketList().get(j).getTIme());
				}
			}
		}
		
		for (int i = 0; i < times.size()-1; i++) {
			total.add(times.get(i+1) - times.get(i));
		}
		
		double sum = 0;
		for (int i = 0; i < total.size(); i++) {
			sum = sum + total.get(i);
		}
		double avg = sum / total.size() / 10.0;;
		results.setRtt(roundToNearestHundreth(avg));
		return results;
	}
	
	public static double roundToNearestHundreth(double d) {
		return Math.round(d * 100.0) / 100.0;
	}
	
}
