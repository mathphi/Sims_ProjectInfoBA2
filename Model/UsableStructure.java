package Model;

import java.awt.Color;

import Tools.Point;
import Tools.Size;
import View.Message.MsgType;

public abstract class UsableStructure extends GameObject implements Activable {
	private static final long serialVersionUID = 6948483223727288003L;

	protected String OCCUPIED_MSG;
	protected String COMEBACK_LATER_MSG;
	protected String JUST_WENT_OUT_MSG;
	protected int COMEBACK_LATER_DELAY;
	protected int JUST_WENT_OUT_DELAY;
	
	public UsableStructure(Point pos, Size size, Color color) {
		super(pos, size, color);
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
		p.rotateToObjectDirection(this);
		p.notifyRefresh();
		
		boolean occupied = checkOccupied();
		
		// The bed is already occupied by another Person
		if (occupied) {
			p.addMessage(OCCUPIED_MSG, MsgType.Problem);
			return;
		}

		if (getLastTime(p) > COMEBACK_LATER_DELAY) {
			action(p);
		}
		else if (getLastTime(p) < JUST_WENT_OUT_DELAY) {
			p.addMessage(JUST_WENT_OUT_MSG, MsgType.Problem);
		}
		else {
			p.addMessage(COMEBACK_LATER_MSG, MsgType.Warning);
		}
	}
	
	/**
	 * Action executed on the Person p.
	 */
	protected abstract void action(Person p);

	/**
	 * Returns the last time the Person p did this action.
	 */
	protected abstract long getLastTime(Person p);
	
	protected boolean checkOccupied() {
		// Search for a locked Person around the object
		boolean occupied = false;
		
		for (GameObject o_around : getObjectsAround()) {
			if (o_around instanceof Person) {
				Person p_around = (Person) o_around;
				
				if (p_around.isLocked()) {
					occupied = true;
					break;
				}
			}
		}
		
		return occupied;
	}

	@Override
	public void proximityEvent(GameObject o) {}
}
