package Model;

import java.awt.Color;

import Tools.Point;
import Tools.Size;

public class GroundGrass extends GroundObject {
	private static final long serialVersionUID = 8776762110367357058L;
	private int type;

	public GroundGrass(Point pos, int type) {
		super(pos, new Size(type, type), new Color(70, 200, 60));
		this.type = type;
	}
	
	@Override
	public void clickedEvent(Person person) {}

	@Override
	public void proximityEvent(GameObject o) {}

	@Override
	public GameObject clone() {
		return (GameObject) new GroundGrass(getPos(), type);
	}
}
