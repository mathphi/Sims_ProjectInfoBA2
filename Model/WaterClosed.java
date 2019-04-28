package Model;

import java.awt.Color;
import java.util.ArrayList;

import Tools.Point;
import Tools.Size;

public class WaterClosed extends GameObject {
	private static final long serialVersionUID = 5573360277831892967L;
	private final static Size SIZE = new Size(2,2);

	public WaterClosed(Point pos) {
		super(pos, SIZE, Color.WHITE);
	}

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void clickedEvent() {
		// Empty bladder of person around on click
		ArrayList<GameObject> obj_list = getObjectsAround();
		
		for (GameObject obj : obj_list) {
			if (obj instanceof Person) {
				((Person)obj).emptyBladder(true);
			}
		}
	}
}