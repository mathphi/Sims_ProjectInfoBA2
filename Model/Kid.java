package Model;

import java.util.ArrayList;
import java.util.Date;

import Tools.Random;
import Tools.Point;

public class Kid extends Person {
	private float hunger;
	// protected int energy;
	private int mood;
	private int health;
	private int year;
	private String type;

	public Kid(Point pos, String firstName, String lastName, String gender) {
		// lot of information are decided in upper class
		super(pos, firstName, lastName, gender);
		energy = 8; // as a kid got less energy accessible

	}

	// ACTION
	protected void eat(EatableObject nourriture) {
		hunger += setHunger(nourriture);
	}

	protected void discuss(Person people) {
		people.setRelationship(1);
		// TODO retirer l'energie
	}

	protected void playWith(Person people) {
		people.setRelationship(2);
		// TODO retirer l'energie
	}

	protected void invite(Person people) {
		people.setRelationship(3);
		// TODO bringing the people at house! et retirer de l'�nergie

	}

	public void characterInteraction(Person people) {
		// function that allows the people to interact with another one
		// interaction is the type of interaction

		switch (people.getRelationship()) {
		case (0): {
			// can only discuss
			// TODO interface graphique: les diff�rentes possibilit�s!
			if (energy >= 1) {
				discuss(people);
				// TODO augmenter le mood
			}

			break;
		}
		case (1): {
			if (energy >= 1) {
				discuss(people);
				// TODO augmenter le mood
				energy -= 1; // on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}

			break;
		}
		case (2): {
			if (energy >= 1) {
				discuss(people);
				// TODO augmenter le mood
				energy -= 1;// on peut faire ainsi ou il faut faire par un setter?
			}

			if (energy >= 2) {
				playWith(people);
				// TODO augmenter le mood
				energy -= 2;
			}
			if (energy >= 4) {
				invite(people);
				// TODO augmenter le mood
				energy -= 3;
			}

			break;
		}

		// not a case 3 because can marry at that age
		default:
			break;
		}

	}

	// SETTER

	public void SetCaracteristic() {// TODO mettre properties
		energy += setEnergy(energy, mood, health);
		// TODO evetn ohter caracte

	}

	protected float setHunger(EatableObject nourriture) {// TODO pas set et pas etable objet but object
		// Methode that will return the gain of nourriture
		float hungerGain = nourriture.getNutritionalValue();
		// TODO calcul compliqu� qui va prendre en compte l'�tat de faim atuel du joueur
		// et si mange de trop sant� baisse
		return hungerGain;
	}

	protected static int setEnergy(int energy, int mood, int health) {
		// function that take in turns the health, mood and a random factor in the
		// calcul of
		// energy gain (energy = rest of the energy at the end of the day)
		// quadratic function, = 0 if health(or mood) = 10 and = need of energy/1.3
		// (1.5) if
		// health (or mood) =0
		double energyNeed = 8 - energy; // need of energy here total is maximum 8!

		double randomFactor = (Random.range(1, (int) Math.round(Math.log(60))));
		randomFactor = (1 - Math.pow(Math.E, randomFactor) / 60); // on average = 0.7
		energyNeed -= randomFactor * 2;

		double factHealth = -Math.sqrt(energyNeed * 1.3) * health / 10 + Math.sqrt(energyNeed * 1.3);
		factHealth = 0.5 * Math.pow(factHealth, 2);// ponderation of the health
		energyNeed -= factHealth;

		double factMood = -Math.sqrt(energyNeed / 1.5) * mood / 10 + Math.sqrt(energyNeed / 1.5);
		factMood = 0.4 * Math.pow(factMood, 2);
		energyNeed -= factMood;

		energyNeed = Math.round(energyNeed); // round of the energy

		return (int) energyNeed; // return of the gain of energy
	}
	
	public boolean isObstacle() {
		return true;
	}
}
