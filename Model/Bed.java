package Model;

import java.awt.Color;

import Tools.Point;
import Tools.Size;

public class Bed extends GameObject{
	private static final long serialVersionUID = 5573360277831892967L;
	private final static Size SIZE = new Size(4,2);

	public Bed(Point pos) {
		super(pos, SIZE, Color.RED);
		//could be good to make 2 colors
	}

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void clickedEvent() {
	
	}

	@Override
	public void proximityEvent(GameObject o) {
		if (o instanceof Person) {
			((Person)o).restoreEnergy();
			//TODO make the people in the bed and unusable
		}
		
	}

	@Override
	public GameObject clone() {
		return (GameObject) new Bed(getPos());
	}
	
	
}