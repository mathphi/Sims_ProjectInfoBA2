package Model;

import Tools.Point;

public class Kid extends Person {
	private static final long serialVersionUID = -6840034678894187001L;

	public Kid(Point pos, String firstName, String lastName, Gender gender,
			Adult mother, Adult father, PsychologicalFactors psychologicFactor) {
		// only one constructor because it is the first stade of life
		// all that parameters are implemented in Person class
		// if PNJ firstName, lastName, birthDate, money inventoryHouse psychologicFactor are generated
		// randomly
		super(pos, firstName, lastName, gender, mother, father, psychologicFactor);

	}

	// ACTION
	

	// TODO function that make evolve properties during the game
}
