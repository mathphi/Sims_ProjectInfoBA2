package Model;

import Tools.Point;
import Tools.Random;
import Tools.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Person extends GameObject implements Directable {
	private static Size SIZE = new Size(1, 1);

	private int direction = EAST;

	// general information
	protected String firstName;
	protected String lastName;
	protected Date birthDate;
	protected String gender;
	protected int money;
	protected static int energy;

	// visible properties, if = 100 no need
	protected float hunger;
	protected static int mood;
	protected static int health;

	protected Adult mother;
	protected Adult father;

	// relation
	protected Map<Person, Integer> friendList = new HashMap<>();

	// object
	protected ArrayList<GameObject> inventoryHouse = new ArrayList<GameObject>();
	// list that contains all the object, even PNJ got one with their object
	// (cloths,...)

	public Person(Point pos, String firstName, String lastName, String gender, Date birthDate, int money,
			ArrayList<GameObject> inventoryHouse, Adult mother, Adult father) {

		super(pos, SIZE);
		energy = 100;
		mood = 100;
		hunger = 100;
		health = 100;

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
	}

	public void move(Point p) {
		this.setPos(this.getPos().add(p));
	}

	public void rotate(Point p) {
		if (p.getX() == 0 && p.getY() == -1)
			direction = NORTH;
		else if (p.getX() == 0 && p.getY() == 1)
			direction = SOUTH;
		else if (p.getX() == 1 && p.getY() == 0)
			direction = EAST;
		else if (p.getX() == -1 && p.getY() == 0)
			direction = WEST;
	}

	public int getDirection() {
		return direction;
	}

	public boolean isObstacle() {
		return true;
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
		energy -= 1;
	}

	protected void playWith(Person people) {
		modifyRelationship(people, 2);
		energy -= 2;
	}

	protected void invite(Person people) {
		modifyRelationship(people, 3);
		energy -= 3;
		// TODO bringing the people at house!

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



	protected float modifyHunger(EatableObject nourriture) {
		// TODO eatable will be a interface -> need to be modify
		// Methode that will return the gain of nourriture
		float hungerGain = nourriture.getNutritionalValue();
		return hungerGain;
	}

	// TODO function that make evolvle the hunger of the player during the game

	protected static void modifyEnergy() {
		// function that take in turns the health, mood and a random factor in the
		// calcul of energy
		//will be run when player go to bed

		double moodHealthFactor = Math.pow(Math.E, health * Math.log(2) * mood / 10000) - 1; // always beetween 0 and 1
		double randomFactor = (Random.range(1, (int) Math.round(Math.log(80))));
		randomFactor = (1 - Math.pow(Math.E, randomFactor) / 80);

		double energyAdd = 100 * moodHealthFactor * randomFactor;
		if (energyAdd < 20) {
			// minimum of energy
			energyAdd = 20;
		}

		energy = (int) (energyAdd);
	}

	protected static void modifyMood(double augmentation) {
		
		// fonction that increase the mood after the activity like going out,...
		double maxMood = 100 - mood; // number of max point
		if (maxMood < augmentation) {
			// check that the gain of mood is < max
			augmentation = maxMood;
		}
		double moodFactor = Math.pow(Math.E, -mood / 100);
		double randomFactor = (Random.range(1, (int) Math.round(Math.log(80))));
		randomFactor = (1 - Math.pow(Math.E, randomFactor) / 80);
		augmentation = randomFactor * augmentation * moodFactor;

		mood += (int) (augmentation);
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

	public Adult getMother() {

		return mother;
	}

	public Adult getFather() {

		return father;
	}

	public int getMoney() {

		return money;
	}

	public void modifyCaracteristic() {
		// TODO will be overwrite after, change the mood, hunger,...
	}

	public int getEnergy() {

		return energy / 100;
	}

	public int getMood() {
		return mood / 100;
	}

	public int getHealth() {
		return health / 100;
	}

}
