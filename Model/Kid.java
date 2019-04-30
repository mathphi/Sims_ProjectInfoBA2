package Model;

import Tools.Point;

public class Kid extends Person {
	private static final long serialVersionUID = -6840034678894187001L;

	public Kid(Point pos, String name, Gender gender, Adult mother, Adult father) {
		super(pos, name, gender, mother, father);
		
		// The kid begin with an age of 10 years
		this.age = 10;

	}

	// ACTION
	

	// TODO function that make evolve properties during the game
}
