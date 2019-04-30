package Model;

import Tools.Point;

public class Adult extends Person {
	private static final long serialVersionUID = 532161543919171452L;

	public Adult(Person person) {
		// constructor if the person need to evolve from teenager to kid
		super(person.getPos(),
			  person.getName(),
			  person.getGender(),
			  person.getMother(),
			  person.getFather());
		
		age = person.getAge();
		money = person.getMoney();
		psychologicalFactors = person.getPsychologicalFactor();
	}

	public Adult(Point pos, String name, Gender gender, Adult mother, Adult father) {
		// constructor if it's a new character
		super(pos, name, gender, mother, father);

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