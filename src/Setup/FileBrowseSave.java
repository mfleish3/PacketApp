package Setup;

//AWT imports
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//IO imports
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
//Swing imports
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
//Packet imports
import Packet.Analyze;
import Packet.Packet;
import Settings.Constants;

public class FileBrowseSave extends JFrame {
	
	private JTextField filenameHttp = new JTextField();
	private JTextField filenameSpdy = new JTextField();
	private JTextField pathHttp = new JTextField();
	private JTextField pathSpdy = new JTextField();
	private JButton browseHttp = new JButton("Browse HTTP");
	private JButton browseSpdy = new JButton("Browse SDPY");
	private File fileHttp;
	private File fileSpdy;
	private DefaultTableModel modelHttp = new DefaultTableModel();
	private DefaultTableModel modelSpdy = new DefaultTableModel();
  
	public FileBrowseSave() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		//Add action listeners
		browseHttp.addActionListener(new BrowseHttp());
		browseSpdy.addActionListener(new BrowseSpdy());
		//Add action listeners to the JPanel
		p.add(browseHttp);
		p.add(browseSpdy);;
		//Create the container and add the JPanel
		Container cp = getContentPane();
		cp.add(p, BorderLayout.SOUTH);
		pathHttp.setEditable(false);
		pathSpdy.setEditable(false);
		filenameHttp.setEditable(false);
		filenameSpdy.setEditable(false);
		p = new JPanel();
		p.setLayout(new GridLayout(2, 1));
		p.add(filenameHttp);
		p.add(filenameSpdy);
		p.add(pathHttp);
		p.add(pathSpdy);
		cp.add(p, BorderLayout.NORTH);
		//Create tables for HTTP and SPDY
		p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
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
		this.getContentPane().add(p);
		this.pack();
		this.setVisible(true);
	}
  
	/**
	 * BrowseHttp with an action listener to handle browsing for HTTP file
	 */
	class BrowseHttp implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			initPcap(fileHttp, filenameHttp, pathHttp, modelHttp);
		}
	}
  
	/**
	 * BrowseSpdy with an action listener to handle browsing for SPDY file
	 */
	class BrowseSpdy implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			initPcap(fileSpdy, filenameSpdy, pathSpdy, modelSpdy);
		}
	}
	
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
	public void initPcap(File file, JTextField filename, JTextField path, DefaultTableModel model) {
		JFileChooser c = new JFileChooser();
		int ret = c.showOpenDialog(FileBrowseSave.this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			file = new File(c.getSelectedFile().toURI());
			filename.setText(c.getSelectedFile().getName());
			path.setText(c.getCurrentDirectory().toString());
			saveFileToProject(file);
			//Get ArrayList of packets from Http pcap file for display
			ArrayList<Packet> packetList = new ArrayList<Packet>();
			packetList = Analyze.start("traces/" + filename.getText(), "a");
			removePacketsFromTable(model);
			addPacketsToTable(packetList, model);
		}
		if (ret == JFileChooser.CANCEL_OPTION) {
			pathSpdy.setText("");
		}
	}

	/**
	 * Add packets for HTTP/SPDY to their corresponding table
	 * @param packetList
	 * @param model
	 */
	public void addPacketsToTable(ArrayList<Packet> packetList, DefaultTableModel model) {
		//Add the chosen rows for each packet
		for (int i = 0; i < packetList.size(); i++) {
			model.addRow(new Object[]{i, "Type", packetList.get(i).getType()});
			model.addRow(new Object[]{i, "Source IP", packetList.get(i).getSource()});
			model.addRow(new Object[]{i, "Dest IP", packetList.get(i).getDestination()});
			model.addRow(new Object[]{i, "MSS", packetList.get(i).getMss()});
			model.addRow(new Object[]{i, "Seq #", packetList.get(i).getSeq()});
			model.addRow(new Object[]{i, "Ack #", packetList.get(i).getAck()});
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