package Model;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import Tools.Point;
import Tools.Size;

public class Television extends UsableStructure {
	private static final long serialVersionUID = 5573360277831892967L;
	
	private static final Size SIZE = new Size(5, 3);
	private static final Color COLOR = Color.RED;
	
	public Television(Point pos) {
		super(pos, SIZE, COLOR);

		COMEBACK_LATER_DELAY = 240;
		JUST_WENT_OUT_DELAY = 30;
		
		OCCUPIED_MSG = "Cette télévision est déjà en cours d'utilisation";
		COMEBACK_LATER_MSG = "Vous avez regardé la télévision il y a peu de temps";
		JUST_WENT_OUT_MSG = "Vous venez de regarder la télévision";
	}
	
	/**
	 * Action executed on the Person p.
	 * @param p
	 */
	@Override
	protected void action(Person p) {
		p.viewTelevision();
	}
	
	/**
	 * Return the last time the Person p slept.
	 * @param p
	 * @return
	 */
	@Override
	protected long getLastTime(Person p) {
		Duration d = Duration.between(p.getLastActionTime(ActionType.Television), LocalDateTime.now());
		return d.getSeconds();
	}
	
	@Override
	public void proximityEvent(GameObject o) {}

	@Override
	public GameObject clone() {
		return (GameObject) new Television(getPos());
	}
}