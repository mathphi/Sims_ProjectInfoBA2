package GameObject;

import java.util.ArrayList;
import java.util.Date;

public class Person extends GameObject {

	// general information, name already given in object
	protected String firstName;
	protected Date birthDate;
	protected String gender;
	protected String name;
	protected int relationPoint; // TODO change in player in a dictionnaire quoi qui aura objet -> relation
	// visible caract.
	protected int money;

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
	protected ArrayList<Person> parents = new ArrayList<Person>(); // TODO c un adulte fdp
	// parents are mandatory Other

	// object
	protected ArrayList<GameObject> inventoryHouse = new ArrayList<GameObject>();
	// list that contains all the object, even PNJ got one with their object
	// (cloths,...)
	protected ArrayList<GameObject> inventory = new ArrayList<GameObject>();
	// "little" inventory of object that the player can keep on him, empty at start,
	// PNJ don't have it
	// relations

	public Person(int X, int Y, String name, String firstName, Date birthDate, String type, String gender, int money,
			ArrayList<GameObject> inventoryHouse) {

		// general spec in mother class
		super(X, Y);

		// general information decided in character

		year = birthDate.getYear() + 12; // game start, player is 12 YO to be moove in the main
		// specific player character caracteristic;
		health = 10;
		hunger = 10;
		bladder = 10;
		mood = 10;
		this.type = type;

		//OtherCharacter father = new OtherCharacter(0, 0, "adult", "m"); // 0,0 will be change after (graphic interface)
																		// ce sera un adulte methode changée au final parent sont créés avant
		//OtherCharacter mother = new OtherCharacter(0, 0, "adult", "f"); // 0,0 will be change after (graphic interface)
		//parents.add(father); TODO 
		//parents.add(mother);

		// specific spec for character (player or not) implemented here
		this.firstName = firstName;
		this.birthDate = birthDate;
		this.gender = gender;
		this.name = name;
		relationPoint = 0; // caracterise the relation with the player

		this.money = money; // player can decide to start game with some money
		this.inventoryHouse = inventoryHouse; // also depends of the players start choice

	}

	public void move() {
		// function that allows the personnage to move
	}

	protected int getRelationship() {
		// getter of relationPoint, only purpose in multijoueur -> if more than 2
		// problem
		int relationship = 0; // unknown people
		if (relationPoint > 20) {
			relationship = 3; // serious relation, can propose to marry
		} else if (relationPoint > 10) {
			relationship = 2; // ami proche
		} else if (relationPoint > 2) {
			relationship = 1; // just a connaissance
		}
		return relationship;
	}

	protected void setRelationship(int point) {// changer le nom car pas set mais augmente TODO
		relationPoint += point;
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

		// TODO comment la rendre access partout créer package tool ou y aura aussi
		// point et size -> Tool
		// sera obligatoirement static car Math.round est static -> pas besoin d'avoir
		// un objet pour le faire!
		return start + (int) Math.round(Math.random() * (l - start));
	}
	
	
	
	
	//INTERACTION
	protected void eat(EatableObject nourriture) {
		hunger += setHunger(nourriture);
	}
	

	private int setHunger(EatableObject nourriture) {
		nourriture.getNutritionalValue();
		return 0;
	}

	protected void discuss(Person people) {
		people.setRelationship(1);
		//TODO retirer l'energie
	}
	

	protected void playWith(Person people) {
		people.setRelationship(2);
		//TODO retirer l'energie
	}
	
	protected void invite(Person people) {
		people.setRelationship(3);
		//TODO bringing the people at house! et retirer de l'énergie
		
	}

	
	public void characterInteraction(Person people) {
		
	}

	public void getInformation() {
		// ONLY USED FOR DEBUG
		System.out.println(name);
		System.out.print(firstName);
		System.out.println(parents);

	}

}
