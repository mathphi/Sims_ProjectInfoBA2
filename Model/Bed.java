package Model;

import java.time.Duration;
import java.awt.Color;
import java.time.LocalDateTime;

import Tools.Point;
import Tools.Size;

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
		System.out.print(d.getSeconds());
		if(d.getSeconds() > 30){
			//can go to bed every 30 seconds
			p.restoreEnergy();
			p.setLastBedTime(LocalDateTime.now());
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