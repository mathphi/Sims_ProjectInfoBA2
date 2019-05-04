package Model;

import java.awt.Color;

import Tools.Point;
import View.Message.MsgType;

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
		// TODO: define a random maxAge for an adult after which he dies ?
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
	

	}

	public void characterInteraction(Person otherPeople, String interaction) {
		boolean action = false;

		switch (interaction) {
		case ("discuss"):
			if (modifyEnergy(-10)) {
				discuss(otherPeople);
				action = true;
			}
			break;
		case ("playWith"):
			if (modifyEnergy(-20)) {
				playWith(otherPeople);
				action = true;

			}

			break;
		case ("invite"):
			if (modifyEnergy(-25)) {
				invite(otherPeople);
				action = true;
			}

			break;

		case ("embrass"):

			if (modifyEnergy(-15)) {
				embrass(otherPeople);
				action = true;
			}
			break;

		case ("goToDrink"):

			if (modifyEnergy(-40)) {

				goToDrink(otherPeople);
				action = true;
			}
			break;

		case ("marry"):

			if (modifyEnergy(-10)) {
				marry(otherPeople);
				action = true;
			}

			break;

		default:
			break;
		}

		/*
		 * TODO: I don't understand why we call automaticAnswer twice (in the action's
		 * functions and here). Also it might be good to move this section in a
		 * separated function called by the action's target functions.
		 */

		// TODO yes i'm working on it
		if (action) {
			double value = automaticAnswer(otherPeople);

			if (value > 0.8) {
				// second condition for not double printing
				addMessage(otherPeople.getName()
						+ ": C'était vraiment un chouette moment! Tu es hyper sympathique et incroyable merci pour tout!",
						MsgType.Info);
			} else if (value > 0.6) {
				addMessage(
						otherPeople.getName() + ": Je n'avais rien d'autre à faire mais c'était cool d'être avec toi ",
						MsgType.Info);
			} else if (value > 0.5) {
				addMessage(otherPeople.getName() + ": Bon... Content de t'avoir vu. ", MsgType.Info);
			} else if (value > 0.3) {
				addMessage(otherPeople.getName() + ": Je me suis ennuyé j'aurais pas du venir ", MsgType.Info);
			} else {
				addMessage(otherPeople.getName() + ": T'es vraiment pas sympathique, me recontacte plus jamais! ",
						MsgType.Info);
			}
		}
	}
}