package Model;

import Tools.Point;
import Tools.Random;
import Tools.Size;
import View.Message;
import View.Message.MsgType;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import Products.Book;
import Products.Nourriture;
import Products.Product;
import Products.Toy;
import Products.Wearable;

public abstract class Person extends GameObject {
	private static final long serialVersionUID = 8476495059211784395L;

	public enum Gender {
		Male,
		Female
	}

	private static Size SIZE = new Size(1, 1);

	protected boolean isActivePerson;

	// general information
	protected String name;
	protected int age;
	protected Gender gender;
	protected boolean isPlayable = true;

	protected int money;

	// visible properties, if = 100 no need
	protected double energy;
	protected float hunger;
	protected double mood;
	protected double hygiene;
	protected double bladder;

	// Automatic answer attributes
	// Represents the knowledge of the player -> increase by going to school or thing like that
	protected int generalKnowledge;
	
	// Represents the impression of other, if often go out, is in good hygiene,... -> higher
	protected int othersImpression; 

	// Represents the importance of mood, hygiene, generalKnwoledge and ohtersImpression
	// for the automatic answers. Will be randomly generated for PNJ
	protected PsychologicalFactors psychologicalFactors;

	protected double relationFactor;
	protected ArrayList<Product> inventory;
	// relation
	protected Map<Person, Integer> friendList = new HashMap<>();
	protected Adult mother;
	protected Adult father;

	// Random factors to differentiate the avolution of each person (one is hungry more often,...)
	private double bladderRandomFactor = Random.range(0.6, 1.2);
	private double hungerRandomFactor = Random.range(0.6, 1.2);
	private double energyRandomFactor = Random.range(0.6, 1.2);

	// Game messages history
	protected ArrayList<Message> messagesHistory = new ArrayList<Message>();
	private transient ArrayList<MessageEventListener> msgListeners = new ArrayList<MessageEventListener>();

	public Person(Point pos, String name, Gender gender, Adult mother, Adult father)
	{
		super(pos, SIZE, Color.BLUE);

		// Initial Person properties (maximum is 100)
		energy = 100;
		mood = 100;
		hunger = 100;
		hygiene = 100;
		bladder = 100;
		
		generalKnowledge = 50;
		othersImpression = 50;
		
		this.name = name;
		this.gender = gender;

		this.psychologicalFactors = PsychologicalFactors.RandomFactors();

		friendList.put(father, 20);
		friendList.put(mother, 20); // considered as the higher level of relation but CAN't propose to marry, etc

		this.mother = mother;
		this.father = father;

		inventory = new ArrayList<Product>();
	}

	public void clickedEvent() {
		// TODO
	}
	
	public void proximityEvent(GameObject o) {
		// TODO
	}

	public void setActivePerson(boolean is_active) {
		isActivePerson = is_active;
	}

	public boolean isActivePerson() {
		return isActivePerson;
	}

	public void move(Point p) {
		this.setPos(this.getPos().add(p));
	}

	public boolean isObstacle() {
		return true;
	}

	public void update(long t) {
		// New year
		if (t % 365 == 0) {
			evolves();
		}
		// New week (7 days...)
		if (t % 7 == 0) {
			increaseNeeds();
		}
	}
	
	public void increaseNeeds() {
		decreaseBladder(Random.range(2.0, 3.0) * bladderRandomFactor); // Random decrease
		modifyHunger(Random.range(-1.0, -2.0) * hungerRandomFactor); // Random decrease
		
		// Decrease more energy if hygiene is low
		modifyEnergy((hygiene >= 20 ? -1 : -3) * energyRandomFactor);
	}
	
	public void evolves() {
		age++;
		
		//TODO: check if we have the age to evolve to next class
	}

