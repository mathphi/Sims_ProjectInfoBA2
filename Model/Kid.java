package Model;

import java.util.ArrayList;
import java.util.Date;

import Tools.Random;
import Tools.Point;

public class Kid extends Person {

	public Kid(Point pos, String firstName, String lastName, String gender, Date birthDate, int money,
			ArrayList<GameObject> inventoryHouse, Adult mother, Adult father) {
		// only one constructor because it is the first stade of life
		// all that parameters are implemented in Person class
		// if PNJ firstName, lastName, birthDate, moneyn inventoryHouse are generated
		// randomly
		super(pos, firstName, lastName, gender, birthDate, money, inventoryHouse, mother, father);
		energy = 8; // as a kid got less energy accessible

	}

	// ACTION

	protected static int modifyEnergy(int energy, int mood, int health) {
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
	
	//TODO function that make evolve properties during the game

	public boolean isObstacle() {
		return true;
	}
}
