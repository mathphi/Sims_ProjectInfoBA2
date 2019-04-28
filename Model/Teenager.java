package Model;

import java.util.ArrayList;
import Tools.Point;

public class Teenager extends Person {
	private static final long serialVersionUID = 1012022981926904899L;

	public Teenager(Person person) {

		// constructor if the person need to evolve from kid to teenager
		super(person.getPos(), person.getFirstName(), person.getLastName(), person.getGender(), person.getAge(),
				person.getMoney(), person.getMother(), person.getFather(),
				person.getPsychologicFactor());
	}

	public Teenager(Point pos, String firstName, String lastName, Gender gender, int age, int money,
			Adult mother, Adult father, ArrayList<Double> psychologicFactor) {
		// constructor if it's a new character (pnj)
		super(pos, firstName, lastName, gender, age, money, mother, father, psychologicFactor);

	}

	public void buy(TakableObject achat) {/*
		if (setMoney(-achat.getPrice())) { inventory.add(achat); }
											 */

	}

	protected void goToDrink(Person people) {
		// move to bar pay
		modifyRelationship(people, 3);
		modifyMood(automaticAnswer(people) * 25);

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

	public void work() {
		// as a teenager only "little job" available
		energy -= 20;
		modifyMoney(10);
		modifyMood(-8);
		modifyOtherVision(8);

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