package Tools;

import java.io.Serializable;

public class Rect implements Serializable {
	private static final long serialVersionUID = -6049308199951080696L;
	
	private Point point;
	private Size size;
	
	public Rect(int x, int y, int w, int h) {
		this(new Point(x, y), new Size(w, h));
	}
	
	public Rect(Point p, Size s) {
		this.point = p;
		this.size = s;
	}
	
	public Point getPos() {
		return point;
	}
	
	public Size getSize() {
		return size;
	}
	
	public void setPos(Point p) {
		point = p;
	}
	
	public void setSize(Size sz) {
		size = sz;
	}
	
	public Rect permuted() {
		return new Rect(point, size.permuted());
	}

	public Point topLeft() {
		return point;
	}
	public Point topRight() {
		return point.add(size.getWidth(), 0);
	}
	public Point bottomLeft() {
		return point.add(0, size.getHeight());
	}
	public Point bottomRight() {
		return point.add(size.getWidth(), size.getHeight());
	}
	
	/**
	 * Return true if the given point is in the Rect
	 * 
	 * @param p
	 * The point to check the presence
	 * @return
	 */
	public boolean contains(Point p) {
		return (p.getX() >= topLeft().getX() && p.getX() < bottomRight().getX() &&
				p.getY() >= topLeft().getY() && p.getY() < bottomRight().getY());
	}

	/**
	 * Return true if the given Rect is included in the Rect
	 * 
	 * @param r
	 * The Rect to check the presence
	 * @return
	 */
	public boolean contains(Rect r) {
		return (r.topLeft().getX()     >= topLeft().getX()     &&
				r.bottomRight().getX() <  bottomRight().getX() &&
				r.topLeft().getY() 	   >= topLeft().getY()     &&
				r.bottomRight().getY() <  bottomRight().getY());
	}
	
	/**
	 * Return true if the given Rect overlaps this Rect
	 * 
	 * @param r
	 * The rect to check the overlapping
	 * @return
	 */
	public boolean overlaps(Rect r) {
		return !(topRight().getX()   <= r.bottomLeft().getX() ||
				 bottomLeft().getX() >= r.topRight().getX() 	 ||
				 topRight().getY()   >= r.bottomLeft().getY() ||
				 bottomLeft().getY() <= r.topRight().getY());
	}

	public String toString() {
		return String.format("Rect(%d, %d, %dx%d)",
				point.getX(), point.getY(), size.getWidth(), size.getHeight());
	}
}
