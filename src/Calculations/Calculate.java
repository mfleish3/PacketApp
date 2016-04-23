package Calculations;

import java.util.ArrayList;
import Packet.Analyze;
import Packet.Connection;
import Objects.Packet;
import Objects.Results;

public class Calculate {

	static int totalBytes = 0;
	static double allTime = 0;
		
	public static Results start(ArrayList<Packet> pack) {
		
		ArrayList<Connection> conn = new ArrayList<Connection>();
		Results results = new Results();
		
		conn = Analyze.getConnections();
		results = calcLatency(pack, results);
		results = calcThroughput(results, conn);
		results = calcAverageRtt(results, conn);
		
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
		results.setThroughput(allBytes / allTime);
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
		results.setRtt(avg);
		return results;
	}
	
}
