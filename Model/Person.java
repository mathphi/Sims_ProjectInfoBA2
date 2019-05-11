package Model;

import Tools.ImagesFactory;
import Tools.Point;
import Tools.Random;
import Tools.Size;
import View.AutomaticAnswers;
import View.Message;
import View.Message.MsgType;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Products.Cloth;
import Products.Product;

public abstract class Person extends GameObject {
	private static final long serialVersionUID = 8476495059211784395L;
	private static final int INVENTORY_MAX_SIZE = 5;
	private static final int ANIM_STEPS_COUNT = 4;

	public static enum Gender {
		Male, Female
	}

	public static enum Relationship {
		Unknown, Acquaintance, CloseFriend, SeriousRelation, VerySeriousRelation, Parent
	}

	public static enum InteractionType {
		None, Discuss, Play, Drink, Kiss, Marry
	}
	
	public static enum ActionType {
		Sleep, Nap, Toilet, Shower, Bath, Work
	}

	private static Size SIZE = new Size(2, 4);

	// Global Person state attributes
	private boolean isActivePerson;
	private boolean isLocked = false;
	
	// Index for the moving animation 
	private int animIndex = 1;

	// General informations
	protected String name;
	protected int age;
	protected Gender gender;
	protected boolean isPlayable = true;

	protected int money;

	// Use a past time as initial time
	/*
	 * WARNING: using this LocalDateTime method is buggy because the Real time
	 * continues to run when the game is paused !
	 * TODO: This may be good to use the GameTime instead.
	 */
	private HashMap<ActionType,LocalDateTime> lastActionsTime = 
			new HashMap<Person.ActionType,LocalDateTime>();

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
	protected Map<Person, Double> friendList = new HashMap<Person, Double>();
	protected Adult mother;
	protected Adult father;

	/*
	 *  Random factors to differentiate the evolution of each
	 *  person (one is hungry more often,...)
	 */
	private double bladderRandomFactor = Random.range(0.6, 1.2);
	private double hungerRandomFactor = Random.range(0.6, 1.2);
	private double energyRandomFactor = Random.range(0.6, 1.2);
	private double moodRandomFactor = Random.range(0.6, 1.2);
	private double hygieneRandomFactor = Random.range(0.6, 1.2);

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

	// Constructor used when the Person evolves (ie from Kid to Teenager)
	public Person(Person other) {
		this(other.getPos(),
			 other.getName(),
			 other.getAge(),
			 other.getGender(),
			 other.getMother(),
			 other.getFather());

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

		friendList = other.getFriendList();

		rotate(other.getDirection());
	}

	public Person(Point pos, String name, int age, Gender gender, Adult mother, Adult father) {
		super(pos, SIZE, Color.BLUE);
		
		// A person has no permutable size
		this.sizePermutable = false;

		this.name = name;
		this.age = age;
		this.gender = gender;
		this.mother = mother;
		this.father = father;

		// Considered as the higher level of relation but CAN't propose to marry, etc
		if (father != null) friendList.put(father, 100.0);
		if (mother != null) friendList.put(mother, 100.0);

		// Initial Person properties (maximum is 100)
		energy = 100;
		mood = 100;
		hunger = 100;
		hygiene = 100;
		bladder = 100;

		generalKnowledge = 50;
		othersImpression = 50;
		
		// Initial money
		money = 200;

		psychologicalFactors = PsychologicalFactors.RandomFactors();
	}

	public void clickedEvent(GameObject o) {
	}

	public void proximityEvent(GameObject o) {
	}

	public abstract boolean maxAgeReached();

	public void setActivePerson(boolean is_active) {
		isActivePerson = is_active;
	}

	public boolean isActivePerson() {
		return isActivePerson;
	}

	public Point getFrontPos() {
		Point pos = getPos();
		
		switch (getDirection()) {
		case NORTH:
			pos = pos.add(getSize().getWidth()/2, -1);
			break;
		case EAST:
			pos = pos.add(getSize().getWidth(), getSize().getHeight()/2);
			break;
		case SOUTH:
			pos = pos.add(getSize().getWidth()/2, getSize().getHeight());
			break;
		case WEST:
			pos = pos.add(-1, getSize().getHeight()/2);
			break;
		default:
			break;
		}
		
		return pos;
	}
	
