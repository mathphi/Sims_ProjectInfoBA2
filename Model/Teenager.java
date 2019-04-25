package Model;

import java.util.ArrayList;
import java.util.Date;

import Tools.Random;
import Tools.Point;

public class Teenager extends Person {

	public Teenager(Person player) {

		// constructor if the player need to evolve from kid to teenager
		super(player.getPos(), player.getfirstName(), player.getlastName(), player.getGender(), player.getBirthDate(),
				player.getMoney(), player.getinventoryHouse(), player.getMother(), player.getFather());
	}

	public Teenager(Point pos, String firstName, String lastName, String gender, Date birthDate, int money,
			ArrayList<GameObject> inventoryHouse, Adult mother, Adult father) {
		// constructor if it's a new character (pnj)
		super(pos, firstName, lastName, gender, birthDate, money, inventoryHouse, mother, father);

	}

	public void buy(TakableObject achat) {/*
											 * if (setMoney(-achat.getPrice())) { inventory.add(achat); }
											 */

	}

	protected void goToDrink(Person people) {
		// TODO energy, move to bar pay
		modifyRelationship(people, 3);

	}

	protected void embrass(Person people) {
		// TODO random condition de l'autre personne pour quelle accepte
		boolean theOtherisok = true;
		if (theOtherisok) {
			modifyRelationship(people, 5);
		} else {
			// the other don't want
			people.modifyRelationship(people, -10); // value to be adapted
		}

	}

	public void characterInteraction(Person people) {
		// function that allows the people to interact with another one
		// interaction is the type of interaction

		// TODO this is only to remove an error temporarily
		int energy = 0;

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
				goToDrink(people);
				// TODO augmenter le mood
				energy -= 3;
			}

			break;
		}
		case (3): {
			if (energy >= 1) {
				// discuss(people);
				// TODO augmenter le mood
				embrass(people);
				energy -= 1;// on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				// playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}
			if (energy >= 4) {
				// invite(people);
				goToDrink(people);
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