package Model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import Tools.Point;
import View.Message.MsgType;

public class Adult extends Person implements Worker {
	private static final long serialVersionUID = 532161543919171452L;

	public boolean isWorking = false;
	public LocalDateTime lastWorkTime = LocalDateTime.now().minusDays(1);

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

	@Override
	public boolean isWorking() {
		return isWorking;
	}

	@Override
	public void work(int energyImpact, int moodImpact, int salary, int duration) {
		if (useEnergy(energyImpact))
			return;
		
		isWorking = true;
		setLocked(true);
		
		WaiterThread.wait(duration, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isWorking = false;
				setLocked(false);
				resetLastActionTime(ActionType.Work);
				
				modifyMoney(salary);
				modifyMood(-moodImpact);
				modifyOthersImpression(Math.abs(moodImpact * 1.5));

				addMessage(
						String.format("Vous venez de gagner %d€ en travaillant", salary),
						MsgType.Info);
			}
		});
	}
	
	/**
	 * This function return the level of friendship by parsing the relation points.
	 * 
	 * @param other
	 * @return
	 */
	public Relationship getRelationship(Person other) {
		Relationship relationship = super.getRelationship(other);
		
		if (relationship == Relationship.VerySeriousRelation) {
			//TODO: Check if i'm married ->
			// 		if true we don't want a VerySeriousRelation (disable marry interaction)
			
			// relationship = Relationship.SeriousRelation
		}
		
		return relationship;
	}

	private void marry(Person people) {
		/*
		 * TODO big todo to do including to do what's suppose to be done
		 * because it should already have be done as it was to be done
		 */
		
		//TODO: add a condition if already married (the other person refuses)

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