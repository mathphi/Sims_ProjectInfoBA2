package Model;

import java.awt.Color;

import Tools.Point;
import View.Message.MsgType;

public class Teenager extends Person {
	private static final long serialVersionUID = 1012022981926904899L;

	public Teenager(Person other) {
		super(other);

		setColor(new Color(39, 140, 247));
	}

	public Teenager(Point pos, String name, int age, Gender gender, Adult mother, Adult father) {
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
		modifyRelationPoints(people, 3);
		people.modifyRelationPoints(this, 3);
		modifyMood(automaticAnswer(people) * 25);

	}

	protected void embrass(Person people) {
		if (automaticAnswer(people) >= 1.6) {
			modifyRelationPoints(people, 5);
			people.modifyRelationPoints(this, 5);
			modifyMood(automaticAnswer(people) * 40);
		} else {
			// the other don't want
			modifyRelationPoints(people, -10); // value to be adapted
			people.modifyRelationPoints(this, -10);
			modifyMood(automaticAnswer(people) * -40);
		}

	}

	public void work() {
		// as a teenager only "little job" available
		energy -= 20;
		modifyMoney(10);
		modifyMood(-8);
		modifyOthersImpression(8);

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