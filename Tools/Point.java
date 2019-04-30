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

	public Point add(int x, int y) {
		return add(new Point(x, y));
	}

	public Point add(Point p) {
		return new Point(x + p.getX(), y + p.getY());
	}
	
	public double getDistance(Point p) {
		int dx = x - p.getX();
		int dy = y - p.getY();
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}
	
	public String toString() {
		return String.format("Point(%d, %d)", x, y);
	}
}
