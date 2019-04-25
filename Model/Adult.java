package Model;

import Tools.Point;

import java.util.ArrayList;
import java.util.Date;

public class Adult extends Person {

	public Adult(Person player) {
		// constructor if the player need to evolve from teenager to kid
		super(player.getPos(), player.getfirstName(), player.getlastName(), player.getGender(), player.getBirthDate(),
				player.getMoney(), player.getinventoryHouse(), player.getMother(), player.getFather());
	}

	public Adult(Point pos, String firstName, String lastName, String gender, Date birthDate, int money,
			ArrayList<GameObject> inventoryHouse, Adult mother, Adult father) {
		// constructor if it's a new character
		super(pos, firstName, lastName, gender, birthDate, money, inventoryHouse, mother, father);

	}

	private void marry(Person partner) {
		// bien jou� morray
		// TODO big todo to do including to do what's suppose to be done because it
		// should already have be done as it was to be done
	}

	public void characterInteraction(Person people) {
		// function that allows the people to interact with another one
		// interaction is the type of interaction

		switch (getRelationship(people)) {
		case (0): {
			// can only discuss
			// TODO interface graphique: les diff�rentes possibilit�s!
			if (energy >= 1) {
				// discuss(people);
				// TODO augmenter le mood
			}
		}
		case (1): {
			if (energy >= 1) {
				// discuss(people);
				// TODO augmenter le mood
				energy -= 1; // on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				// playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}

			break;
		}
		case (2): {
			if (energy >= 1) {
				// discuss(people);
				// TODO augmenter le mood
				energy -= 1;// on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				// playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}
			if (energy >= 4) {
				// invite(people);
				// goToDrink(people);
				// TODO augmenter le mood
				energy -= 3;
			}

			break;

		}
		case (3): {
			if (energy >= 1) {
				// discuss(people);
				// TODO augmenter le mood
				// embrass(people);
				marry(people);
				energy -= 1;// on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				// playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}
			if (energy >= 4) {
				// invite(people);
				// goToDrink(people);
				// TODO augmenter le mood
				energy -= 3;
			}

			break;
		}
		default:
			break;
		}

	}
}