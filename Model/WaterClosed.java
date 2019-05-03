package Model;

import java.awt.Color;

import Tools.Point;
import Tools.Size;

public class WaterClosed extends GameObject{
	private static final long serialVersionUID = 5573360277831892967L;
	private final static Size SIZE = new Size(2,1);

	public WaterClosed(Point pos) {
		super(pos, SIZE, Color.WHITE);
	}

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void clickedEvent(Person person) {
		// Nothing to do on click
	}

	@Override
	public void proximityEvent(GameObject o) {
		// Empty bladder of incoming person
		if (o instanceof Person) {
			((Person)o).emptyBladder(true);
		}
	}
	
	public GameObject clone() {
		return (GameObject) new WaterClosed(getPos());
	}
}