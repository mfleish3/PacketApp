package Objects;

import java.util.ArrayList;

public class DecryptedBytes {

	private int length;
	private ArrayList<String> bytes;
	
	public DecryptedBytes () {
		this.length = 0;
		this.bytes = new ArrayList<String>();
	}
	
	public int getLength() {
		return this.length;
	}
	
	public ArrayList<String> getBytes() {
		return this.bytes;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
}
