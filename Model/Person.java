package Model;

import Tools.Point;
import Tools.Random;
import Tools.Size;
import View.Message;
import View.Message.MsgType;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
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

	public static enum Gender {
		Male, Female
	}
	
	public static enum Relationship {
		Unknown, Acquaintance, CloseFriend, SeriousRelation, Parent
	}
	
	public static enum InteractionType {
		None, Discuss, Play, Invite, Drink, Kiss, Marry
	}

	private static Size SIZE = new Size(2, 2);

	private boolean isActivePerson;

	// general information
	protected String name;
	protected int age;
	protected Gender gender;
	protected boolean isPlayable = true;

	protected int money;

	private LocalDateTime lastBedTime = LocalDateTime.now();

	// visible properties, if = 100 no need
	protected double energy;
	protected double hunger;
	protected double mood;
	protected double hygiene;
	protected double bladder;

	// Automatic answer attributes
	// Represents the knowledge of the player -> increase by going to school or
	// thing like that
	protected double generalKnowledge;

	// Represents the impression of other, if often go out, is in good hygiene,...
	// -> higher
	protected double othersImpression;

	// Represents the importance of mood, hygiene, generalKnwoledge and
	// ohtersImpression
	// for the automatic answers. Will be randomly generated for PNJ
	protected PsychologicalFactors psychologicalFactors;

	protected ArrayList<Product> inventory = new ArrayList<Product>();
	// relation
	protected Map<Person, Integer> friendList = new HashMap<>();
	protected Adult mother;
	protected Adult father;

	// Random factors to differentiate the evolution of each person (one is hungry
	// more often,...)
	private double bladderRandomFactor = Random.range(0.6, 1.2);
	private double hungerRandomFactor = Random.range(0.6, 1.2);
	private double energyRandomFactor = Random.range(0.6, 1.2);

	/*
	 * WARNING: Use of transient keyword to avoid to save these attributes in the
	 * saving file. These attributes are null when the object is restored from the
	 * file.
	 */

	// Game messages history
	protected ArrayList<Message> messagesHistory = new ArrayList<Message>();
	private transient ArrayList<MessageEventListener> msgListeners = new ArrayList<MessageEventListener>();

	// Refresh listeners
	private transient ArrayList<ActionListener> refreshListeners = new ArrayList<ActionListener>();

	// Thread used for smooth moving
	private transient MoveThread moveThread;

	// Constructor used when the Person evolves (ie from Kid to Teenager)
	public Person(Person other) {
		this(other.getPos(), other.getName(), other.getAge(), other.getGender(), other.getMother(), other.getFather());

		money = other.getMoney();

		energy = other.getEnergy() * 100;
		mood = other.getMood() * 100;
		hunger = other.getHunger() * 100;
		hygiene = other.getHygiene() * 100;
		bladder = other.getBladder() * 100;

		generalKnowledge = other.getGeneralKnowledge() * 100;
		othersImpression = other.getOthersImpression() * 100;

		psychologicalFactors = other.getPsychologicalFactor();

		inventory = other.getInventory();
		messagesHistory = other.getMessagesHistory();

		rotate(other.getDirection());
	}

	public Person(Point pos, String name, int age, Gender gender, Adult mother, Adult father) {
		super(pos, SIZE, Color.BLUE);

		this.name = name;
		this.age = age;
		this.gender = gender;
		this.mother = mother;
		this.father = father;

		// Considered as the higher level of relation but CAN't propose to marry, etc
		friendList.put(father, 20);
		friendList.put(mother, 20);

		// Initial Person properties (maximum is 100)
		energy = 100;
		mood = 100;
		hunger = 100;
		hygiene = 100;
		bladder = 100;

		generalKnowledge = 50;
		othersImpression = 50;

		psychologicalFactors = PsychologicalFactors.RandomFactors();

		// Use a past time as initial time
		lastBedTime = LocalDateTime.now().minusDays(1);
	}

	public void clickedEvent(GameObject o) {

	}

	public void proximityEvent(GameObject o) {
		// TODO
	}

	public abstract boolean maxAgeReached();

	public void setActivePerson(boolean is_active) {
		isActivePerson = is_active;
	}

	public boolean isActivePerson() {
		return isActivePerson;
	}

	public void move(Point delta) {
		// moveThread is null when restored from saving file
		if (moveThread == null) {
			moveThread = new MoveThread(this);
			Thread t = new Thread(moveThread);
			t.start();
		}

		moveThread.newMovement(delta);
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

		if (energy == 0) {
			// TODO: what can we do ?
		}
	}

	public void evolves() {
		age++;
	}

	/**
	 * This function return the level of friendship by reading the friendList HashMap.
	 * 
	 * 
	 * @param other
	 * @return
	 */
	public Relationship getRelationship(Person other) {
		Relationship relationship = Relationship.Unknown;
		
		// If the Person is not in the friendList -> unknown
		int relationPoints = friendList.getOrDefault(other, 0);

		// Set the relationship level
		if (other == mother || other == father) {
			relationship = Relationship.Parent;
		} else if (relationPoints > 20) {
			relationship = Relationship.SeriousRelation;
		} else if (relationPoints > 10) {
			relationship = Relationship.CloseFriend;
		} else if (relationPoints > 0) {
			relationship = Relationship.Acquaintance;
		}
		
		return relationship;
	}

	public void modifyRelationPoints(Person friend, double factor) {
		int value = friendList.getOrDefault(friend, 0);
		
		// Apply modification factor
		value += (int) factor;
		
		// Keep range between 0 and 100
		value = Math.max(0, Math.min(value, 100));
		
		// Update the value in the friendList
		friendList.put(friend, value);
	}

	/**
	 * This function compute the appreciation that this Person has of another Person.
	 * The calculation is based on the mood, the hygiene, the general knowledge and
	 * the others impression of the other Person.
	 * 
	 * @param other
	 * The other Person whose appreciation is calculated
	 * 
	 * @return
	 * A relation factor between 0 and 1 (a number close to 1 means good agreement between
	 * this Person and the other, a number close to 0 is the opposite).
	 */
	public double automaticAnswer(Person other) {
		double relationFactor = other.getMood() * psychologicalFactors.getMood()
				+ other.getHygiene() * psychologicalFactors.getHygiene()
				+ other.getGeneralKnowledge() * psychologicalFactors.getGeneralKnowledge()
				+ other.getOthersImpression() * psychologicalFactors.getOthersImpression();
		
		// The sum of psychological factors is always 1
		// So we are sure that the relation factor is between 0 and 1
		
		// TODO: move these messages in a specific function that contains
		//		 appropriate answers for the corresponding action
		if (relationFactor > 0.8) {
			other.addMessageFrom(
					this,
					"C'était vraiment un chouette moment! "
					+ "Tu es hyper sympathique et incroyable merci pour tout!",
					MsgType.Info);
		} else if (relationFactor > 0.6) {
			other.addMessageFrom(
					this,
					"C'était vraiment cool d'être avec toi",
					MsgType.Info);
		} else if (relationFactor > 0.4) {
			other.addMessageFrom(
					this,
					"Je n'avais rien d'autre à faire mais bon... Content de t'avoir vu",
					MsgType.Info);
		} else if (relationFactor > 0.2) {
			other.addMessageFrom(
					this,
					"Je me suis ennuyé j'aurais pas du venir",
					MsgType.Info);
		} else {
			other.addMessageFrom(
					this,
					"T'es vraiment pas sympathique, ne me recontacte plus jamais!",
					MsgType.Info);
		}

		return relationFactor;

	}

	public void eat(Nourriture nourriture) {
		float hungerGain = nourriture.getNutritionalValue();
		hunger += (int) (hungerGain);
		if (hunger > 100) {
			// too much point
			hunger = 100;
		}
		
		//TODO: Euh... Eating cost energy ???
		modifyEnergy(-nourriture.getEnergyNeed());
	}

	public boolean discuss(Person people) {
		if (!modifyEnergy(-10))
			return false;

		applyInteractionEffect(people, 1, 15);

		return true;
	}

	public boolean playWith(Person people) {
		if (!modifyEnergy(-20))
			return false;

		applyInteractionEffect(people, 2, 20);

		return true;
	}

	public boolean invite(Person people) {
		if (!modifyEnergy(-25))
			return false;

		applyInteractionEffect(people, 3, 30);

		// TODO bringing the people at house!
		
		return true;
	}
	
	protected void applyInteractionEffect(Person people, double relationWeight, double moodWeight) {
		double relationFactor = automaticAnswer(people);
		
		modifyRelationPoints(people, relationWeight * relationFactor);
		people.modifyRelationPoints(this, relationWeight * people.automaticAnswer(this));
		modifyMood(moodWeight * relationFactor);
	}

	/**
	 * Function that allows the people to interact with another one. need to be
	 * overwrite in adult and teenager class for interaction with level 3 friends
	 * (thing like kiss, marry,...)
	 * 
	 * TODO: NOOO implement the level 3 here (just not used if this is a Kid,...)
	 * Else we have to overwrite this big function uselessly
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
		default:
			break;
		}
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
			addMessage("Vous n'avez pas été aux toilettes à temps... Vous êtes maintenant très sale", MsgType.Problem);
			modifyHygiene(-50);
			modifyMood(-20);
		}
	}

	/**
	 * This function take care of the hygiene and mood to compute the energy gain. A
	 * random factor is also applied.
	 * 
	 * This function is typically called when the Person sleeps.
	 */
	public void restoreEnergy() {
		double gain = (getHygiene() + getMood()) / 2.0 * 80;
		double randomFactor = Random.range(10, 20);

		double total = gain + randomFactor;

		// Get a total gain of at least 20
		total = Math.max(20, total);

		energy += total;

		// Don't exceed 100 pts of energy
		energy = Math.min(100, energy);
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

	public boolean modifyEnergy(double factor) {
		// Check if enough energy
		if (factor < 0 && -factor > energy) {
			addMessage("Vous n'avez plus assez d'énergie!", MsgType.Warning);
			return false;
		}

		energy += factor;
		energy = Math.max(0, Math.min(energy, 100));

		return true;
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

	public double getGeneralKnowledge() {
		return generalKnowledge / 100.0;
	}

	public double getOthersImpression() {
		return othersImpression / 100.0;
	}

	public Map<Person, Integer> getFriendList() {
		return friendList;
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

	public ArrayList<Product> getInventory() {
		return inventory;
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

	public void addMessage(String text, MsgType type) {
		addMessage(new Message(text, type));
	}
	
	/**
	 * Add a message from another person in the messages list.
	 * The name of the other person is added in bold at start of the message
	 */
	public void addMessageFrom(Person other, String text, MsgType type) {
		addMessage(
				String.format("<b>%s:</b> %s", other.getName(), text),
				type);
	}

	public void addRefreshListener(ActionListener a) {
		// refreshListeners is null when Person is restored from the save file
		if (refreshListeners == null) {
			refreshListeners = new ArrayList<ActionListener>();
		}

		refreshListeners.add(a);
	}

	public void refresh() {
		if (refreshListeners == null)
			return;

		for (ActionListener a : refreshListeners) {
			a.actionPerformed(null);
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

		// TODO: the yellow border is temporary
		if (isActivePerson()) {
			g.setColor(Color.YELLOW);
			g.drawRect((int) (getPos().getX() * BLOC_SIZE), (int) (getPos().getY() * BLOC_SIZE),
					(BLOC_SIZE * getSize().getWidth()) - 2, (BLOC_SIZE * getSize().getHeight()) - 2);
		}

		int deltaX = 0;
		int deltaY = 0;

		Size realSize = new Size(BLOC_SIZE * getSize().getWidth(), BLOC_SIZE * getSize().getHeight());

		switch (getDirection()) {
		case EAST:
			deltaX = +(realSize.getWidth() - 2) / 2;
			break;
		case NORTH:
			deltaY = -(realSize.getHeight() - 2) / 2;
			break;
		case WEST:
			deltaX = -(realSize.getWidth() - 2) / 2;
			break;
		case SOUTH:
			deltaY = (realSize.getHeight() - 2) / 2;
			break;
		}

		int xCenter = (int) (getPos().getX() * BLOC_SIZE) + (realSize.getWidth() - 2) / 2;
		int yCenter = (int) (getPos().getY() * BLOC_SIZE) + (realSize.getHeight() - 2) / 2;
		g.drawLine(xCenter, yCenter, xCenter + deltaX, yCenter + deltaY);
	}

	public GameObject clone() {
		return null;
	}

	private void setLastBedTime(LocalDateTime localDateTime) {
		lastBedTime = LocalDateTime.now();
	}

	public LocalDateTime getLastBedTime() {
		return lastBedTime;
	}

	public void sleep() {
		// TODO disable movement when sleeping !

		setLastBedTime(LocalDateTime.now());
		restoreEnergy();

		// TODO plusieurs messages en fonction du gain d'énergie
		addMessage("Vous avez dormi et récupéré de l'énergie", MsgType.Info);
	}
}
