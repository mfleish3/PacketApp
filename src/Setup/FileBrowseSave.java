package Setup;

//AWT imports
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//IO imports
import java.io.File;
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
import Objects.Packet;
import Objects.PacketSpdy;
import Objects.Results;
//Packet imports
import Packet.ExtractHttp;
import Packet.ExtractSpdy;
import Settings.Constants;

public class FileBrowseSave extends JFrame {
	
	private JTextField filenameHttp = new JTextField();
	private JTextField filenameSpdy = new JTextField();
//	private JTextField filenameQuic = new JTextField();
	private JTextField pathHttp = new JTextField();
	private JTextField pathSpdy = new JTextField();
//	private JTextField pathQuic = new JTextField();
	private JButton browseHttp = new JButton("Browse HTTP");
	private JButton browseSpdy = new JButton("Browse SDPY");
//	private JButton browseQuic = new JButton("Browse QUIC");
	private File fileHttp;
	private File fileSpdy;
//	private File fileQuic;
	private DefaultTableModel modelHttp = new DefaultTableModel();
	private DefaultTableModel modelSpdy = new DefaultTableModel();
//	private DefaultTableModel modelQuic = new DefaultTableModel();
	private DefaultTableModel modelResultsHttp = new DefaultTableModel();
	private DefaultTableModel modelResultsSpdy = new DefaultTableModel();
  
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
		//Panel for filenames and paths 
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 1));
		p.add(filenameHttp);
		p.add(filenameSpdy);
//		p.add(filenameQuic);
		p.add(pathHttp);
		p.add(pathSpdy);
//		p.add(pathQuic);
		//Add filename/path panel to packetContainer
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
//		table = new JTable(modelQuic);
//		modelQuic.addColumn("Quic Packet #");
//		modelQuic.addColumn("Quic Property"); 
//		modelQuic.addColumn("Quic Value");
//		tableContainer = new JScrollPane(table);
//		p.add(tableContainer, BorderLayout.WEST);
		//Add tables panel to packerContainer
		packetContainer.add(p);
		//Panel for browse buttons
		p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		//Add action listeners
		browseHttp.addActionListener(new BrowseHttp());
		browseSpdy.addActionListener(new BrowseSpdy());
//		browseQuic.addActionListener(new BrowseQuic());
		//Add action listeners to the JPanel
		p.add(browseHttp);
		p.add(browseSpdy);
//		p.add(browseQuic);
		pathHttp.setEditable(false);
		pathSpdy.setEditable(false);
//		pathQuic.setEditable(false);
		filenameHttp.setEditable(false);
		filenameSpdy.setEditable(false);
//		filenameQuic.setEditable(false);
		//Add browse button panel to packetContainer
		packetContainer.add(p);
		//Add packetContainer to the outerContainer
		outerContainer.add(packetContainer);
		//Results table for HTTP
		p = new JPanel();
		p.setPreferredSize(new Dimension(10, 10));
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
		p.setPreferredSize(new Dimension(500,100));
		//Add results table panel to resultsContainer
		resultsContainer.add(p);
		//Results table for SPDY
		p = new JPanel();
		p.setPreferredSize(new Dimension(10, 10));
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
		p.setPreferredSize(new Dimension(500,100));
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
	 * BrowseQuic with an action listener to handle browsing for QUIC file
	 */
//	class BrowseQuic implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			initPcap(fileQuic, filenameQuic, pathQuic, modelQuic, Constants.QUIC);
//		}
//	}
	
	/**
	 * Copy http/spdy from browse to project
	 * @param file
	 */
	public void saveFileToProject(File file) {
		Path pathTraces = Paths.get(System.getProperty("user.dir")+"\\traces");
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
			Results results = new Results();
			switch (proto) {
				case Constants.HTTP:
					//Get ArrayList of packets from Http pcap file for display
					ArrayList<Packet> packetList = new ArrayList<Packet>();
					packetList = ExtractHttp.start("traces/" + filename.getText(), "a");
					removePacketsFromTable(model);
					addPacketsToTableHttp(packetList, model);
					results = Calculate.startHttp(packetList);
					addResultsToTable(results, modelResultsHttp);
					break;
				case Constants.SPDY:
					//Get ArrayList of packets from Spdy pcap file for display
					ArrayList<PacketSpdy> packetListSpdy = new ArrayList<PacketSpdy>();
					packetListSpdy = ExtractSpdy.start("traces/" + filename.getText(), "a");
					addPacketsToTableSpdy(packetListSpdy, model);
					results = Calculate.startSpdy(packetListSpdy);
					addResultsToTable(results, modelResultsSpdy);
					break;
				case Constants.QUIC:
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
		model.addRow(new Object[]{"Average RTT", results.getRtt() + " ms"});
		model.addRow(new Object[]{"Throughput", results.getThroughput() + " bytes/ms"});
		model.addRow(new Object[]{"Latency", results.getLatency() + " ms"});
		model.addRow(new Object[]{"Total Bytes", results.getTotalBytes() + " bytes"});
	}

	/**
	 * Add packets for HTTP
	 * @param packetList
	 * @param model
	 */
	public void addPacketsToTableHttp(ArrayList<Packet> packetList, DefaultTableModel model) {
		//Add the chosen rows for each packet
		for (int i = 0; i < packetList.size(); i++) {
			model.addRow(new Object[]{i, "Source IP", packetList.get(i).getIpSource()});
			model.addRow(new Object[]{i, "Dest IP", packetList.get(i).getIpDest()});
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
	 * If a pcap file already has been loaded, clear the table
	 * @param model
	 */
	public void removePacketsFromTable(DefaultTableModel model) {
		int rows = model.getRowCount();
		if (rows > 0) {
			for (int i = rows-1; i > -1; i--) {
				model.removeRow(i);
			}
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