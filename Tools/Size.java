package Tools;

import java.io.Serializable;

public class Size implements Serializable {
	private static final long serialVersionUID = -3166065755771234670L;
	
	private int width;
	private int height;
	
	public Size(int w, int h) {
		if (w < 0 || h < 0)
			throw new IllegalArgumentException("Invalid size, parameters cannot be negative");
		
		this.width = w;
		this.height = h;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void setWidth(int w) {
		if (w < 0)
			throw new IllegalArgumentException("Invalid size, parameters cannot be negative");
		
		this.width = w;
	}

	public void setHeight(int h) {
		if (h < 0)
			throw new IllegalArgumentException("Invalid size, parameters cannot be negative");
		
		this.height = h;
	}
	
	public Size add(Size s) {
		return new Size(width + s.getWidth(), height + s.getHeight());
	}
	
	public Size add(int w, int h) {
		return add(new Size(w, h));
	}
	
	public Size permuted() {
		return new Size(height, width);
	}
	
	public String toString() {
		return String.format("Size(%d, %d)", width, height);
	}
}
