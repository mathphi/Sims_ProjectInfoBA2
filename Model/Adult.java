package Model;

import Tools.Point;

import java.util.ArrayList;
import java.util.Date;

public class Adult extends Person {

	public Adult(Person player) {
		// constructor if the player need to evolve from teenager to kid
		super(player.getPos(), player.getfirstName(), player.getlastName(), player.getGender(), player.getAge(),
				player.getMoney(), player.getinventoryHouse(), player.getMother(), player.getFather(), player.getPsychologicFactor());
	}

	public Adult(Point pos, String firstName, String lastName, String gender, int age, int money,
			ArrayList<GameObject> inventoryHouse, Adult mother, Adult father,ArrayList<Double> psychologicFactor  ) {
		// constructor if it's a new character
		super(pos, firstName, lastName, gender, age, money, inventoryHouse, mother, father, psychologicFactor);

	}

	private void marry(Person partner) {
		// bien jou� morray
		// TODO big todo to do including to do what's suppose to be done because it
		// should already have be done as it was to be done
	}

	protected void goToDrink(Person people) {
		// move to bar pay
		modifyRelationship(people, 3);
		modifyMood(automaticAnswer(people)*25);

		energy -= 30;

	}

	protected void embrass(Person people) {
		if (automaticAnswer(people) >= 1.6) {
			modifyRelationship(people, 5);
			modifyMood(automaticAnswer(people) * 40);
		} else {
			// the other don't want
			people.modifyRelationship(people, -10); // value to be adapted
			modifyMood(automaticAnswer(people) * -40);
		}
		energy -= 15;

	}

	public void characterInteraction(Person people) {
		// function that allows the people to interact with another one
		// interaction is the type of interaction

		switch (getRelationship(people)) {
		case (0): {
			// can only discuss
			// TODO interface graphique: les diff�rentes possibilit�s!
			if (energy >= 10) {
				discuss(people);

			}
		}
		case (1): {
			if (energy >= 10) {
				discuss(people);

			}

			if (energy >= 20) {
				playWith(people);

			}

			break;
		}
		case (2): {
			if (energy >= 10) {
				discuss(people);

			}

			if (energy >= 20) {
				playWith(people);

			}
			if (energy >= 40) {
				invite(people);
				goToDrink(people);

			}

			break;

		}
		case (3): {
			if (energy >= 10) {
				discuss(people);

				embrass(people);
				marry(people);

			}

			if (energy >= 20) {
				playWith(people);

			}
			if (energy >= 40) {
				invite(people);
				goToDrink(people);

			}

			break;
		}
		default:
			break;
		}

	}
}