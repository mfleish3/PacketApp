package Setup;

//AWT imports
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
//IO imports
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
//Swing imports
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Calculations.Calculate;
import Objects.PacketHttp;
import Objects.PacketQuic;
import Objects.PacketSpdy;
import Objects.Results;
//Packet imports
import Packet.ExtractHttp;
import Packet.ExtractQuic;
import Packet.ExtractSpdy;
import Settings.Constants;

public class FileBrowseSave extends JFrame {
	
	private JTextField filenameHttp = new JTextField();
	private JTextField filenameSpdy = new JTextField();
	private JTextField filenameDecrypt = new JTextField();
	private JTextField filenameQuic = new JTextField();
	private JTextField pathHttp = new JTextField();
	private JTextField pathSpdy = new JTextField();
	private JTextField pathDecrypt = new JTextField();
	private JTextField pathQuic = new JTextField();
	private JButton browseHttp = new JButton("Browse HTTP");
	private JButton browseSpdy = new JButton("Browse SDPY");
	private JButton exportResults = new JButton("Export Results");
	private JButton browseDecrypt = new JButton("Browse Decrypt");
	private JButton browseQuic = new JButton("Browse QUIC");
	private File fileHttp;
	private File fileSpdy;
	private File fileDecrypt;
	private File fileQuic;
	private DefaultTableModel modelHttp = new DefaultTableModel();
	private DefaultTableModel modelSpdy = new DefaultTableModel();
	private DefaultTableModel modelDecrypt = new DefaultTableModel();
	private DefaultTableModel modelQuic = new DefaultTableModel();
	private DefaultTableModel modelResultsHttp = new DefaultTableModel();
	private DefaultTableModel modelResultsSpdy = new DefaultTableModel();
	private DefaultTableModel modelResultsQuic = new DefaultTableModel();
	private Results resultsHttp = new Results();
	private Results resultsSpdy = new Results();
	private Results resultsQuic = new Results();
	private boolean addedHttp = false;
	private boolean addedSpdy = false;
	private boolean addedQuic = false;
  
