package Model;

import java.awt.Color;

import Tools.Point;
import Tools.Random;
import View.Message.MsgType;

public class Kid extends Person {
	private static final long serialVersionUID = -6840034678894187001L;
	
	// Annual pocket money between 50€ and 100€ with a step of 5
	private final int POCKET_MONEY = Random.rangeInt(10, 20) * 5;

	public Kid(Point pos, String name, int age, Gender gender, Adult mother, Adult father) {
		super(pos, name, age, gender, mother, father);
		
		setColor(new Color(39, 170, 247));
	}

	@Override
	public boolean maxAgeReached() {
		return (getAge() > 13);
	}

	/**
	 * A Kid must receive an annual pocket money to survive !
	 */
	@Override
	public void update(long t) {
		super.update(t);
		
		// New year
		if (t % 365 == 0) {
			receivePocketMoney();
		}
	}
	
	private void receivePocketMoney() {
		modifyMoney(POCKET_MONEY);
		
		addMessage(
				String.format(
						"Vous venez de recevoir %d€ d'argent de poche !",
						POCKET_MONEY),
				MsgType.Info);
	}
}
