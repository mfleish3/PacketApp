package Setup;

import javax.swing.JFrame;

public class StartProject {

	private static JFrame frame = new FileBrowseSave();
	
	/**
	 * Start the HTTP/SPDY visualization application
	 * @param args
	 */
	public static void main(String[] args) {
		FileBrowseSave.init(frame);
	}	
}