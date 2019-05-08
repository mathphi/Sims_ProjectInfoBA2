package Model;

import Tools.Point;
import Tools.Random;
import Tools.Size;
import View.Message;
import View.Message.MsgType;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.Directable.Direction;
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
		Unknown, Acquaintance, CloseFriend, SeriousRelation, VerySeriousRelation, Parent
	}

	public static enum InteractionType {
		None, Discuss, Play, Invite, Drink, Kiss, Marry
	}
	
	public static enum ActionType {
		Sleep, Nap, Toilet, Shower, Bath, Work
	}

	private static Size SIZE = new Size(2, 2);

	private boolean isActivePerson;
	private boolean isLocked = false;

	// general information
	protected String name;
	protected int age;
	protected Gender gender;
	protected boolean isPlayable = true;

	protected int money = 100000000;

	// Use a past time as initial time
	/*
	 * WARNING: using this LocalDateTime method is buggy because the Real time
	 * continues to run when the game is off.
	 * So the savegame will not keep the good intervals.
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
		modifyEnergy((hygiene >= 20 ? -1 : -3) * energyRandomFactor);

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
		} else if (relationPoints > 75) {
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

	public void automaticAnswer(Person other, double relationFactor) {

		// TODO: how to make a function that contains appropriate
		// answers for the corresponding action
		if (relationFactor > 0.6) {
			other.addMessageFrom(this,
					"C'était vraiment un chouette moment! " +
					"Tu es hyper sympathique et incroyable merci pour tout!",
					MsgType.Info);
		}
		else if (relationFactor > 0.3) {
			other.addMessageFrom(this, 
					"C'était vraiment cool d'être avec toi",
					MsgType.Info);
		}
		else if (relationFactor > 0.0) {
			other.addMessageFrom(this,
					"Je n'avais rien d'autre à faire mais bon... Content de t'avoir vu",
					MsgType.Info);
		}
		else if (relationFactor > -0.5) {
			other.addMessageFrom(this,
					"Je me suis ennuyé j'aurais pas du venir",
					MsgType.Warning);
		}
		else {
			other.addMessageFrom(this,
					"T'es vraiment pas sympathique, ne me recontacte plus jamais!",
					MsgType.Problem);
		}
	}

	public void eat(Nourriture nourriture) {
		float hungerGain = 0;// TODO nourriture.getHungerImpact();
		hunger += (int) (hungerGain);
		if (hunger > 100) {
			// too much point
			hunger = 100;
		}

		// TODO: Euh... Eating cost energy ???
		modifyEnergy(0); // TODO
	}

	protected void discuss(Person people) {
		if (!useEnergy(10))
			return;

		applyInteractionEffect(people, 6, 15);
	}

	protected void playWith(Person people) {
		if (!useEnergy(20))
			return;

		applyInteractionEffect(people, 12, 20);
	}

	protected void invite(Person people) {
		if (!useEnergy(25))
			return;

		applyInteractionEffect(people, 15, 25);

		// TODO bringing the people at house!
	}

	protected void applyInteractionEffect(Person people, double relationWeight, double moodWeight) {
		double relationFactor = getAppreciationOf(people);

		modifyRelationPoints(people, relationWeight * relationFactor);
		people.modifyRelationPoints(this, relationWeight * people.getAppreciationOf(this));
		modifyMood(moodWeight * relationFactor);

		automaticAnswer(people, relationFactor);
		people.automaticAnswer(this, people.getAppreciationOf(this));

		System.out.println(friendList.getOrDefault(people, 0.0));
	}

	protected void applyRejectedEffect(Person people, double relationWeight, double moodWeight) {
		modifyRelationPoints(people, -relationWeight);
		people.modifyRelationPoints(this, -relationWeight);
		modifyMood(-moodWeight);

		// TODO: adapted answers for this
		addMessage(people.getName() + " n'a pas accepté votre demande", MsgType.Problem);
		people.addMessage("Vous avez refusé la demande de " + getName(), MsgType.Problem);

		System.out.println(friendList.getOrDefault(people, 0.0));
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
		case Invite:
			invite(other);
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
			Duration d = Duration.between(getLastActionTime(ActionType.Toilet), LocalDateTime.now());

			// Don't block the Person every time he pass at proximity of the Toilet...
			if (d.getSeconds() < 20)
				return;

			resetLastActionTime(ActionType.Toilet);

			setLocked(true);

			addMessage("Vous êtes en train de vous soulager...", MsgType.Info);

			WaiterThread.wait(5, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					bladder = 100;
					setLocked(false);
					addMessage("*bruit de chasse d'eau*", MsgType.Info);
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

		// TODO: consequence if hygiene <= 0
	}

	public void modifyHunger(double factor) {
		hunger += factor;
		hunger = Math.max(0, Math.min(hunger, 100));

		// TODO: conditional consequences
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
			addMessage("Vous n'avez plus assez d'énergie!", MsgType.Problem);
			return false;
		}

		modifyEnergy(-quantity);

		return true;
	}

	public void modifyMoney(int amount) {
		// if need to pay -> amount <0
		// if it's a gain -> amount >0
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

	public void addInventory(Product newProduct) {
		inventory.add(newProduct);
		if (newProduct instanceof Wearable) {
			// need to modify othersImpression immediatly for the time the player wear it
			modifyOthersImpression(((Wearable) newProduct).getOthersImpressionGain());

		}
	}

	public void useInventory(Product product) {
		if (product instanceof Wearable) {

			// TODO remove mettre le produit sur le sol à l'endroit où est le joueur
			if (((Wearable) product).getOthersImpressionGain() > othersImpression) {
				addMessage(new Message(
						"Vous ne pouvez pas retirer ce vêtement. "
								+ "La vision que les autres personnes ont de vous est déjà trop basse!",
						MsgType.Problem));
			}

			else {
				inventory.remove(product);
				modifyMood(product.getMoodImpact());
				modifyOthersImpression(product.getOtherImpressionImpact());
				addMessage(new Message("Vous venez de retirer votre " + product.getName(), MsgType.Info));

			}

		} else {

			
			//TODO faire controle manuellement pour être sur qu'il peut utiliser + changer energy en use!!
			inventory.remove(product);
			modifyMood(product.getMoodImpact());
			modifyEnergy(product.getEnergyImpact());
			modifyOthersImpression(product.getOtherImpressionImpact());
			modifyHunger(product.getHungerImpact());
			modifyHygiene(product.getHygieneImpact());
			modifygeneralKnowledge(product.getGeneralKnowledgeImpact());
			addMessage(new Message("Vous venez d'utiliser " + product.getName(), MsgType.Info));

		}
	}

	private void modifygeneralKnowledge(int generalKnowledgeImpact) {

		generalKnowledge += generalKnowledgeImpact;
		generalKnowledge = Math.max(0, Math.min(generalKnowledge, 100));

	}

	public void paint(Graphics g, int BLOC_SIZE) {
		super.paint(g, BLOC_SIZE);

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

	public void buy(Product product) {

		if (product.getPrice() > money) {
			addMessage("Vous n'avez pas assez d'argent!", MsgType.Warning);
		} else if (inventory.size() >= 5) {
			addMessage("Votre inventaire est plein vous devez le vider avant d'acheter.", MsgType.Warning);

		} else {
			money -= product.getPrice();
			inventory.add(product);
			addMessage(
					"Félicitation, vous venez d'acheter: " + product.getName() + ".Il se trouve dans votre inventaire.",
					MsgType.Info);
			if (product instanceof Wearable) {
				modifyOthersImpression(product.getOtherImpressionImpact());
				// Clothes have immediate effect.
				addMessage(new Message("Vous venez d'enfiler votre " + product.getName(), MsgType.Info));
			}
		}

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
}
