package Model;

import java.awt.Color;

import Tools.Point;

public class Teenager extends Person {
	private static final long serialVersionUID = 1012022981926904899L;

	public Teenager(Person other) {
		super(other);
		
		setColor(new Color(39, 140, 247));
	}

	public Teenager(Point pos, String name, int age, Gender gender, Adult mother, Adult father)	{
		super(pos, name, age, gender, mother, father);
		
		setColor(new Color(39, 140, 247));
	}

	@Override
	public boolean maxAgeReached() {
		return (getAge() > 21);
	}
	

	public void buy(TakableObject achat) {/*
											 * if (setMoney(-achat.getPrice())) { inventory.add(achat); }
											 */

	}

	protected void goToDrink(Person people) {
		// move to bar pay
		modifyRelationship(people, 3);
		people.modifyRelationship(this, 3);
		modifyMood(automaticAnswer(people) * 25);

		energy -= 30;

	}

	protected void embrass(Person people) {
		if (automaticAnswer(people) >= 1.6) {
			modifyRelationship(people, 5);
			people.modifyRelationship(this, 5);
			modifyMood(automaticAnswer(people) * 40);
		} else {
			// the other don't want
			modifyRelationship(people, -10); // value to be adapted
			people.modifyRelationship(this, -10);
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

	@Override
	public void clickedEvent(Person person) {
		// TODO Auto-generated method stub
		
	}

}