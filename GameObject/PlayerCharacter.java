package GameObject;

import java.util.ArrayList;
import java.util.Date;

abstract class PlayerCharacter extends Person implements AutomaticCharacter {

	// general information already in Charater

	// spec to player character (more characteristic)
	// when = 10: no need of actions
	protected int health;
	protected int hunger;
	protected int bladder;
	protected int mood;
	protected int energy;
	protected int year;
	protected String type;
	// relations
	protected ArrayList<Person> friendList = new ArrayList<Person>(); // TODO a tranformer en hashmap c le futur dico ok
	// friends can be real in multi mode
	protected ArrayList<OtherCharacter> parents = new ArrayList<OtherCharacter>();  //TODO c un adulte fdp
	// parents are mandatory Other

	// object
	protected ArrayList<GameObject> inventoryHouse = new ArrayList<GameObject>();
	// list that contains all the object, even PNJ got one with their object
	// (cloths,...)
	protected ArrayList<GameObject> inventory = new ArrayList<GameObject>();
	// "little" inventory of object that the player can keep on him, empty at start,
	// PNJ don't have it

	public PlayerCharacter(int X, int Y, String name, String firstName, Date birthDate, String type, String gender,
			int money, ArrayList<GameObject> inventoryHouse) {

		// general information decided in character
		super(X, Y, name, firstName, birthDate, gender, money, inventoryHouse);
		year = birthDate.getYear() + 12; // game start, player is 12 YO to be moove in the main
		// specific player character caracteristic;
		health = 10;
		hunger = 10;
		bladder = 10;
		mood = 10;
		this.type = type;

		OtherCharacter father = new OtherCharacter(0, 0, "adult", "m"); // 0,0 will be change after (graphic interface) ce sera un adulte 
		OtherCharacter mother = new OtherCharacter(0, 0, "adult", "f"); // 0,0 will be change after (graphic interface)
		parents.add(father);
		parents.add(mother);
	}

	public void doAutomaticThing() {
		// TODO
	}

	// SETTER

	public boolean setMoney(int gold) {
		// TODO to change in add and buy/transaction (set is to set)
		boolean check = true;
		if ((money + gold) <= 0) {
			// not enough money, gold<0 if its a price, >0 if its a earn
			System.out.println("not enough money");
			check = false; // payment not done
		} else {
			money += gold;
		}
		return check;
	}

	public void setCaracteristic() {
		// function that will set all the caracteristic like mood, hunger, energy at the
		// end of the day
		// re define in each age
	}

	public void characterInteraction() {
		// Redefine after
	}

	// GETTER

	// FOR PROGRAMMATION PURPOSE ONLY

	protected static int randBetween(int start, long l) {
		
		// TODO comment la rendre access partout créer package tool ou y aura aussi point et size -> Tool 
		//sera obligatoirement static car Math.round est static -> pas besoin d'avoir un objet pour le faire!
		return start + (int) Math.round(Math.random() * (l - start));
	}

	public void getInformation() {
		// ONLY USED FOR DEBUG
		System.out.println(name);
		System.out.print(firstName);
		System.out.println(parents);

	}

}
