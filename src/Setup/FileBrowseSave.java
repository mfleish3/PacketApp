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

//Swing imports
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jnetpcap.Pcap;

import Display.PacketHandler;
import Settings.Constants;

public class FileBrowseSave extends JFrame {
	
  private JTextField filenameHttp = new JTextField();
  private JTextField filenameSpdy = new JTextField();
  private JTextField pathHttp = new JTextField();
  private JTextField pathSpdy = new JTextField();
  private JButton browseHttp = new JButton("Browse HTTP");
  private JButton browseSpdy = new JButton("Browse SDPY");
  private JButton saveHttp = new JButton("Save HTTP");
  private JButton saveSpdy = new JButton("Save SPDY");
  private JButton openHttp = new JButton("Open HTTP");
  private JButton openSpdy = new JButton("Open SPDY");
  private File fileHttp;
  private File fileSpdy;
  
  public FileBrowseSave() {
    JPanel p = new JPanel();
    browseHttp.addActionListener(new BrowseHttp());
    browseSpdy.addActionListener(new BrowseSpdy());
    saveHttp.addActionListener(new FileHttp());
    saveSpdy.addActionListener(new FileSpdy());
    openHttp.addActionListener(new OpenHttp());
    openSpdy.addActionListener(new OpenSpdy());
    p.add(browseHttp);
    p.add(saveHttp);
    p.add(openHttp);
    p.add(browseSpdy);
    p.add(saveSpdy);
    p.add(openSpdy);
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
  }
  
  class OpenHttp implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		  PacketHandler.getInstance().checkPackets("traces/" + filenameHttp.getText());
	  }
  }
  
  class OpenSpdy implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		  PacketHandler.getInstance().checkPackets("traces/" + filenameSpdy.getText());
	  }
  }

  class BrowseHttp implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JFileChooser c = new JFileChooser();
      int ret = c.showOpenDialog(FileBrowseSave.this);
      if (ret == JFileChooser.APPROVE_OPTION) {
		  fileHttp = new File(c.getSelectedFile().toURI());
		  filenameHttp.setText(c.getSelectedFile().getName());
		  pathHttp.setText(c.getCurrentDirectory().toString());
      }
      if (ret == JFileChooser.CANCEL_OPTION) {
        pathHttp.setText("");
      }
    }
  }
  
  class BrowseSpdy implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JFileChooser c = new JFileChooser();
      int ret = c.showOpenDialog(FileBrowseSave.this);
      if (ret == JFileChooser.APPROVE_OPTION) {
		  fileSpdy = new File(c.getSelectedFile().toURI());
		  filenameSpdy.setText(c.getSelectedFile().getName());
		  pathSpdy.setText(c.getCurrentDirectory().toString());
      }
      if (ret == JFileChooser.CANCEL_OPTION) {
        pathHttp.setText("");
      }
    }
  }

  /**
   * Save http file to project path for visualization
   * @author Matt Fleishman
   *
   */
  class FileHttp implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		  saveFileToProject(fileHttp);
	  }
  }
  
  /**
   * Save SPDY file to project path for visualization
   * @author Matt Fleishman
   *
   */
  class FileSpdy implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		 saveFileToProject(fileSpdy);
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