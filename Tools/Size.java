package Tools;

import java.io.Serializable;

public class Size implements Serializable {
	private static final long serialVersionUID = -3166065755771234670L;
	
	private int width;
	private int height;
	
	public Size(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
