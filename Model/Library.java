package Model;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import Tools.Point;
import Tools.Size;

public class Library extends UsableStructure {
	private static final long serialVersionUID = -6219410139900137363L;

	private static final Size SIZE = new Size(6, 6);
	private static final Color COLOR = Color.WHITE;

	public Library(Point pos) {
		super(pos, SIZE, COLOR);

		COMEBACK_LATER_DELAY = 180;
		JUST_WENT_OUT_DELAY = 30;
		
		OCCUPIED_MSG = "Il y a déjà quelqu'un qui utilise cette bibliothèque";
		COMEBACK_LATER_MSG = "Vous devez encore attendre avant de pouvoir lire à nouveau";
		JUST_WENT_OUT_MSG = "Vous venez de lire des livres";
	}
	
	@Override
	public void rotate(Direction d) {
		// The library is not rotation capable (because we don't have any sprite for that)
	}
	
	@Override
	protected void action(Person p) {
		p.readLibrary();
	}

	@Override
	protected long getLastTime(Person p) {
		Duration d = Duration.between(p.getLastActionTime(ActionType.Library), LocalDateTime.now());
		return d.getSeconds();
	}

	@Override
	public GameObject clone() {
		return (GameObject) new Library(getPos());
	}
}
