package Model;

import Tools.Point;
import Tools.Random;
import Tools.Size;

import java.awt.Color;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public abstract class Person extends GameObject implements Directable {
	private static Size SIZE = new Size(1, 1);

	private int direction = EAST;

	// general information
	protected String firstName;
	protected String lastName;
	protected int age;
	protected String gender;
	protected int money;
	protected int energy;

	// visible properties, if = 100 no need
	protected float hunger;
	protected int mood;
	protected int health;
	protected int bladder;

	// automatic answer parameters
	protected int generalKnowledge; // caracterise the knwoledge of the player -> increase by going to school or
									// thing like that
	protected int otherVision; // caracterise the vision of other, if often go out, is in good health,... ->
								// higher

	protected ArrayList<Double> psychologicFactor;
	// list of 4 int from 0 to 25 to caracterise the importance of mood, health
	// generalKnwoledge and ohtervision (in that order) for the automatic answer
	// will be randomly generated for PNJ

	protected double relationFactor;

	// relation
	protected Map<Person, Integer> friendList = new HashMap<>();
	protected Adult mother;
	protected Adult father;

	// object
	protected ArrayList<GameObject> inventoryHouse = new ArrayList<GameObject>();
	// list that contains all the object, even PNJ got one with their object
	// (cloths,...)

	public Person(Point pos, String firstName, String lastName, String gender, int age, int money,
			ArrayList<GameObject> inventoryHouse, Adult mother, Adult father, ArrayList<Double> psychologicFactor) {

		super(pos, SIZE, Color.BLUE);
		energy = 100;
		mood = 100;
		hunger = 100;
		health = 100;
		bladder = 100;

		// maximum is 100
		generalKnowledge = 50;
		otherVision = 50;
		this.firstName = firstName;
		this.gender = gender;
		this.lastName = lastName;
		this.age = age; // = 10 if player character
		this.money = money; // player can decide to start game with some money
		this.inventoryHouse = inventoryHouse; // also depends of the players start choice

		this.psychologicFactor = psychologicFactor;

		friendList.put(father, 20);
		friendList.put(mother, 20); // considered as the higher level of relation but CAN't propose to marry, etc

		this.mother = mother;
		this.father = father;
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
		// automaticAnswer is a multiplicatory factor, is ~2 if the character are really
		// complementary
		// or is <0.5 if the character are realy not the same
		if (point < 0 & friendList.get(friend) < -point) {
			// there is not enough point
			// they became unknown people again
			friendList.remove(friend);
		} else {
			friendList.put(friend, (int) (point * automaticAnswer(friend) + friendList.get(friend)));
		}
	}

	protected double automaticAnswer(Person people) {
		// function that return a number beetween 0 and 1
		// if 1 the 2 characters are realy complementary
		// if 0 they haven't the same will
		// take the caracterisic of the player that sent the request mutliply by the
		// factor from the recever
		double relationFactor = mood * people.getPsychologicFactor().get(0)
				+ health * people.getPsychologicFactor().get(1)
				+ generalKnowledge * people.getPsychologicFactor().get(2)
				+ otherVision * people.getPsychologicFactor().get(3);
		relationFactor /= 100000;
		if (relationFactor > 0.7) {
			// will multipy by 2 the gain of mood and point relation because realy
			// complementary
			relationFactor *= 2;
		} else if (relationFactor < 0.3) {
			// the person are realy not the same -> mood and point of relation need to be
			// reduce
			relationFactor *= -1;
		}

		// TODO indicate in function of the value if the people like a lot, a little or
		// realy not
		return relationFactor;
	}

	protected void eat(EatableObject nourriture) {
		float hungerGain = nourriture.getNutritionalValue();
		hunger += (int) (hungerGain);
		if (hunger > 100) {
			// too much point
			hunger = 100;
		}
	}

	protected void discuss(Person people) {
		modifyRelationship(people, 1);
		modifyMood(automaticAnswer(people) * 15);
		energy -= 10;
	}

	protected void playWith(Person people) {
		modifyRelationship(people, 2);
		modifyMood(automaticAnswer(people) * 20);
		energy -= 20;
	}

	protected void invite(Person people) {

		modifyRelationship(people, 3);
		modifyMood(automaticAnswer(people) * 30);
		energy -= 25;
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
			if (energy >= 10) {
				discuss(people);

			}

			break;
		}
		case (1): {
			if (energy >= 10) {
				discuss(people);

			}

			if (energy >= 20) {
				playWith(people);

			}

			break;
		}
		case (2): {
			if (energy >= 10) {
				discuss(people);

			}

			if (energy >= 20) {
				playWith(people);

			}
			if (energy >= 25) {
				invite(people);

			}

			break;
		}

		default:
			break;
		}

	}

	// TODO function that make evolvle the hunger of the player during the game
	protected void goToBed() {
		// TODO make the player go to bed
		age += 1;

	}

	protected void modifyMoney(int amount) {
		// if need to pay -> amount <0
		// if it's a gain -> amount >0
		money += amount;
	}

	protected void modifyEnergy() {
		// function that take in turns the health, mood and a random factor in the
		// calcul of energy
		// will be run when player go to bed

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

	protected void modifyMood(double value) {

		// fonction that increase the mood after the activity like going out,...
		double maxMood = 100 - mood; // number of max point
		if (value > 0) {
			if (maxMood < value) {
				// check that the gain of mood is < max
				value = maxMood;
			}
			double moodFactor = Math.pow(Math.E, -mood / 100);
			double randomFactor = (Random.range(1, (int) Math.round(Math.log(80))));
			randomFactor = (1 - Math.pow(Math.E, randomFactor) / 80);
			value = randomFactor * value * moodFactor;
		} else if (-value > mood) {
			// check the reduction isn't bigger of the amount of mood available
			value = -mood;
		}

		mood += value;
	}

	protected void modifyOtherVision(double value) {

		// function that modify the vision of the character
		double maxVision = 100 - otherVision; // number of max point
		if (value > 0) {
			if (maxVision < value) {
				// check that the gain of mood is < max
				value = maxVision;
			}

			double randomFactor = (Random.range(1, (int) Math.round(Math.log(80))));
			randomFactor = (1 - Math.pow(Math.E, randomFactor) / 80);
			value = randomFactor * value;
		} else if (-value > otherVision) {
			// check the reduction isn't bigger of the amount of mood available
			value = -otherVision;
		}
		otherVision += value;

	}

	public void parametrizePsychoFactor() {
		// need to be sure that the sums of the 4 factors (beetween 0 and 25 each) is =
		// 100
		double sum = 0;
		ArrayList<Double> newPsychoFactor = new ArrayList<Double>();

		for (double factor : psychologicFactor) {
			sum += factor;
		}
		for (double factor : psychologicFactor) {
			newPsychoFactor.add(factor * 100 / sum);
		}
		psychologicFactor = newPsychoFactor;

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

	public int getAge() {

		return age;
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

	public int getEnergy() {

		return energy / 100;
	}

	public int getMood() {
		return mood / 100;
	}

	public int getHealth() {
		return health / 100;
	}

	public ArrayList<Double> getPsychologicFactor() {
		return psychologicFactor;
	}
}
