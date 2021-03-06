package Model;

import java.awt.Color;

import Tools.Point;
import Tools.Size;

public class WaterClosed extends GameObject implements Activable {
	private static final long serialVersionUID = 5573360277831892967L;
	private final static Size SIZE = new Size(3,3);

	public WaterClosed(Point pos) {
		super(pos, SIZE, Color.WHITE);
	}

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void clickedEvent(GameObject o) {
		// Nothing to do on click
	}

	@Override
	public void proximityEvent(GameObject o) {
		// Empty bladder of incoming person
		if (o instanceof Person) {
			Person p = (Person) o;
			p.rotateToObjectDirection(this);
			p.emptyBladder(true);
		}
	}
	
	public GameObject clone() {
		return (GameObject) new WaterClosed(getPos());
	}
}