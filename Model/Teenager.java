package Model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.Activable.ActionType;
import Tools.Point;
import View.Message.MsgType;

public class Teenager extends Person implements Worker {
	private static final long serialVersionUID = 1012022981926904899L;

	public boolean isWorking = false;

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
		return (getAge() > 24);
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

				// Salary factor between 1 and 2, function of the general knowledge
				double salaryFactor = 1.0 + getGeneralKnowledge();
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
	@Override
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
		default:
			break;
		}
	}
}