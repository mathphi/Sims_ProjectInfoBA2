package Tools;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = -2456799340429225544L;
	
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point add(Point p) {
		return new Point(x + p.getX(), y + p.getY());
	}
}
