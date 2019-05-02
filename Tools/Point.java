package Tools;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = -2456799340429225544L;
	
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public int getXInt() {
		return (int)x;
	}

	public int getYInt() {
		return (int)y;
	}

	public Point add(double x, double y) {
		return add(new Point(x, y));
	}

	public Point add(Point p) {
		return new Point(x + p.getX(), y + p.getY());
	}
	
	public Point multiply(double factor) {
		return new Point(x * factor, y * factor);
	}
	
	public double getDistance(Point p) {
		double dx = x - p.getX();
		double dy = y - p.getY();
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}
	
	public String toString() {
		return String.format("Point(%.2f, %.2f)", x, y);
	}
}