	private Direction convertOrientation(Point delta) {
		Direction direction = Direction.EAST;

		int x = delta.getXInt();
		int y = delta.getYInt();

		if (x == 0 && y == -1)
			direction = Direction.NORTH;
		else if (x == 0 && y == 1)
			direction = Direction.SOUTH;
		else if (x == 1 && y == 0)
			direction = Direction.EAST;
		else if (x == -1 && y == 0)
			direction = Direction.WEST;

		return direction;
	}

	public void rotate(Point delta) {
		super.rotate(convertOrientation(delta));
	}

	public void move(Point delta) {
		// Don't move if the Person sleep
		if (isLocked())
			return;

		rotate(delta);
		setPos(getPos().add(delta));
		refresh();
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
		// Don't update the needs when sleeping...
		if (isLocked())
			return;

		decreaseBladder(Random.range(2.0, 3.0) * bladderRandomFactor); // Random decrease
		modifyHunger(Random.range(-1.0, -2.0) * hungerRandomFactor); // Random decrease

		// Decrease more energy if hygiene is low
		modifyEnergy((hygiene >= 20 ? -1.0 : -3.0) * energyRandomFactor);

		modifyMood(-0.5 * moodRandomFactor);
		modifyHygiene(-0.8 * hygieneRandomFactor);

		if (energy == 0) {
			// TODO: what can we do ?
		}
	}

	public void evolves() {
		age++;
	}

	/**
	 * This function return the level of friendship by parsing the relation points.
	 * 
	 * @param other
	 * @return
	 */
	public Relationship getRelationship(Person other) {
		Relationship relationship = Relationship.Unknown;

		// If the Person is not in the friendList -> 0 -> Unknown
		double relationPoints = getRelationPoints(other);

		// Set the relationship level
		if (other == mother || other == father ||	// Other is my parent ?
			other.getMother() == this || other.getFather() == this) { // Other is my child ? //TODO: use a children list in Adult ?
			relationship = Relationship.Parent;
		} else if (relationPoints > 75 && this.getClass() == other.getClass()) {
			// Don't enter in a very serious relation if the Person are not on the same level
			relationship = Relationship.VerySeriousRelation;
		} else if (relationPoints > 40) {
			relationship = Relationship.SeriousRelation;
		} else if (relationPoints > 15) {
			relationship = Relationship.CloseFriend;
		} else if (relationPoints > 0) {
			relationship = Relationship.Acquaintance;
		}

		return relationship;
	}

	public void modifyRelationPoints(Person friend, double factor) {
		double value = friendList.getOrDefault(friend, 0.0);

		// Apply modification factor
		value += factor;

		// Keep range between 0 and 100
		value = Math.max(0, Math.min(value, 100));

		// Update the value in the friendList
		friendList.put(friend, value);
	}

	/**
	 * Get the relation points by reading the friendList HashMap
	 * 
	 * @param friend The Person to get the relation points
	 * 
	 * @return The relation points of the Person or 0 if not in the HashMap
	 */
	public double getRelationPoints(Person friend) {
		// If the Person is not in the friendList -> 0
		double relationPoints = friendList.getOrDefault(friend, 0.0);
		return relationPoints;
	}

	/**
	 * This function compute the appreciation that this Person has of another
	 * Person. The calculation is based on the mood, the hygiene, the general
	 * knowledge and the others impression of the other Person.
	 * 
	 * @param other The other Person whose appreciation is calculated
	 * 
	 * @return A relation factor between -1 and 1 (a number close to 1 means good
	 *         agreement between this Person and the other, a number close to -1 is
	 *         the opposite).
	 */
	public double getAppreciationOf(Person other) {
		double relationFactor = other.getMood() * psychologicalFactors.getMood()
				+ other.getHygiene() * psychologicalFactors.getHygiene()
				+ other.getGeneralKnowledge() * psychologicalFactors.getGeneralKnowledge()
				+ other.getOthersImpression() * psychologicalFactors.getOthersImpression();

		/*
		 * The sum of psychological factors is always 1. So we are sure that the
		 * relation factor is between 0 and 1, and let convert it to a factor between -1
		 * and 1 (for applications to mood, relationship, ...). Therefore if the
		 * relationFactor can be directly used to increase or decrease the mood or the
		 * relationship.
		 */

		relationFactor = relationFactor * 2 - 1;

		return relationFactor;

	}

