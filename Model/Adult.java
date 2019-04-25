package Model;

import Tools.Point;

import java.util.ArrayList;
import java.util.Date;

public class Adult extends Person {

	public Adult(Point pos, String firstName, String lastName, String gender) {
		super(pos, firstName, lastName, gender);
		// TODO Auto-generated constructor stub
	}

	private void marry(Person partner) {
		//bien jou� morray
		//TODO big todo to do including to do what's suppose to be done because it should already have be done as it was to be done
	}
	
	public void characterInteraction(Person people) {
		// function that allows the people to interact with another one
		// interaction is the type of interaction

		//TODO this is only to remove an error temporarily
		int energy = 0;
		
		switch (people.getRelationship()) {
		case (0): {
			// can only discuss
			// TODO interface graphique: les diff�rentes possibilit�s!
			if (energy >= 1) {
				//discuss(people);
				// TODO augmenter le mood
			}
		}
		case (1): {
			if (energy >= 1) {
				//discuss(people);
				// TODO augmenter le mood
				energy -= 1; // on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				//playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}

			break;
		}
		case (2): {
			if (energy >= 1) {
				//discuss(people);
				// TODO augmenter le mood
				energy -= 1;// on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				//playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}
			if (energy >= 4) {
				//invite(people);
				//goToDrink(people);
				// TODO augmenter le mood
				energy -= 3;
			}
			
			break;

		}
		case (3): {
			if (energy >= 1) {
				//discuss(people);
				// TODO augmenter le mood
				//embrass(people); 
				marry(people);
				energy -= 1;// on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				//playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}
			if (energy >= 4) {
				//invite(people);
				//goToDrink(people);
				// TODO augmenter le mood
				energy -= 3;
			}
			
			break;
		}
		default:
			break;
		}
		// not a case 3 because can marry at that age

	}
}