	public FileBrowseSave() {
		//Outer container for JFrame
		JPanel outerContainer = new JPanel();
		outerContainer.setLayout(new BoxLayout(outerContainer, BoxLayout.X_AXIS));
		//Container for packet file, path, table, browse
		JPanel packetContainer = new JPanel();
		packetContainer.setLayout(new BoxLayout(packetContainer, BoxLayout.Y_AXIS));
		//Container for results
		JPanel resultsContainer = new JPanel();
		resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));
		resultsContainer.setPreferredSize(new Dimension(500, 500));
		JPanel packetContainerHttp = new JPanel();
		packetContainerHttp.setLayout(new BoxLayout(packetContainerHttp, BoxLayout.Y_AXIS));
		JPanel packetContainerSpdy = new JPanel();
		packetContainerSpdy.setLayout(new BoxLayout(packetContainerSpdy, BoxLayout.Y_AXIS));
		JPanel packetContainerQuic = new JPanel();
		packetContainerQuic.setLayout(new BoxLayout(packetContainerSpdy, BoxLayout.Y_AXIS));
		//Panel for filenames and paths 
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 3));
		packetContainerHttp.setLayout(new GridLayout(2, 1));
		packetContainerHttp.add(filenameHttp);
		packetContainerHttp.add(pathHttp);
		packetContainerSpdy.setLayout(new GridLayout(2, 2));
		packetContainerSpdy.add(filenameSpdy);
		packetContainerSpdy.add(filenameDecrypt);
		packetContainerSpdy.add(pathSpdy);
		packetContainerSpdy.add(pathDecrypt);
		packetContainerQuic.setLayout(new GridLayout(2, 1));
		packetContainerQuic.add(filenameQuic);
		packetContainerQuic.add(pathQuic);
		//Add filename/path panel to packetContainer
		p.add(packetContainerHttp);
		p.add(packetContainerSpdy);
		p.add(packetContainerQuic);
		packetContainer.add(p);
		//Panel for  HTTP, SPDY, QUIC tables
		p = new JPanel();
		p.setLayout(new GridLayout(1, 3));
		//HTTP Table
		JTable table = new JTable(modelHttp);
		modelHttp.addColumn("Http Packet #"); 
		modelHttp.addColumn("Http Property"); 
		modelHttp.addColumn("Http Value");
		JScrollPane tableContainer = new JScrollPane(table);
		p.add(tableContainer, BorderLayout.WEST);
		//SPDY Table
		table = new JTable(modelSpdy);
		modelSpdy.addColumn("Spdy Packet #");
		modelSpdy.addColumn("Spdy Property"); 
		modelSpdy.addColumn("Spdy Value");
		tableContainer = new JScrollPane(table);
		p.add(tableContainer, BorderLayout.WEST);
		//QUIC Table
		table = new JTable(modelQuic);
		modelQuic.addColumn("Quic Packet #");
		modelQuic.addColumn("Quic Property"); 
		modelQuic.addColumn("Quic Value");
		tableContainer = new JScrollPane(table);
		p.add(tableContainer, BorderLayout.WEST);
		//Add tables panel to packerContainer
		packetContainer.add(p);
		//Panel for browse buttons
		p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		//Add action listeners
		browseHttp.addActionListener(new BrowseHttp());
		browseSpdy.addActionListener(new BrowseSpdy());
		browseDecrypt.addActionListener(new BrowseDecrypt());
		exportResults.addActionListener(new ExportResults());
		browseQuic.addActionListener(new BrowseQuic());
		//Add action listeners to the JPanel
		JPanel spdyContainer = new JPanel();
		JPanel httpContainer = new JPanel();
		JPanel quicContainer = new JPanel();
		httpContainer.add(browseHttp);
		p.add(httpContainer);
		spdyContainer.add(browseSpdy);
		spdyContainer.add(browseDecrypt);
		p.add(spdyContainer);
		quicContainer.add(browseQuic);
		p.add(quicContainer);
		pathHttp.setEditable(false);
		pathSpdy.setEditable(false);
		pathDecrypt.setEditable(false);
		pathQuic.setEditable(false);
		filenameHttp.setEditable(false);
		filenameSpdy.setEditable(false);
		filenameDecrypt.setEditable(false);
		filenameQuic.setEditable(false);
		//Add browse button panel to packetContainer
		packetContainer.add(p);
		//Add packetContainer to the outerContainer
		outerContainer.add(packetContainer);
		//Results table for HTTP
		p = new JPanel();
		p.setPreferredSize(new Dimension(100, 100));
		p.setLayout(new GridLayout(1, 1));
		p.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "HTTP Results",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		table = new JTable(modelResultsHttp);
		modelResultsHttp.addColumn("Property");
		modelResultsHttp.addColumn("Value");
		tableContainer = new JScrollPane(table);
		p.add(tableContainer, BorderLayout.SOUTH);
		//Add results table panel to resultsContainer
		resultsContainer.add(p);
		//Results table for SPDY
		p = new JPanel();
		p.setPreferredSize(new Dimension(100, 100));
		p.setLayout(new GridLayout(1, 1));
		p.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "SPDY Results",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		table = new JTable(modelResultsSpdy);
		modelResultsSpdy.addColumn("Property");
		modelResultsSpdy.addColumn("Value");
		tableContainer = new JScrollPane(table);
		p.add(tableContainer, BorderLayout.SOUTH);
		//Add results table panel to resultsContainer
		resultsContainer.add(p);
		//Results table for QUIC
		p = new JPanel();
		p.setPreferredSize(new Dimension(100, 100));
		p.setLayout(new GridLayout(1, 1));
		p.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "QUIC Results",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		table = new JTable(modelResultsQuic);
		modelResultsQuic.addColumn("Property");
		modelResultsQuic.addColumn("Value");
		tableContainer = new JScrollPane(table);
		p.add(tableContainer, BorderLayout.SOUTH);
		//Add results table panel to resultsContainer
		resultsContainer.add(p);
		//Add Export Results button
		p = new JPanel();
		p.setPreferredSize(new Dimension(100, 100));
		p.add(exportResults);
		exportResults.setVisible(false);
		//Add results table panel to resultsContainer
		resultsContainer.add(p);
		//Add resultsContainer to the outerContainer
		outerContainer.add(resultsContainer);
		//Add the outerContainer to the JFrame
		this.getContentPane().add(outerContainer);
		this.pack();
		this.setVisible(true);
	}
  
	/**
	 * BrowseHttp with an action listener to handle browsing for HTTP file
	 */
	class BrowseHttp implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			initPcap(fileHttp, filenameHttp, pathHttp, modelHttp, Constants.HTTP);
		}
	}
  
	/**
	 * BrowseSpdy with an action listener to handle browsing for SPDY file
	 */
	class BrowseSpdy implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			initPcap(fileSpdy, filenameSpdy, pathSpdy, modelSpdy, Constants.SPDY);
		}
	}
	
	/**
	 * BrowseDecrypt with an action listener to handle browsing for decrypted spdy text file
	 */
	class BrowseDecrypt implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			initPcap(fileDecrypt, filenameDecrypt, pathDecrypt, modelDecrypt, Constants.DECRYPT);
		}
	}
	
	/**
	 * ExportResults with an action listener to handle exporting results to excel file
	 */
	class ExportResults implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			outputResults(resultsHttp, resultsSpdy, resultsQuic);
		}
	}
	
	/**
	 * BrowseQuic with an action listener to handle browsing for QUIC file
	 */
	class BrowseQuic implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			initPcap(fileQuic, filenameQuic, pathQuic, modelQuic, Constants.QUIC);
		}
	}
	
	/**
	 * Copy http/spdy from browse to project
	 * @param file
	 */
	public void saveFileToProject(File file) {
		Path pathTraces = Paths.get(System.getProperty("user.dir"));
		try {
			java.nio.file.Files.copy( 
					file.toPath(), 
					pathTraces.resolve(file.getName()),
					java.nio.file.StandardCopyOption.REPLACE_EXISTING,
					java.nio.file.StandardCopyOption.COPY_ATTRIBUTES,
					java.nio.file.LinkOption.NOFOLLOW_LINKS );
		} catch (IOException ioe) {
			System.out.println("[FileBrowseSave] Save file error: " + ioe);
		}
	}
  
	/**
	 * Initialize pcap file, set filename, set path, and send call to add packets
	 * @param file
	 * @param filename
	 * @param path
	 * @param model
	 */
	public void initPcap(File file, JTextField filename, JTextField path, DefaultTableModel model, String proto) {
		JFileChooser c = new JFileChooser();
		int ret = c.showOpenDialog(FileBrowseSave.this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			file = new File(c.getSelectedFile().toURI());
			filename.setText(c.getSelectedFile().getName());
			path.setText(c.getCurrentDirectory().toString());
			saveFileToProject(file);
			switch (proto) {
				case Constants.HTTP:
					//Get ArrayList of packets from Http pcap file for display
					ArrayList<PacketHttp> packetList = new ArrayList<PacketHttp>();
					packetList = ExtractHttp.start(filename.getText());
					removeRowsFromTable(model);
					addPacketsToTableHttp(packetList, model);
					resultsHttp = new Results();
					resultsHttp = Calculate.startHttp(packetList);
					removeRowsFromTable(modelResultsHttp);
					addResultsToTable(resultsHttp, modelResultsHttp);
					addedHttp = true;
					initExport();
					break;
				case Constants.SPDY:
					//Get ArrayList of packets from Spdy pcap file for display
					ArrayList<PacketSpdy> packetListSpdy = new ArrayList<PacketSpdy>();
					packetListSpdy = ExtractSpdy.start(filename.getText());
					removeRowsFromTable(model);
					addPacketsToTableSpdy(packetListSpdy, model);
					resultsSpdy = new Results();
					resultsSpdy = Calculate.startSpdy(packetListSpdy);
					removeRowsFromTable(modelResultsSpdy);
					addResultsToTable(resultsSpdy, modelResultsSpdy);
					addedSpdy = true;
					initExport();
					break;
				case Constants.QUIC:
					//Get ArrayList of packets from Quic pcap file for display
					ArrayList<PacketQuic> packetListQuic = new ArrayList<PacketQuic>();
					packetListQuic = ExtractQuic.start(filename.getText());
					removeRowsFromTable(model);
					addPacketsToTableQuic(packetListQuic, model);
					resultsQuic = new Results();
					resultsQuic = Calculate.startQuic(packetListQuic);
					removeRowsFromTable(modelResultsQuic);
					addResultsToTable(resultsQuic, modelResultsQuic);
					addedQuic = true;
					initExport();
					break;
				case Constants.DECRYPT:
					ExtractSpdy.setFilenameDecrypt(file.getName());
					break;
				default:
					break;
			}
		}
		if (ret == JFileChooser.CANCEL_OPTION) {
			pathSpdy.setText("");
		}
	}
	
	/**
	 * Add results to the Results table
	 * @param results
	 */
	public void addResultsToTable(Results results, DefaultTableModel model) {
		model.addRow(new Object[]{"Number of Packets", results.getTotalPackets()});
		model.addRow(new Object[]{"Average RTT", results.getRtt() + " ms"});
		model.addRow(new Object[]{"Throughput", results.getThroughput() + " bytes/ms"});
		model.addRow(new Object[]{"Latency", results.getLatency() + " ms"});
		model.addRow(new Object[]{"Total Bytes", results.getTotalBytes() + " bytes"});
		model.addRow(new Object[]{"Number of Connections", results.getNumberOfConnections()});
	}

	/**
	 * Add packets for HTTP
	 * @param packetList
	 * @param model
	 */
	public void addPacketsToTableHttp(ArrayList<PacketHttp> packetList, DefaultTableModel model) {
		//Add the chosen rows for each packet
		for (int i = 0; i < packetList.size(); i++) {
			model.addRow(new Object[]{i, "Source IP", packetList.get(i).getIpSource()});
			model.addRow(new Object[]{i, "Dest IP", packetList.get(i).getIpDest()});
			model.addRow(new Object[]{i, "Timestamp", packetList.get(i).getTimestamp()});
			model.addRow(new Object[]{i, "Size", packetList.get(i).getSize()});
			model.addRow(new Object[]{i, "Seq", packetList.get(i).getSeq()});
			model.addRow(new Object[]{i, "Ack", packetList.get(i).getAck()});
			model.addRow(new Object[]{i, "Mss", packetList.get(i).getMss()});
		}
	}
	
	/**
	 * Add packets for SPDY
	 * @param packetList
	 * @param model
	 */
	public void addPacketsToTableSpdy(ArrayList<PacketSpdy> packetList, DefaultTableModel model) {
		//Add the chosen rows for each packet
		for (int i = 0; i < packetList.size(); i++) {
			model.addRow(new Object[]{i, "Source IP", packetList.get(i).getIpSource()});
			model.addRow(new Object[]{i, "Dest IP", packetList.get(i).getIpDest()});
			model.addRow(new Object[]{i, "Timestamp", packetList.get(i).getTimestamp()});
			model.addRow(new Object[]{i, "Content Type", packetList.get(i).getContentType()});
			model.addRow(new Object[]{i, "VersionP", packetList.get(i).getVersion()});
			model.addRow(new Object[]{i, "SSL Length", packetList.get(i).getLength()});
			for (int j = 0; j < packetList.get(i).getStreams().size(); j++) {
				model.addRow(new Object[]{i, "Stream " + (j+1), packetList.get(i).getStreams().get(j)});
			}
		}
	}
  
	/**
	 * Add packets for QUIC
	 * @param packetList
	 * @param model
	 */
	public void addPacketsToTableQuic(ArrayList<PacketQuic> packetList, DefaultTableModel model) {
		//Add the chosen rows for each packet
		for (int i = 0; i < packetList.size(); i++) {
			model.addRow(new Object[]{i, "Source IP", packetList.get(i).getIpSource()});
			model.addRow(new Object[]{i, "Dest IP", packetList.get(i).getIpDest()});
			model.addRow(new Object[]{i, "Size", packetList.get(i).getSize()});
			model.addRow(new Object[]{i, "Timestamp", packetList.get(i).getTimestamp()});
			model.addRow(new Object[]{i, "Version", packetList.get(i).getVersion()});
			model.addRow(new Object[]{i, "Reset", packetList.get(i).getReset()});
			model.addRow(new Object[]{i, "CID", packetList.get(i).getCid()});
			model.addRow(new Object[]{i, "Sequence Number", packetList.get(i).getSeq()});
			model.addRow(new Object[]{i, "Reserved", packetList.get(i).getReserved()});
		}
	}
	
	/**
	 * If a pcap file already has been loaded, clear the table
	 * @param model
	 */
	public void removeRowsFromTable(DefaultTableModel model) {
		int rows = model.getRowCount();
		if (rows > 0) {
			for (int i = rows-1; i > -1; i--) {
				model.removeRow(i);
			}
		}
	}
	
	/**
	 * Output results to txt file
	 * @throws IOException
	 */
	public static void outputResults(Results resultsHttp, Results resultsSpdy, Results resultsQuic) {
		try {
			BufferedWriter out = null;
			out = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\results.xls"));
			out.write("HTTP");
			out.newLine();
			out.write(String.valueOf(resultsHttp.getTotalPackets()));
			out.newLine();
			out.write(String.valueOf(resultsHttp.getRtt()));
			out.newLine();
			out.write(String.valueOf(resultsHttp.getThroughput()));
			out.newLine();
			out.write(String.valueOf(resultsHttp.getLatency()));
			out.newLine();
			out.write(String.valueOf(resultsHttp.getTotalBytes()));
			out.newLine();
			out.write(String.valueOf(resultsHttp.getNumberOfConnections()));
			out.newLine();
			out.newLine();
			out.write("SPDY");
			out.newLine();
			out.write(String.valueOf(resultsSpdy.getTotalPackets()));
			out.newLine();
			out.write(String.valueOf(resultsSpdy.getRtt()));
			out.newLine();
			out.write(String.valueOf(resultsSpdy.getThroughput()));
			out.newLine();
			out.write(String.valueOf(resultsSpdy.getLatency()));
			out.newLine();
			out.write(String.valueOf(resultsSpdy.getTotalBytes()));
			out.newLine();
			out.write(String.valueOf(resultsSpdy.getNumberOfConnections()));
			out.newLine();
			out.newLine();
			out.write("QUIC");
			out.newLine();
			out.write(String.valueOf(resultsQuic.getTotalPackets()));
			out.newLine();
			out.write(String.valueOf(resultsQuic.getRtt()));
			out.newLine();
			out.write(String.valueOf(resultsQuic.getThroughput()));
			out.newLine();
			out.write(String.valueOf(resultsQuic.getLatency()));
			out.newLine();
			out.write(String.valueOf(resultsQuic.getTotalBytes()));
			out.newLine();
			out.write(String.valueOf(resultsQuic.getNumberOfConnections()));
			out.flush();  
			out.close();  
		} catch (IOException ioe) {
			System.out.println("[Calculate] outputResults error: " + ioe);
		}
	}
	
	/**
	 * Show Export Results button only if both HTTP and SPDY have been added
	 */
	public void initExport() {
		if (addedHttp && addedSpdy && addedQuic) {
			addedHttp = false;
			addedSpdy = false;
			addedQuic = false;
			exportResults.setVisible(true);
		} else {
			exportResults.setVisible(false);
		}
	}
  
	/**
	 * Initialize properties for JFrame
	 * @param frame
	 * @param width
	 * @param height
	 */
	public static void init(JFrame frame) {
		frame.setTitle("Packet Visualization");
		frame.setVisible(true);
		frame.setSize(Constants.WIDTH, Constants.HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}