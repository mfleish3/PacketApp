package Setup;

import javax.swing.JFrame;

public class StartProject {

	private static StartProject inst = null;
	private static JFrame frame = new FileBrowseSave();
	
	/**
	 * PacketHandler constructor
	 */
	public StartProject() {
	
	}
	
	/**
	 * Singleton to maintain a static reference to one instance of PacketHandler
	 * @return instance of PacketHandler
	 */
	public static StartProject getInstance() {
		if(inst == null) {
			inst = new StartProject();
		}
		return inst;
	}
	
	/**
	 * Start the HTTP/SPDY visualization application
	 * @param args
	 */
	public static void main(String[] args) {
		FileBrowseSave.init(frame);
	}
	
	public void setFrame(JFrame frame) {
		StartProject.frame = frame;
	}
	
	public JFrame getFrame() {
		return StartProject.frame;
	}
	
}
