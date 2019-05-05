package Model;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import Tools.Point;
import Tools.Size;
import View.Message.MsgType;

public class Bed extends GameObject {
	private static final long serialVersionUID = 5573360277831892967L;
	
	private static final Size SIZE = new Size(6, 3);
	private static final Color COLOR = Color.RED;
	
	protected int COMEBACK_LATER_DELAY = 240;
	protected int JUST_WOKEN_UP_DELAY = 30;
	
	protected String OCCUPIED_MSG = 
			"Le lit est déjà occupé !";
	protected String COMEBACK_LATER_MSG = 
			"Vous avez dormi il y a peu de temps";
	protected String JUST_WOKEN_UP_MSG = 
			"Vous venez de vous réveiller";
	
	public Bed(Point pos) {
		super(pos, SIZE, COLOR);
	}

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public void clickedEvent(GameObject o) {
		// The clicked event must comes from a Person at proximity
		if (!getObjectsAround().contains(o))
			return;
		
		Person p = (Person) o;
		boolean bed_occupied = checkOccupied();
		
		// The bed is already occupied by another Person
		if (bed_occupied) {
			p.addMessage(OCCUPIED_MSG, MsgType.Problem);
			return;
		}

		if (getLastTime(p) > COMEBACK_LATER_DELAY) {
			// Can go back to bed after X minutes
			action(p);
		}
		else if (getLastTime(p) < JUST_WOKEN_UP_DELAY) {
			p.addMessage(JUST_WOKEN_UP_MSG, MsgType.Problem);
		}
		else {
			p.addMessage(COMEBACK_LATER_MSG, MsgType.Warning);
		}
	}
	
	/**
	 * Action executed on the Person p.
	 * This function can be easily overwritten.
	 * @param p
	 */
	protected void action(Person p) {
		p.sleep();
	}
	
	/**
	 * Return the last time the Person p slept.
	 * This function can be easily overwritten to adapt to another action (ie. repose)
	 * @param p
	 * @return
	 */
	protected long getLastTime(Person p) {
		Duration d = Duration.between(p.getLastSleepTime(), LocalDateTime.now());
		return d.getSeconds();
	}
	
	protected boolean checkOccupied() {
		// Search for a sleeping Person around the bed
		boolean bed_occupied = false;
		
		for (GameObject o_around : getObjectsAround()) {
			if (o_around instanceof Person) {
				Person p_around = (Person) o_around;
				
				if (p_around.isSleeping()) {
					bed_occupied = true;
					break;
				}
			}
		}
		
		return bed_occupied;
	}

	@Override
	public void proximityEvent(GameObject o) {}

	@Override
	public GameObject clone() {
		return (GameObject) new Bed(getPos());
	}

}