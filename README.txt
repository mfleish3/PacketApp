*********************
* README
* HTTP vs. SPDY/QUIC
********************* 

1) External Libraries

JnetPcap
http://jnetpcap.com/

2) High Level View of Design

Package Calculations
	Class Calculate
		-Calculates throughput, average RTT and latency for http, spdy, and quic
Package Objects
	Class DecryptedBytes
		-Object for decrypted spdy bytes
		-Fields: length, bytes
	Class PacketHttp
		-Object for http packets
		-Fields: ipSourece, ipDest, timestamp, size, seq, ack, mss
	Class PacketQuic
		-Object for quic packets
		-Fields: ipSource, ipDest, contentType, length, streams, timestamp, rtt, size, version, reset, cid, seq, reserved
	Class PacketSpdy
		-Object for spdy packets
		-Fields: ipSource, ipDest, contentType, version, length, streams, timestamp, rtt, size
	Class Results
		-Object for results
		-Fields: rtt, throughput, latency, totalBytes, numberOfConnections, totalPackets
Package Packet
	Class ByteHandler
		-Extract segments of bytes for requested fields
	Class ExtractHttp
		-Extract packets from http pcap file
		-Set selected fields for PacketHttp object
	Class ExtractQuic
		-Extract packets from quic pcap file
		-Set selected fields for PacketSpdy object
	Class ExtractSpdy
		-Extract packets from spdy pcap file
		-Set selected fields for PacketSpdy object
Package Settings
	Class Constants
		-Contains constants for the JFrame dimensions and protocol names
Package Setup
	Class FileBrowseSave
		-Initializes panels, text fields, buttons, models
		-Handles browsing actions
		-Handles saving file actions
		-Handles populating tables for packet details
		-Handles populating tables for calculated results
		-Handles exporting results to an excel file
	Class StartProject
		-Contains the main method to start the project with an initialized JFrame

3) How to Run Programs

3a. Capturing packets via Wireshark

When saving the SPDY packet, a second text file containing the decrypted bytes must also be included. This method is as follows:

(To be done before the Wireshark capture is started)
1) Open the control panel
2) Go to System -> Advanced system settings -> Environment Variables
3) Enter a new User Variable (Variable = SSLKEYLOGFILE, Value = "PATH TO THE DESKTOP" \sslkeylog.log)
4) In Wireshark go to Edit -> Preferences -> Protocols -> SSL
5) In the (Pre)-Master-Secret log filename, browse and select the sslkeylog.log file on the desktop (Firefox may need to be opened at least once before this file is created)
6) Click OK

The method to obtain working pcap files for this application are as follows:

1) Open Firefox
2) Enter 'about:config' in the url input

(HTTP)
3) Enter 'network.http.spdy.enabled.http2'
4) Make sure value is set to False
5) Enter 'security.ssl.enable_alpn'
6) Make sure value is set to False

(SPDY/HTTP2 & QUIC)
3) Enter 'network.http.spdy.enabled.http2'
4) Make sure value is set to True
5) Enter 'security.ssl.enable_alpn'
6) Make sure value is set to True

7) Open Firefox
8) Open Wireshark
9) Start packet capture
10) Load a page/video via Firefox
11) Once the page/video is loaded stop the capture in Wireshark

(HTTP)
12) Filter the packets in Wireshark to 'http'
13) Save the selected packets as http............xxxxxx.pcap

(SPDY/HTTP2)
12) Export the decrypted packets
	a. Mark each packet (ctrl+m)
	b. File -> Export Packet Dissections -> As Plain Text
	c. Check the box that says 'Marked Packets'
	d. Save file as spdy............xxxxxx.txt
13) Filter the packets in Wireshark to 'http2'
14) Save the selected packets as spdy............xxxxxx.pcap

(QUIC)
12) Filter the packets in Wireshark to 'quic'
13) Save the selected packets as quic............xxxxxx.pcap

These are to be the selected files for Browse HTTP, Browse SPDY, Browse Decrypt, and Browse Quic respectively.

3b. Starting the application

JAR

1) Navigate the to directory where the jar file is located
2) Enter the command "java -jar <filename>.jar"

ECLIPSE

1) Open the project
2) Click 'Run' with StartProject.java as its configure main method

3c. Using the application

1) View HTTP packet details and calculated results
	a. Click 'Browse HTTP' button
	b. Select a HTTP pcap file from a Wireshark capture

2) View SPDY packet details and calculated results
	a. Click 'Browse Decrypt' button
	b. Select a decrypted text file from a Wireshark capture
	c. Click 'Browse SPDY' button
	d. Select a SPDY pcap file from a Wireshark capture
	
3) View QUIC packet details and calculated results
	a. Click 'Browse QUIC' button
	b. Select a QUIC pcap file from a Wireshark capture
	
4) Export results to excel
	a. Once all files have been initialized, click the 'Export Results' button
