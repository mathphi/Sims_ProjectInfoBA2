package Tools;

public class Random {
	public static int range(int start, int end) {
		return start + (int) Math.random() * (end - start);
	}
}
