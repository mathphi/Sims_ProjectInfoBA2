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
	
	// The partner if married
	private Adult partner = null;

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
			if (getPartner() != null) {
				if (getPartner() == other) {
					// Return the married status if it's my partner
					relationship = Relationship.Married;
				}
				else {
					// I don't want a very serious relation if I'm married
					relationship = Relationship.SeriousRelation;
				}
			}
		}
		
		return relationship;
	}

	public void setPartner(Adult partner) {
		this.partner = partner;
		friendList.put(partner, 100.0);
	}
	
	public Adult getPartner() {
		return partner;
	}
	
	private void marry(Person people) {
		// Only adults can marry...
		if (!(people instanceof Adult))
			return;
		
		Adult a = (Adult) people;

		if (!useEnergy(15))
			return;
		
		// Send message to other
		a.addMessageFrom(this, "Veux tu m'épouser ?", MsgType.Info);
		
		// The other is already married !
		if (a.getPartner() != null) {
			addMessageFrom(a, "Euh... Je suis déjà marié !", MsgType.Info);
			return;
		}
		
		double appreciationFactor = 0.0;
		
		// Both must want to marry to apply...
		if (getAppreciationOf(a) > 0.8 && a.getAppreciationOf(this) > 0.8) {
			setPartner(a);
			a.setPartner(this);
			
			appreciationFactor = 1.0;

			addMessage("Vous êtes maintenant marié à " + a.getName(), MsgType.Info);
			a.addMessage("Vous êtes maintenant marié à " + this.getName(), MsgType.Info);
		}
		else {
			appreciationFactor = -0.4;
		}

		modifyRelationPoints(people, 40 * appreciationFactor);
		people.modifyRelationPoints(this, 40 * appreciationFactor);
		modifyMood(50 * appreciationFactor);
		
		a.automaticAnswer(this, InteractionType.Marry, appreciationFactor);
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