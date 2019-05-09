package Model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import Tools.Point;
import Tools.Random;
import View.Message.MsgType;

public class Adult extends Person implements Worker {
	private static final long serialVersionUID = 532161543919171452L;
	
	// Random maxAge between 80 and 100
	private int maxAge = 80 + Random.rangeInt(0, 20);

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
		return (getAge() > maxAge);
	}

	@Override
	public boolean isWorking() {
		return isWorking;
	}

	@Override
	public void work(int energyImpact, int moodImpact, int salary, int duration) {
		if (!useEnergy(energyImpact))
			return;
		
		isWorking = true;
		setLocked(true);
		
		WaiterThread.wait(duration, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isWorking = false;
				setLocked(false);
				resetLastActionTime(ActionType.Work);
				
				// Salary factor between 1 and 4, function of the general knowledge
				// More for an adult than a teenager
				double salaryFactor = 1.0 + getGeneralKnowledge() * 3.0;
				int scaledSalary = (int)(salary * salaryFactor); 
				
				modifyMoney(scaledSalary);
				modifyMood(moodImpact);
				modifyOthersImpression(Math.abs(moodImpact * 1.5));

				addMessage(
						String.format("Vous venez de gagner %d€ en travaillant", scaledSalary),
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
	@Override
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

		if (!useEnergy(15))
			return;
		
		applyInteractionEffect(people, InteractionType.Marry, 40, 50);
	}

	private void drinkWith(Person people) {
		if (!useEnergy(40))
			return;
		
		applyInteractionEffect(people, InteractionType.Drink, 20, 30);
	}

	private void kiss(Person people) {	
		if (!useEnergy(5))
			return;
		
		applyInteractionEffect(people, InteractionType.Kiss, 30, 40);
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