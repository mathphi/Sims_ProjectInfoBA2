package Model;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import Tools.Point;
import Tools.Size;

public class Shower extends UsableStructure {
	private static final long serialVersionUID = -6219410139900137363L;

	private static final Size SIZE = new Size(3, 8);
	private static final Color COLOR = Color.WHITE;

	public Shower(Point pos) {
		super(pos, SIZE, COLOR);

		COMEBACK_LATER_DELAY = 210;
		JUST_WENT_OUT_DELAY = 30;
		
		OCCUPIED_MSG = "Cette douche est déjà occupée !";
		COMEBACK_LATER_MSG = "Vous devez encore attendre avant de pouvoir prendre à nouveau une douche";
		JUST_WENT_OUT_MSG = "Vous venez de prendre une douche";
	}
	
	@Override
	public void rotate(Direction d) {
		// The shower is not rotation capable (because we don't have any sprite for that)
	}
	
	@Override
	protected void action(Person p) {
		p.takeShower();
	}

	@Override
	protected long getLastTime(Person p) {
		Duration d = Duration.between(p.getLastActionTime(ActionType.Shower), LocalDateTime.now());
		return d.getSeconds();
	}

	@Override
	public GameObject clone() {
		return (GameObject) new Shower(getPos());
	}
}
