package Model;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import Tools.Point;
import Tools.Size;

public class Sofa extends Bed {
	private static final long serialVersionUID = -7349847768725442782L;

	private static final Size SIZE = new Size(3, 2);
	private static final Color COLOR = Color.ORANGE;
	
	public Sofa(Point pos) {
		super(pos);
		
		setSize(SIZE);
		setColor(COLOR);
		
		COMEBACK_LATER_DELAY = 60;
		JUST_WOKEN_UP_DELAY = 10;
		
		OCCUPIED_MSG = "Ce fauteuil est déjà occupé !";
		COMEBACK_LATER_MSG = "Vous vous êtes reposé il y a peu de temps";
		JUST_WOKEN_UP_MSG = "Vous venez de vous reposer !";
	}
	
	@Override
	protected void action(Person p) {
		p.repose();
	}
	
	@Override
	protected long getLastTime(Person p) {
		Duration d = Duration.between(p.getLastNapTime(), LocalDateTime.now());
		return d.getSeconds();
	}
	
	@Override
	public GameObject clone() {
		return (GameObject) new Sofa(getPos());
	}

}