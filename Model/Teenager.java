package Model;

import java.awt.Color;

import Tools.Point;

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

	public void work() {
		// as a teenager only "little job" available
		energy -= 20;
		modifyMoney(10);
		modifyMood(-8);
		modifyOthersImpression(8);

	}

	private void drinkWith(Person people) {
		if (people.getAppreciationOf(this) >= 0.3) {
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
		if (people.getAppreciationOf(this) >= 0.6) {
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
	@Override
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
		default:
			break;
		}
	}
}