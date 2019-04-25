package Tools;

public class Rect {
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
	
	/**
	 * Return true if the given point is in the Rect
	 * 
	 * @param p
	 * The point to check the presence
	 * @return
	 */
	public boolean contains(Point p) {
		return (p.getX() >= point.getX() && p.getX() <= point.getX() + size.getWidth() &&
				p.getY() >= point.getY() && p.getY() <= point.getY() + size.getHeight());
	}

	/**
	 * Return true if the given Rect is included in the Rect
	 * 
	 * @param r
	 * The Rect to check the presence
	 * @return
	 */
	public boolean contains(Rect r) {
		return (r.getPos().getX() >= point.getX() &&
				r.getPos().getX() + r.getSize().getWidth() <= point.getX() + size.getWidth() &&
				r.getPos().getY() >= point.getY() &&
				r.getPos().getY() + r.getSize().getHeight() <= point.getY() + size.getHeight());
	}
}
