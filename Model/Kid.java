package Model;

import java.awt.Color;

import Tools.Point;

public class Kid extends Person {
	private static final long serialVersionUID = -6840034678894187001L;

	public Kid(Point pos, String name, int age, Gender gender, Adult mother, Adult father) {
		super(pos, name, age, gender, mother, father);
		
		setColor(new Color(39, 170, 247));
	}

	@Override
	public boolean maxAgeReached() {
		return (getAge() > 13);
	}

	@Override
	public void clickedEvent(Person person) {
		// TODO Auto-generated method stub
		
	}
	
	// ACTION
	

	// TODO function that make evolve properties during the game
}
