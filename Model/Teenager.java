package Model;

import Tools.Point;

public class Teenager extends Person {
	private static final long serialVersionUID = 1012022981926904899L;

	public Teenager(Person person) {
		// constructor if the person need to evolve from kid to teenager
		super(person.getPos(), person.getName(), person.getGender(), person.getMother(), person.getFather());

		age = person.getAge();
		money = person.getMoney();
		psychologicalFactors = person.getPsychologicalFactor();
	}

	public Teenager(Point pos, String name, Gender gender, Adult mother, Adult father) {
		// constructor if it's a new character (pnj)
		super(pos, name, gender, mother, father);
	}

	public void buy(TakableObject achat) {/*
											 * if (setMoney(-achat.getPrice())) { inventory.add(achat); }
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
		modifyOthersImpression(8);

	}

	public void characterInteraction(Person people, String choice) {
		// function that allows the people to interact with another one
		// interaction is the type of interaction

		// TODO this is only to remove an error temporarily
		int energy = 0;
		switch (choice) {
		case ("discuss"): {

			if (energy >= 10) {
				discuss(people);

			} else {
				// TODO message comme quoi pas assez d'énergie
			}

			break;
		}
		case ("playWith"): {

			if (energy >= 20) {
				playWith(people);

			} else {
				// TODO message comme quoi pas assez d'énergie
			}

			break;
		}
		case ("invite"): {

			if (energy >= 25) {
				invite(people);

			} else {
				// TODO message comme quoi pas assez d'énergie
			}

			break;
		}
		case ("embrass"): {

			if (energy >= 10) {
				embrass(people);

			} else {
				// TODO message comme quoi pas assez d'énergie
			}

			break;
		}
		case ("goToDrink"): {

			if (energy >= 40) {
				goToDrink(people);

			} else {
				// TODO message comme quoi pas assez d'énergie
			}

			break;
		}

		default:
			break;
		}

	}

}