	public void automaticAnswer(Person other, InteractionType type, double appreciationLevel) {
		String message = AutomaticAnswers.getAutomaticAnswer(type, appreciationLevel);
		MsgType msgType = MsgType.Info;
		
		if (appreciationLevel > 0.0) {
			msgType = MsgType.Info;
		}
		else if (appreciationLevel > -0.5) {
			msgType = MsgType.Warning;
		}
		else {
			msgType = MsgType.Problem;
		}

		other.addMessageFrom(this, message, msgType);
	}

	protected void discuss(Person people) {
		if (!useEnergy(10))
			return;

		applyInteractionEffect(people, InteractionType.Discuss, 6, 15);
	}

	protected void playWith(Person people) {
		if (!useEnergy(20))
			return;

		applyInteractionEffect(people, InteractionType.Play, 12, 20);
	}

	protected void applyInteractionEffect(Person people, InteractionType type, double relationWeight, double moodWeight) {
		double appreciationFactor = getAppreciationOf(people);

		modifyRelationPoints(people, relationWeight * appreciationFactor);
		people.modifyRelationPoints(this, relationWeight * people.getAppreciationOf(this));
		modifyMood(moodWeight * appreciationFactor);

		automaticAnswer(people, type, appreciationFactor);
		people.automaticAnswer(this, type, people.getAppreciationOf(this));
	}