	public int getRelationship(Person friend) {
		// function who return the level of friendship by reading the hashmap with all
		// the people known
		int relationship = 0;
		if (friendList.containsKey(friend)) {
			// if the people is not in the friendList he is unknown
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

	public void modifyRelationship(Person friend, int point) {
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

	public double automaticAnswer(Person people) {
		// function that return a number beetween 0 and 1
		// if 1 the 2 characters are realy complementary
		// if 0 they haven't the same will
		// take the caracterisic of the player that sent the request mutliply by the
		// factor from the recever
		double relationFactor = 
				mood * people.getPsychologicalFactor().getMood()
				+ hygiene * people.getPsychologicalFactor().getHygiene()
				+ generalKnowledge * people.getPsychologicalFactor().getGeneralKnowledge()
				+ othersImpression * people.getPsychologicalFactor().getOthersImpression();
	
		relationFactor /= 100.0;
		
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

	public void eat(Nourriture nourriture) {
		float hungerGain = nourriture.getNutritionalValue();
		hunger += (int) (hungerGain);
		if (hunger > 100) {
			// too much point
			hunger = 100;
		}
		modifyEnergy(-nourriture.getEnergyNeed());
	}

	public void discuss(Person people) {
		modifyRelationship(people, 1);
		modifyMood(automaticAnswer(people) * 15);
		energy -= 10;
	}

	public void playWith(Person people) {
		modifyRelationship(people, 2);
		modifyMood(automaticAnswer(people) * 20);
		energy -= 20;
	}

	public void invite(Person people) {

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
			// TODO interface graphique: les différentes possibilités!
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

	// TODO function that make evolve the hunger of the player during the game
	public void goToBed() {
		// TODO make the player go to bed

	}

	/**
	 * Decrease the bladder of the factor and check if the bladder full If the
	 * bladder is full, call emptyBladder()
	 * 
	 * @param factor
	 */
	public void decreaseBladder(double factor) {
		bladder -= factor;
		bladder = Math.max(0, Math.min(bladder, 100));

		if (bladder <= 0) {
			boolean isOnToilet = false;

			for (GameObject o : getObjectsAround()) {
				if (o instanceof WaterClosed) {
					isOnToilet = true;
				}
			}
			
			emptyBladder(isOnToilet);
		}
	}

	/**
	 * In short, he piss... We just have to check if the person piss in a toilet or
	 * just... on himself The player will lose hygiene,... in the second case
	 */
	public void emptyBladder(boolean isOnToilet) {
		bladder = 100;
		
		if (!isOnToilet) {
			addMessage(new Message("Too late...", MsgType.Problem)); //TODO: I have no idea for this text...
			modifyHygiene(-50);
			modifyMood(-25);
		}
	}

	/**
	 * Modify the hygiene of a factor If the hygiene is too low... (?) TODO
	 * 
	 * @param factor
	 */
	public void modifyHygiene(double factor) {
		hygiene += factor;
		hygiene = Math.max(0, Math.min(hygiene, 100));

		// TODO: consequence if hygiene <= 0
	}

	public void modifyHunger(double factor) {
		hunger += factor;
		hunger = Math.max(0, Math.min(hunger, 100));

		// TODO: conditional consequences
		if (hunger <= 0) {
			modifyHygiene(-10);
			modifyMood(-10);
		}
	}

	public void modifyEnergy(double factor) {
		energy += factor;
		energy = Math.max(0, Math.min(energy, 100));
	}

	public void restoreEnergy() {
		// function that take in turns the hygiene, mood and a random factor in the
		// calcul of energy
		// will be run when player go to bed

		double moodHygieneFactor = Math.pow(Math.E, hygiene * Math.log(2) * mood / 10000.0) - 1; // always beetween 0
																									// and 1
		double randomFactor = (Random.range(1, (int) Math.round(Math.log(80))));
		randomFactor = (1 - Math.pow(Math.E, randomFactor) / 80.0);

		double energyAdd = 100 * moodHygieneFactor * randomFactor;
		if (energyAdd < 20) {
			// minimum of energy
			energyAdd = 20;
		}

		energy = (int) (energyAdd);
	}

	public void modifyMoney(int amount) {
		// if need to pay -> amount <0
		// if it's a gain -> amount >0
		money += amount;
	}

	public void modifyMood(double value) {

		// fonction that increase the mood after the activity like going out,...
		double maxMood = 100 - mood; // number of max point
		if (value > 0) {
			if (maxMood < value) {
				// check that the gain of mood is < max
				value = maxMood;
			}
			double moodFactor = Math.pow(Math.E, -mood / 100.0);
			double randomFactor = (Random.range(1, (int) Math.round(Math.log(80))));
			randomFactor = (1 - Math.pow(Math.E, randomFactor) / 80.0);
			value = randomFactor * value * moodFactor;
		} else if (-value > mood) {
			// check the reduction isn't bigger of the amount of mood available
			value = -mood;
		}

		mood += value;
	}

	public void modifyOthersImpression(double value) {

		// function that modify the vision of the character
		double maxVision = 100 - othersImpression; // number of max point
		if (value > 0) {
			if (maxVision < value) {
				// check that the gain of mood is < max
				value = maxVision;
			}

			double randomFactor = (Random.range(1, (int) Math.round(Math.log(80))));
			randomFactor = (1 - Math.pow(Math.E, randomFactor) / 80.0);
			value = randomFactor * value;
		} else if (-value > othersImpression) {
			// check the reduction isn't bigger of the amount of mood available
			value = -othersImpression;
		}
		othersImpression += value;

	}

	public String getName() {
		return name;
	}

	public Gender getGender() {
		return gender;
	}

	public int getAge() {
		return age;
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

	public double getEnergy() {
		return energy / 100.0;
	}

	public double getMood() {
		return mood / 100.0;
	}

	public double getHygiene() {
		return hygiene / 100.0;
	}

	public double getBladder() {
		return bladder / 100.0;
	}

	public double getHunger() {
		return hunger / 100.0;
	}
	
	public void setPlayable(boolean playable) {
		isPlayable = playable;
	}
	
	public boolean isPlayable() {
		return isPlayable;
	}

	public PsychologicalFactors getPsychologicalFactor() {
		return psychologicalFactors;
	}

	public ArrayList<Message> getMessagesHistory() {
		return messagesHistory;
	}

	public void addMessageEventListener(MessageEventListener mel) {
		// msgListeners is null when Person is restored from the save file
		if (msgListeners == null) {
			msgListeners = new ArrayList<MessageEventListener>();
		}

		msgListeners.add(mel);
	}

	public void addMessage(Message msg) {
		messagesHistory.add(msg);

		for (MessageEventListener mel : msgListeners) {
			mel.messageEvent(msg);
		}
	}

	public void addInventory(Product newProduct) {
		inventory.add(newProduct);
		if (newProduct instanceof Wearable) {
			// need to modify othersImpression immediatly for the time the player wear it
			modifyOthersImpression(((Wearable) newProduct).getOthersImpressionGain());

		}
	}

	public void useInventory(Product product) {
		if (product instanceof Wearable) {
			// TODO ask the player if he wants to delette it
			boolean choice = true;
			if (choice) {
				if (((Wearable) product).getOthersImpressionGain() > othersImpression) {
					addMessage(new Message(
							"Vous ne pouvez pas retirer ce vêtement. "
									+ "La vision que les autres personnes ont de vous est déjà trop basse!",
							MsgType.Problem));
				}

				else {
					inventory.remove(product);
				}
			}

		} else {
			// TODO ask a confirmation the player want to use
			boolean choice = true;
			if (choice) {
				String type = product.getType();
				switch (type) {
				case ("Jeux"): {
					modifyMood(((Toy) product).getMoodAdd());
					modifyEnergy(-((Toy) product).getEnergyNeed());
				}
				case ("Nourriture"): {
					modifyHunger(((Nourriture) product).getNutritionalValue());
					modifyEnergy(-((Nourriture) product).getEnergyNeed());
					
				}
				case ("Livre"): {
					// modifyGeneralKnowledge(((Book) product).getKnwoledgeAdd()); //TODO faire la
					// function
					modifyEnergy(-((Book) product).getEnergyNeed());

				}
				}
				inventory.remove(product); 
			}
		}
	}
	
	public void paint(Graphics g, int BLOC_SIZE) {
		super.paint(g, BLOC_SIZE);
		
		//TODO: the yellow border is temporary
        if (isActivePerson()) {
        	g.setColor(Color.YELLOW);
            g.drawRect(
            		getPos().getX() * BLOC_SIZE,
            		getPos().getY() * BLOC_SIZE,
            		(BLOC_SIZE * getSize().getWidth()) - 2,
            		(BLOC_SIZE * getSize().getHeight()) - 2);
        }

		int deltaX = 0;
		int deltaY = 0;

		switch (getDirection()) {
		case EAST:
			deltaX = +(BLOC_SIZE - 2) / 2;
			break;
		case NORTH:
			deltaY = -(BLOC_SIZE - 2) / 2;
			break;
		case WEST:
			deltaX = -(BLOC_SIZE - 2) / 2;
			break;
		case SOUTH:
			deltaY = (BLOC_SIZE - 2) / 2;
			break;
		}

		int xCenter = getPos().getX() * BLOC_SIZE + (BLOC_SIZE - 2) / 2;
		int yCenter = getPos().getY() * BLOC_SIZE + (BLOC_SIZE - 2) / 2;
		g.drawLine(xCenter, yCenter, xCenter + deltaX, yCenter + deltaY);
	}
	
	public GameObject clone() {return null;}
}
