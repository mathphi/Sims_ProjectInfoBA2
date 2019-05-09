package Tools;

public class Random {
	public static double range(double start, double end) {
		return start + Math.random() * (end - start);
	}
	
	public static int rangeInt(int start, int end) {
		return (int) Math.round(start + Math.random() * (end - start));
	}
}
