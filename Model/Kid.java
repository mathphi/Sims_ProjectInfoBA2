package Model;

import java.util.ArrayList;
import java.util.Date;

import Tools.Random;
import Tools.Point;

public class Kid extends Person {

	public Kid(Point pos, String firstName, String lastName, String gender, Date birthDate, int money,
			ArrayList<GameObject> inventoryHouse, Adult mother, Adult father) {
		// only one constructor because it is the first stade of life
		// all that parameters are implemented in Person class
		// if PNJ firstName, lastName, birthDate, moneyn inventoryHouse are generated
		// randomly
		super(pos, firstName, lastName, gender, birthDate, money, inventoryHouse, mother, father);


	}

	// ACTION

	
	
	//TODO function that make evolve properties during the game
}
