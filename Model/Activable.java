package Model;

public interface Activable {
	public static enum ActionType {
		Sleep, Nap, Toilet, Shower, Bath, Work, Television, Library
	}
	
	public void clickedEvent(GameObject o);
	public void proximityEvent(GameObject o);
}
