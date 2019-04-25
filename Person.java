//package Model;

import Tools.Point;
import Tools.Random;
import Tools.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Model.Adult;
import Model.EatableObject;
import Model.GameObject;

public abstract class Person extends GameObject {
	private static Size SIZE = new Size(1, 1);

	// general information
	protected String firstName;
	protected String lastName;
	protected Date birthDate;
	protected String gender;
	protected int money;
	protected int energy;

	// visible properties, if = 10 no need
	protected float hunger;
	protected int mood;
	protected int health;
	
	protected Person mother;
	protected Person father;

	// relation
	protected Map<Person, Integer> friendList = new HashMap<>();

	// object
	protected ArrayList<GameObject> inventoryHouse = new ArrayList<GameObject>();
	// list that contains all the object, even PNJ got one with their object
	// (cloths,...)

	public Person(Point pos, String firstName, String lastName, String gender, Date birthDate, int money,
			ArrayList<GameObject> inventoryHouse, Person mother, Person father) {

		super(pos, SIZE);

		mood = 10;
		hunger = 10;
		health = 10;

		// specific spec for character (player or not) implemented here
		this.firstName = firstName;
		this.gender = gender;
		this.lastName = lastName;
		this.birthDate = birthDate; // if PNJ birthDate is automaticately generated in Main class
		this.money = money; // player can decide to start game with some money
		this.inventoryHouse = inventoryHouse; // also depends of the players start choice

		friendList.put(father, 20);
		friendList.put(mother, 20); // considered as the higher level of relation but CAN't propose to marry, etc

		this.mother = mother;
		this.father = father;
	}

	public void move() {
		// function that allows the personnage to move
	}

	protected int getRelationship(Person friend) {
		// function who return the level of friendship by reading the hachmap with all
		// the people known
		int relationship = 0;
		if (friendList.containsKey(friend)) {
			// if the people si not in the friendList he is unknown
			int relationPoint = friendList.get(friend);

			if (relationPoint > 20) {
				relationship = 3; // serious relation, can propose to marry
			} else if (relationPoint > 10) {
				relationship = 2; // ami proche
			} else {
				relationship = 1; // just a connaissance
			}
		}
		return relationship;
	}

	protected void modifyRelationship(Person friend, int point) {
		// function that wil modify the relation ship
		// take the old value and add the new amount of point
		// if new friend, ony all "point"
		friendList.put(friend, point + friendList.get(friend));
	}

	protected void eat(EatableObject nourriture) {
		hunger += modifyHunger(nourriture);
	}

	protected void discuss(Person people) {
		modifyRelationship(people, 1);
		// TODO retirer l'energie
	}

	protected void playWith(Person people) {
		modifyRelationship(people, 2);
		// TODO retirer l'energie
	}

	protected void invite(Person people) {
		modifyRelationship(people, 3);
		// TODO bringing the people at house! et retirer de l'�nergie

	}

	public void characterInteraction(Person people) {
		// function that allows the people to interact with another one
		// interaction is the type of interaction
		// need to be overwrite in adult and teennager class for interaction with level
		// 3 friends (thing like embrass, marry,...)

		switch (getRelationship(people)) {
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

		default:
			break;
		}

	}

	// SETTER

	public void modifyProperties() {// TODO mettre properties
		energy += modifyEnergy(energy, mood, health);
		// TODO evetn ohter caracte

	}

	protected float modifyHunger(EatableObject nourriture) {
		// TODO eatable will be a interface -> need to be modify
		// Methode that will return the gain of nourriture
		float hungerGain = nourriture.getNutritionalValue();
		return hungerGain;
	}

	// TODO function that make evolvle the hunger of the player during the game

	protected static int modifyEnergy(int energy, int mood, int health) {
		// function that take in turns the health, mood and a random factor in the
		// calcul of
		// energy gain (energy = rest of the energy at the end of the day)
		// quadratic function, = 0 if health(or mood) = 10 and = need of energy/1.3
		// (1.5) if
		// health (or mood) =0
		// need to be overwrite for kid because max of energy is 8
		double energyNeed = 10 - energy; // need of energy here total is maximum 10!

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

	public String getfirstName() {
		
		return firstName;
	}

	public String getlastName() {
		
		return lastName;
	}

	public String getGender() {
	
		return gender;
	}

	public Date getBirthDate() {
		
		return birthDate;
	}

	public ArrayList<GameObject> getinventoryHouse() {
		
		return inventoryHouse;
	}

	public Person getMother() {
	
		return mother;
	}

	public Person getFather() {

		return father;
	}

	public int getMoney() {
		
		return money;
	}
	public int getEnergy() {
		return energy;
	}



}
