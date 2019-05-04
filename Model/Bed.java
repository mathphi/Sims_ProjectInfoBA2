package Model;

import java.time.Duration;
import java.awt.Color;
import java.time.LocalDateTime;

import Tools.Point;
import Tools.Size;
import View.Message.MsgType;

public class Bed extends GameObject {
	private static final long serialVersionUID = 5573360277831892967L;
	private final static Size SIZE = new Size(4, 2);

	public Bed(Point pos) {
		super(pos, SIZE, Color.RED);
		// could be good to make 2 colors
	}

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void clickedEvent(GameObject o) {
		Person p = (Person) o;
		Duration d = Duration.between(p.getLastBedTime(), LocalDateTime.now());

		if (d.getSeconds() > 240) {
			// Can go back to bed after 4 minutes
			p.sleep();
		}
		else if (d.getSeconds() < 30) {
			p.addMessage("Vous venez de vous réveiller", MsgType.Problem);
		}
		else {
			p.addMessage("Vous avez dormi il y a peu de temps", MsgType.Problem);
		}
	}

	@Override
	public void proximityEvent(GameObject o) {

	}

	@Override
	public GameObject clone() {
		return (GameObject) new Bed(getPos());
	}

}