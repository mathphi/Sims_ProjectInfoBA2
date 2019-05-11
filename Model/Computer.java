package Model;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import Model.Person.ActionType;
import Tools.Point;
import Tools.Size;
import View.Message.MsgType;

public class Computer extends UsableStructure {
	private static final long serialVersionUID = 5573360277831892967L;
	private final static Size SIZE = new Size(7, 3);

	public Computer(Point pos) {
		super(pos, SIZE, Color.GREEN);
		
		COMEBACK_LATER_DELAY = 180;
		JUST_WENT_OUT_DELAY = 30;
		
		COMEBACK_LATER_MSG = "Vous devez encore attendre avant de travailler à nouveau";
		JUST_WENT_OUT_MSG = "Vous venez de travailler";
		OCCUPIED_MSG = "Cet ordinateur est déjà en cours d'utilisation";
	}

	@Override
	protected void action(Person p) {
		if (p instanceof Worker) {
			p.addMessage("Vous avez commencé à travailler sur l'ordinateur", MsgType.Info);
			
			Worker w = (Worker) p;
			w.work(20, -8, 40, 30);
		} else {
			p.addMessage("Vous ne pouvez pas travailler...", MsgType.Warning);
		}
	}

	@Override
	protected long getLastTime(Person p) {
		Duration d = Duration.between(p.getLastActionTime(ActionType.Work), LocalDateTime.now());
		return d.getSeconds();
	}

	@Override
	public GameObject clone() {
		return (GameObject) new Computer(getPos());
	}
}