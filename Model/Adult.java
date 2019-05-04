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

	private void marry(Person people) {
		/*
		 * TODO big todo to do including to do what's suppose to be done
		 * because it should already have be done as it was to be done
		 */

		if (people.getAppreciationOf(this) >= 0.9) {
			if (!useEnergy(15))
				return;
		
			applyInteractionEffect(people, 40, 50);
		} else {
			// The other don't want
			applyRejectedEffect(people, 20, 40);
		}
	}

	private void drinkWith(Person people) {
		if (people.getAppreciationOf(this) >= 0.5) {
			if (!useEnergy(40))
				return;
		
			applyInteractionEffect(people, 20, 30);
			
			//TODO move to bar pay
		} else {
			// The other don't want
			applyRejectedEffect(people, 10, 10);
		}
	}

	private void kiss(Person people) {	
		if (people.getAppreciationOf(this) >= 0.7) {
			if (!useEnergy(5))
				return;
		
			applyInteractionEffect(people, 30, 40);
		} else {
			// The other don't want
			applyRejectedEffect(people, 15, 30);
		}
	}

	/**
	 * Function that allows the people to interact with another one.
	 * 
	 * @param other
	 * The other people with which to interact
	 * 
	 * @param interaction The type of interaction
	 */
	public void characterInteraction(Person other, InteractionType interaction) {	
		switch (interaction) {
		case Discuss:
			discuss(other);
			break;
		case Play:
			playWith(other);
			break;
		case Invite:
			invite(other);
			break;
		case Kiss:
			kiss(other);
			break;
		case Drink:
			drinkWith(other);
			break;
		case Marry:
			marry(other);
			break;
		default:
			break;
		}
	}
}