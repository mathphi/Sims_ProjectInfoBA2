package Model;

import java.awt.Color;

import Tools.Point;

public class Adult extends Person {
	private static final long serialVersionUID = 532161543919171452L;

	public Adult(Person other) {
		super(other);
		
		setColor(new Color(39, 80, 247));
	}

	public Adult(Point pos, String name, int age, Gender gender, Adult mother, Adult father) {
		super(pos, name, age, gender, mother, father);
		
		setColor(new Color(39, 80, 247));		
	}

	@Override
	public boolean maxAgeReached() {
		//TODO: define a random maxAge for an adult after which he dies ?
		return (getAge() > 90);
	}

	public void work() {
		// work depends of the level of study
		// TODO need to make the choice!
		int levelWork = 3;

		if (generalKnowledge > 80) {
			levelWork = 3;
		} else if (generalKnowledge > 50) {
			levelWork = 2;
		} else if (generalKnowledge > 20) {
			levelWork = 1;

		}
		switch (levelWork) {
		case (0): {
			// only 1 job availlable
			energy -= 20;
			modifyMoney(10);
			modifyMood(-15);
			modifyOthersImpression(5);

		}
		case (1): {
			int job = 1; // TODO ask the payer wich job he wants (0,1)
			switch (job) {
			case (0): {
				// basic job
				energy -= 20;
				modifyMoney(10);
				modifyMood(-15);
				modifyOthersImpression(5);
			}
			case (1): {
				energy -= 25;
				modifyMoney(20);
				modifyMood(-15);
				modifyOthersImpression(8);

			}
			}
		}
		case (2): {
			int job = 1; // TODO ask the payer wich job he wants (0,1,2)
			switch (job) {
			case (0): {
				// basic job
				energy -= 20;
				modifyMoney(10);
				modifyMood(-15);
				modifyOthersImpression(5);
			}
			case (1): {
				energy -= 25;
				modifyMoney(20);
				modifyMood(-15);
				modifyOthersImpression(8);

			}
			case (2): {
				energy -= 30;
				modifyMoney(30);
				modifyMood(-12);
				modifyOthersImpression(10);

			}
			}
		}

		case (3): {
			int job = 1; // TODO ask the payer wich job he wants (0,1,2)
			switch (job) {
			case (0): {
				// basic job
				energy -= 20;
				modifyMoney(10);
				modifyMood(-15);
				modifyOthersImpression(5);
			}
			case (1): {
				energy -= 25;
				modifyMoney(20);
				modifyMood(-15);
				modifyOthersImpression(8);

			}
			case (2): {
				energy -= 30;
				modifyMoney(30);
				modifyMood(-12);
				modifyOthersImpression(10);

			}

			case (3): {
				energy -= 35;
				modifyMoney(50);
				modifyMood(-10);
				modifyOthersImpression(15);

			}
			}

		}
		}
	}

	private void marry(Person partner) {
		// bien jou� morray
		// TODO big todo to do including to do what's suppose to be done because it
		// should already have be done as it was to be done
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

	public void characterInteraction(Person people, String choice) {
		// function that allows the people to interact with another one
		// interaction is the type of interaction
		switch (choice) {
		case ("discuss"): {

			if (energy >= 10) {
				discuss(people);

			} else {
				// TODO message comme quoi pas assez d'énergie
			}

			break;
		}
		case ("play"): {

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

		case ("marry"): {

			if (energy >= 10) {
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