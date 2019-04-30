package Model;

import java.awt.Color;

import Tools.Point;
import Tools.Size;

public class GroundFlooring extends GroundObject {
	private static final long serialVersionUID = -122003289613012910L;
	private int type;

	public GroundFlooring(Point pos, int type) {
		super(pos, new Size(type, type), new Color(230, 191, 131));
		this.type = type;
	}

	@Override
	public void clickedEvent() {}

	@Override
	public void proximityEvent(GameObject o) {}

	@Override
	public GameObject clone() {
		return (GameObject) new GroundFlooring(getPos(), type);
	}
}
