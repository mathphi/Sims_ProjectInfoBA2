package Tools;

public class Random {
	public static double range(int start, int end) {
		return start + Math.random() * (end - start); //suppression of (int) -> was giving an int but factor can not been
	}
}
