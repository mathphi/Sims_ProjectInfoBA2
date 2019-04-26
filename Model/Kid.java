package Model;

import java.util.ArrayList;

import Tools.Point;

public class Kid extends Person {

	public Kid(Point pos, String firstName, String lastName, String gender, int age, int money,
			ArrayList<GameObject> inventoryHouse, Adult mother, Adult father, ArrayList<Double> psychologicFactor) {
		// only one constructor because it is the first stade of life
		// all that parameters are implemented in Person class
		// if PNJ firstName, lastName, birthDate, money inventoryHouse psychologicFactor are generated
		// randomly
		super(pos, firstName, lastName, gender, age, money, inventoryHouse, mother, father, psychologicFactor);

	}

	// ACTION
	

	// TODO function that make evolve properties during the game
}
