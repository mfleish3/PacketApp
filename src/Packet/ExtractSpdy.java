package Packet;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.jnetpcap.*;
import org.jnetpcap.packet.*;
import org.jnetpcap.util.PcapPacketArrayList;

import Objects.DecryptedBytes;
import Objects.PacketSpdy;

public class ExtractSpdy {
	
	private static String filenameDecrypt = "";
	
	public static ArrayList<PacketSpdy> start(String trace) {
		
		StringBuilder errbuf = new StringBuilder();
		Pcap pcap = Pcap.openOffline(trace, errbuf);
		
		PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new PcapPacketHandler<PcapPacketArrayList>() {
			
			public void nextPacket(PcapPacket packet, PcapPacketArrayList PaketsList) {
				
				PaketsList.add(packet);
			}
		};
		
		PcapPacketArrayList packets = new PcapPacketArrayList();
		pcap.loop(-1,jpacketHandler,packets);
		
		int counter = 0;
		ArrayList<PacketSpdy> packetList= new ArrayList<PacketSpdy>();
		DecryptedBytes db = new DecryptedBytes();
		ArrayList<DecryptedBytes> dbList = new ArrayList<DecryptedBytes>();
		dbList = formatDecryptedBytes();
		
		for (PcapPacket p : packets) {
			
			ByteHandler.setBuffer(p);
			PacketSpdy packet = new PacketSpdy();
			
			//Set IP Source and Destination
			packet.setIpSource(ByteHandler.getSpdyIpSource()); //26-29
			packet.setIpDest(ByteHandler.getSpdyIpDest()); //30-33
			
			//Set timestamp
			packet.setTimestamp(p.getCaptureHeader().timestampInMillis());
			
			//Set size
			packet.setSize(p.getTotalSize());
			
			//Get current decrypted bytes
			db = dbList.get(counter);
			
			int start = 0;
			int end = 0;
			int typeIndex = 0;
			//Total bytes in decrypted bytes
			int totalLength = db.getLength();
			
			//Check to see if first stream is Magic
			if ( db.getBytes().get(0).contains("50") && db.getBytes().get(23).contains("0a") ) {
				packet.getStreams().add("Magic");
				//Starting at index 24 of decrypted packet
				start = 24;
				end = start + 2;
				typeIndex = start + 3;
				totalLength = totalLength - 24;
			} else {
				//Starting at index 0 of decrypted packet
				start = 0;
				end = start + 2;
				typeIndex = start + 3;
			}
			
			while (totalLength > 0) {
				
				//Get length of current stream (+ 9 for the header)
				int length = new BigInteger(ByteHandler.getStreamLength(db, start, end), 16).intValue() + 9;
				
				//Get type of current stream
				String type = db.getBytes().get(typeIndex);
				
				//Set correct stream type
				if (type.contains("04")) {
					packet.getStreams().add("Settings");
				} else if (type.contains("08")) {
					packet.getStreams().add("Window Update");
				} else if (type.contains("02")) {
					packet.getStreams().add("Priority");
				} else if (type.contains("00")) {
					packet.getStreams().add("Data");
				} else if (type.contains("06")) {
					packet.getStreams().add("Ping");
				} else if (type.contains("01")) {
					packet.getStreams().add("Headers");
				}
				
				//Reduce length remaining for current total length
				totalLength = totalLength - length;
				start = start + length;
				end = start + 2;
				typeIndex = start + 3;
			}
			
			counter++;
			
			//Set SSL packet info
			packet.setContentType(ByteHandler.getContentType());
			packet.setVersion(ByteHandler.getVersion());		
			packet.setLength((new BigInteger(ByteHandler.getSslLength(), 16 ).intValue()));
			packetList.add(packet);
		}
		return packetList;
	}
	
	public static ArrayList<DecryptedBytes> formatDecryptedBytes() {
		//Make a list of all the decrypted packets
	    ArrayList<DecryptedBytes> dbList = new ArrayList<DecryptedBytes>();
		//Get the text file with the decrypted SSL data for SPDY
		Scanner sc2 = null;
	    try {
	        sc2 = new Scanner(new File(System.getProperty("user.dir") + "/traces/" + filenameDecrypt));
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();  
	    }
	    //Get the amount of bytes in the decrypted data
	    int amt = 0;
	    boolean dec = false;
	    //Loop through each line of the file
	    while (sc2.hasNextLine()) {
	    	String line = sc2.nextLine();
	    	if (line.contains("Frame (")) {
	    		dec = true;
	    	}
	    	if (dec) {
		    	//If the line contains decrpyted data, extract the number of bytes
		    	if (line.contains("Decrypted SSL data (")) {
		    		dec = false;
		    		int bytesStart = line.indexOf("bytes");
		    		String amount = line.substring(20, bytesStart-1);
		    		amt = Integer.parseInt(amount);
		    		//Calculate how many lines need to be read
			    	int lines = amt / 16;
			    	int remainder = amt % 16;
			    	
			    	DecryptedBytes db = new DecryptedBytes();
			    	db.setLength(amt);
			    	for (int i = 0; i < lines; i++) {
			    		Scanner s2 = new Scanner(sc2.nextLine());
			    		s2.next();
			    		for (int j = 0; j < 16; j++) {
			    			String s = s2.next();
				            db.getBytes().add(s);
			    		}	
			    	}
			    	if (remainder > 0) {
			    		Scanner s2 = new Scanner(sc2.nextLine());
			    		s2.next();
		    			for (int k = 0; k < remainder; k++) {
		    				String s = s2.next();
				            db.getBytes().add(s);
		    			}
		    		}
			    	dbList.add(db);
		    	}
	    	}
	    }
	    return dbList;
	}
	
	public static void setFilenameDecrypt(String filename) {
		filenameDecrypt = filename;
	}
}