package Model;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import Model.Person.ActionType;
import Tools.Point;
import Tools.Size;

public class Bath extends UsableStructure {
	private static final long serialVersionUID = -6219410139900137363L;

	private static final Size SIZE = new Size(3, 6);
	private static final Color COLOR = Color.WHITE;

	public Bath(Point pos) {
		super(pos, SIZE, COLOR);

		COMEBACK_LATER_DELAY = 480; // 8 minutes
		JUST_WENT_OUT_DELAY = 30;
		
		OCCUPIED_MSG = "Cette baignoire est déjà occupée !";
		COMEBACK_LATER_MSG = "Voud devez encore attendre avant de pouvoir prendre à nouveau un bain";
		JUST_WENT_OUT_MSG = "Vous venez de prendre un bain";
	}
	
	@Override
	protected void action(Person p) {
		p.takeBath();
	}

	@Override
	protected long getLastTime(Person p) {
		Duration d = Duration.between(p.getLastActionTime(ActionType.Bath), LocalDateTime.now());
		return d.getSeconds();
	}

	@Override
	public GameObject clone() {
		return (GameObject) new Bath(getPos());
	}
}
