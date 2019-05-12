package Tools;

public class Random {
	public static double range(double start, double end) {
		return start + Math.random() * (end - start);
	}
	
	public static int rangeInt(int start, int end) {
		return (int) Math.round(start + Math.random() * (end - start));
	}
	
	public static Point randomPosition(Rect container) {
		return randomPosition(container.topLeft(), container.bottomRight());
	}
	
	public static Point randomPosition(Point start, Point end) {
		double x = range(start.getX(), end.getX());
		double y = range(start.getY(), end.getY());
		
		return new Point(x, y);
	}
}