	/**
	 * Function that allows the people to interact with another one. Overwritten in
	 * Adult and Teenager classes for interaction more types of interactions
	 * 
	 * @param other       The other people with which to interact
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
		default:
			break;
		}
	}

	/**
	 * Decrease the bladder of the factor and check if the bladder full. If the
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
	 * In short, he pisses... We just have to check if the person pisses in a toilet or
	 * just... on himself. The player will lose hygiene, and mood in the second case.
	 */
	public void emptyBladder(boolean isOnToilet) {
		if (!isOnToilet) {
			bladder = 100;
			modifyHygiene(-50);
			modifyMood(-20);
			modifyOthersImpression(-35);

			addMessage(
					"Vous n'avez pas été aux toilettes à temps... "
					+ "Vous êtes maintenant très sale !",
					MsgType.Problem);
		}
		else {
			if (isLocked())
				return;
			
			Duration d = Duration.between(getLastActionTime(ActionType.Toilet), LocalDateTime.now());

			// Don't block the Person every time he pass at proximity of the Toilet...
			if (d.getSeconds() < 20)
				return;

			setLocked(true);

			addMessage("Vous êtes en train de vous soulager...", MsgType.Info);

			WaiterThread.wait(5, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					bladder = 100;
					setLocked(false);
					addMessage("*bruit de chasse d'eau*", MsgType.Info);

					resetLastActionTime(ActionType.Toilet);
				}
			});
		}
	}

	/**
	 * This function take care of the hygiene and mood to compute the energy gain. A
	 * random factor is also applied.
	 * 
	 * This function is typically called when the Person sleeps.
	 */
	public void restoreEnergy(int energyFactor) {
		// 4/5 of the energyFactor is a computed gain, 1/5 is a random factor
		double gain = (getHygiene() + getMood()) / 2.0 * (energyFactor * 4.0 / 5.0);
		double randomFactor = Random.range((energyFactor / 10.0), (energyFactor / 5.0));

		double total = gain + randomFactor;

		// Get a total gain of at least 10
		total = Math.max(10, total);

		energy += total;

		// Don't exceed 100 pts of energy
		energy = Math.min(100, energy);
	}

	/**
	 * This function take care of the energy to compute the hygiene.
	 * A random factor is also applied.
	 */
	public void restoreHygiene(int hygieneFactor) {
		// 4/5 of the energyFactor is a computed gain, 1/5 is a random factor
		double gain = getEnergy() * (hygieneFactor * 4.0 / 5.0);
		double randomFactor = Random.range((hygieneFactor / 10.0), (hygieneFactor / 5.0));

		double total = gain + randomFactor;

		// Get a total gain of at least 10
		total = Math.max(10, total);

		hygiene += total;

		// Don't exceed 100 pts of hygiene
		hygiene = Math.min(100, hygiene);
	}

	/**
	 * Modify the hygiene of a factor
	 * 
	 * @param factor
	 */
	public void modifyHygiene(double factor) {
		hygiene += factor;
		hygiene = Math.max(0, Math.min(hygiene, 100));
	}

	public void modifyHunger(double factor) {
		hunger += factor;
		hunger = Math.max(0, Math.min(hunger, 100));

		if (hunger <= 0) {
			modifyHygiene(-5);
			modifyMood(-10);
		}
	}

	public void modifyEnergy(double factor) {
		energy += factor;
		energy = Math.max(0, Math.min(energy, 100));
	}

	/**
	 * This function check if we have enough energy to use it and modifies the
	 * energy if all is ok, else return false and print a message
	 */
	public boolean useEnergy(double quantity) {
		// Check if enough energy
		if (quantity > 0 && quantity > energy) {
			addMessage("Vous n'avez plus assez d'énergie !", MsgType.Problem);
			return false;
		}

		modifyEnergy(-quantity);

		return true;
	}

	/**
	 * If need to pay -> amount < 0
	 * If it's a gain -> amount > 0
	 * 
	 * @param amount
	 */
	public void modifyMoney(int amount) {
		money += amount;
	}

	public void modifyMood(double value) {
		// Add a random factor to add unpredictable behaviours in the game
		mood += value + Random.range(0, value / 5.0);

		// Don't exceed limits
		mood = Math.max(0, Math.min(mood, 100));
	}

	public void modifyOthersImpression(double value) {
		// Add a random factor to add unpredictable behaviours in the game
		othersImpression += value + Random.range(-value / 10.0, value / 10.0);

		// Don't exceed limits
		othersImpression = Math.max(0, Math.min(othersImpression, 100));
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

	public Map<Person, Double> getFriendList() {
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
	 * Add a message from another person in the messages list. The name of the other
	 * person is added in bold at start of the message
	 */
	public void addMessageFrom(Person other, String text, MsgType type) {
		addMessage(String.format("<b>%s :</b> %s", other.getName(), text), type);
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

	public void useInventory(Product product) {
		// Check if we have enough energy
		if (energy + product.getEnergyImpact() < 0) {
			addMessage(
					"Vous n'avez pas assez d'énergie pour utiliser ce produit !",
					MsgType.Warning);
			return;
		}
		
		// Remove product from inventory
		inventory.remove(product);
		
		// Apply impact
		modifyMood(product.getMoodImpact());
		modifyEnergy(product.getEnergyImpact());
		modifyHunger(product.getHungerImpact());
		modifyHygiene(product.getHygieneImpact());
		modifyGeneralKnowledge(product.getGeneralKnowledgeImpact());
		modifyOthersImpression(product.getOtherImpressionImpact());
		
		addMessage("Vous venez d'utiliser votre " + product.getName().toLowerCase(), MsgType.Info);
	}
	
	public void buyProduct(Product product) {
		if (product.getPrice() > money) {
			addMessage("Vous n'avez pas assez d'argent pour acheter ce produit !", MsgType.Warning);
		}
		else if (inventory.size() >= INVENTORY_MAX_SIZE) {
			addMessage("Votre inventaire est plein ! Vous devez le vider avant d'acheter autre chose.", MsgType.Warning);
		}
		else {
			modifyMoney(-product.getPrice());
			
			if (product instanceof Cloth) {
				// Clothes have immediate effect
				modifyOthersImpression(product.getOtherImpressionImpact());
				addMessage(new Message(
						String.format(
								"Vous venez d'enfiler votre %s", 
								product.getName().toLowerCase()),
						MsgType.Info));
			}
			else {
				// Add product to inventory
				inventory.add(product);
				
				addMessage(
						String.format(
								"Vous venez d'acheter un(e) %s. Il se trouve dans votre inventaire.",
								product.getName().toLowerCase()),
						MsgType.Info);
			}
		}
	}
	
	public boolean isInventoryFull() {
		return inventory.size() >= INVENTORY_MAX_SIZE;
	}

	private void modifyGeneralKnowledge(int generalKnowledgeImpact) {

		generalKnowledge += generalKnowledgeImpact;
		generalKnowledge = Math.max(0, Math.min(generalKnowledge, 100));

	}

	public GameObject clone() {
		return null;
	}

	public void resetLastActionTime(ActionType a) {
		lastActionsTime.put(a, LocalDateTime.now());
	}
	
	public LocalDateTime getLastActionTime(ActionType a) {
		return lastActionsTime.getOrDefault(a, LocalDateTime.now().minusDays(1));
	}
	
	public void sleep() {
		addMessage("Bonne nuit, vous êtes maintenant endormi", MsgType.Info);

		activateSleepState(60, 100, ActionType.Sleep);
	}

	public void repose() {
		addMessage("Bonne sieste, reposez-vous bien", MsgType.Info);

		activateSleepState(20, 40, ActionType.Nap);
	}

	private void activateSleepState(int duration, int energyFactor, ActionType type) {
		if (type != ActionType.Nap && type != ActionType.Sleep) {
			throw new IllegalArgumentException("Bad sleep state type");
		}
		
		// Mark Person as locked (prevent movements,...)
		setLocked(true);

		WaiterThread.wait(duration, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restoreEnergy(energyFactor);
				resetLastActionTime(type);

				setLocked(false);

				addMessage("Vous vous êtes reposé, vous avez récupéré de l'énergie", MsgType.Info);
			}
		});
	}
	
	public void takeShower() {
		if (useEnergy(10)) {
			addMessage("Vous avez commencé à prendre une douche", MsgType.Info);
			activateWashingState(20, 50, ActionType.Shower);
		}
	}
	
	public void takeBath() {
		if (useEnergy(15)) {
			addMessage("Vous êtes en train de prendre un bain", MsgType.Info);
			activateWashingState(50, 100, ActionType.Shower);
		}
	}
	
	private void activateWashingState(int duration, int hygieneFactor, ActionType type) {
		if (type != ActionType.Shower && type != ActionType.Bath) {
			throw new IllegalArgumentException("Bad washing state type");
		}
		
		// Mark Person as locked (prevent movements,...)
		setLocked(true);
		
		WaiterThread.wait(duration, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restoreHygiene(hygieneFactor);
				resetLastActionTime(type);

				setLocked(false);

				addMessage("Vous vous êtes lavé, vous avez récupéré de l'hygiène", MsgType.Info);
			}
		});
	}

	public void setLocked(boolean locked) {
		isLocked = locked;
	}

	public boolean isLocked() {
		return isLocked;
	}
	
	public void rotateToObjectDirection(GameObject dest) {
		Point from_pos = this.getPos();
		Point to_pos = dest.getPos();
		
		// Compute with the position of the middle of the object
		from_pos = from_pos.add(getSize().getWidth()/2.0, getSize().getHeight()/2.0);
		to_pos = to_pos.add(dest.getSize().getWidth()/2.0, dest.getSize().getHeight()/2.0);

		double dx = to_pos.getX() - from_pos.getX();
		double dy = to_pos.getY() - from_pos.getY();
		
		Direction d = Direction.EAST;
		
		// Get the main direction
		if (Math.abs(dx) > Math.abs(dy)) {
			if (dx > 0) {
				d = Direction.EAST;
			} else {
				d = Direction.WEST;
			}
		} else {
			if (dy > 0) {
				d = Direction.SOUTH;
			} else {
				d = Direction.NORTH;
			}
		}
		
		rotate(d);
	}
	
	public void incrementAnimIndex() {
		if (animIndex + 1 > ANIM_STEPS_COUNT) {
			animIndex = 1;
		}
		else {
			animIndex++;
		}
	}
	
	public void resetAnimIndex() {
		animIndex = 1;
	}
	
	public int getCurrentAnimIndex() {
		return animIndex;
	}
	
	private String getGenderLetter() {
		String letter = "M";
		
		switch (gender) {
		case Male:
			letter = "M";
			break;
		case Female:
			letter = "F";
			break;
		default:
			break;
		}
		
		return letter;
	}
	
	@Override
	public BufferedImage getCurrentImage() {
		String imgID = String.format(
				"%s_%s_%s_%d",
				this.getClass().getSimpleName(),
				getGenderLetter(),
				getDirectionLetter(),
				getCurrentAnimIndex());
		
		BufferedImage img = ImagesFactory.getImage(imgID);
		
		if (img == null) {
			// Try to get the first SOUTH image (as default image)
			imgID = String.format("%s_%s_S_1", this.getClass().getSimpleName(), getGenderLetter());
			img = ImagesFactory.getImage(imgID);
		}
		
		return img;
	}

	public void paint(Graphics g, int BLOC_SIZE) {
		Graphics2D g2d = (Graphics2D) g;
		
		if (isActivePerson()) {
			g2d.setColor(Color.BLUE);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawOval(
					(int) (getPos().getX() * BLOC_SIZE + 2),
					(int) ((getPos().getY() + getSize().getHeight() - 1) * BLOC_SIZE),
					BLOC_SIZE * getSize().getWidth() - 4,
					BLOC_SIZE * getSize().getWidth() - 4);
		}

		super.paint(g2d, BLOC_SIZE);
	}

}
