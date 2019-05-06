package Model;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import Model.Person.ActionType;
import Tools.Point;
import Tools.Size;

public class Bed extends UsableStructure {
	private static final long serialVersionUID = 5573360277831892967L;
	
	private static final Size SIZE = new Size(6, 3);
	private static final Color COLOR = Color.RED;
	
	public Bed(Point pos) {
		super(pos, SIZE, COLOR);

		COMEBACK_LATER_DELAY = 240;
		JUST_WENT_OUT_DELAY = 30;
		
		OCCUPIED_MSG = "Le lit est déjà occupé !";
		COMEBACK_LATER_MSG = "Vous avez dormi il y a peu de temps";
		JUST_WENT_OUT_MSG = "Vous venez de vous réveiller";
	}

	@Override
	public boolean isObstacle() {
		return true;
	}
	
	/**
	 * Action executed on the Person p.
	 * @param p
	 */
	@Override
	protected void action(Person p) {
		p.sleep();
	}
	
	/**
	 * Return the last time the Person p slept.
	 * @param p
	 * @return
	 */
	@Override
	protected long getLastTime(Person p) {
		Duration d = Duration.between(p.getLastActionTime(ActionType.Sleep), LocalDateTime.now());
		return d.getSeconds();
	}
	
	@Override
	public void proximityEvent(GameObject o) {}

	@Override
	public GameObject clone() {
		return (GameObject) new Bed(getPos());
	}

}