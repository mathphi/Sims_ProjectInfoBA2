package Model;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import Tools.Point;
import Tools.Size;
import View.Message.MsgType;

public class Computer extends GameObject {
	private static final long serialVersionUID = 5573360277831892967L;
	private final static Size SIZE = new Size(4,2);

	public Computer(Point pos) {
		super(pos, SIZE, Color.GREEN);
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
		
		if (checkOccupied()) {
			p.addMessage("Cet ordinateur est déjà en cours d'utilisation", MsgType.Problem);
			return;
		}
		
		if (p instanceof Worker) {
			Worker w = (Worker) p;

			// Can work again after 3 minutes
			if (getLastTime(w) > 180) {
				p.addMessage("Vous avez commencé à travailler sur l'ordinateur", MsgType.Info);
				
				// Work on computer cost 20 energy, 8 mood, give 30 money and take 30 seconds
				w.work(20, 8, 30, 30);
			}
			else if (getLastTime(w) < 30) {
				p.addMessage("Vous venez de travailler", MsgType.Problem);
			}
			else {
				p.addMessage("Vous devez encore attendre avant de travailler à nouveau", MsgType.Problem);
			}
		}
		else {
			p.addMessage("Vous ne pouvez pas travailler...", MsgType.Warning);
		}
	}
	
	private boolean checkOccupied() {
		// Search for a working Person around the computer
		boolean occupied = false;
		
		for (GameObject o_around : getObjectsAround()) {
			if (o_around instanceof Worker) {
				Worker w_around = (Worker) o_around;
				
				if (w_around.isWorking()) {
					occupied = true;
					break;
				}
			}
		}
		
		return occupied;
	}

	private long getLastTime(Worker w) {
		Duration d = Duration.between(w.getLastWorkTime(), LocalDateTime.now());
		return d.getSeconds();
	}
	
	@Override
	public void proximityEvent(GameObject o) {}

	@Override
	public GameObject clone() {
		return (GameObject) new Computer(getPos());
	}
}