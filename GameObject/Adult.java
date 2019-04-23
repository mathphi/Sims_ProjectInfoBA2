package GameObject;

import java.util.ArrayList;
import java.util.Date;

public class Adult extends Person{

	public Adult(int X, int Y, String name, String firstName, Date birthDate,  String gender, int money,
			ArrayList<GameObject> inventoryHouse) {
		super(X, Y, name, firstName, birthDate, gender, gender, money, inventoryHouse);
		// TODO Auto-generated constructor stub
	}

	private void marry(Person partner) {
		//bien joué morray
		//TODO big todo to do including to do what's suppose to be done because it should already have be done as it was to be done
	}
	
	public void characterInteraction(Person people) {
		// function that allows the people to interact with another one
		// interaction is the type of interaction

		switch (people.getRelationship()) {
		case (0): {
			// can only discuss
			// TODO interface graphique: les différentes possibilités!
			if (energy >= 1) {
				discuss(people);
				// TODO augmenter le mood
			}
		}
		case (1): {
			if (energy >= 1) {
				discuss(people);
				// TODO augmenter le mood
				energy -= 1; // on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}

		}
		case (2): {
			if (energy >= 1) {
				discuss(people);
				// TODO augmenter le mood
				energy -= 1;// on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}
			if (energy >= 4) {
				invite(people);
				goToDrink(people);
				// TODO augmenter le mood
				energy -= 3;
			}

		}
		case (3): {
			if (energy >= 1) {
				discuss(people);
				// TODO augmenter le mood
				embrass(people); 
				marry(people);
				energy -= 1;// on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}
			if (energy >= 4) {
				invite(people);
				goToDrink(people);
				// TODO augmenter le mood
				energy -= 3;
			}

		}
		}
		// not a case 3 because can marry at that age

	}

	private void goToDrink(Person people) {
		// TODO Auto-generated method stub
		
	}

	private void embrass(Person people) {
		// TODO Auto-generated method stub
		
	}
}
