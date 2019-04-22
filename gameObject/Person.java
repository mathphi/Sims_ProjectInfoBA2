package gameObject;

import java.util.ArrayList;
import java.util.Date;

public class Person extends GameObject {

	// general information, name already given in object
	protected String firstName;
	protected Date birthDate;
	protected String gender;
	protected String name;
	protected int relationPoint; //TODO change in player in a dictionnaire quoi qui aura objet -> relation
	// visible caract.
	protected int money;

	// relations
	protected ArrayList<Person> friendList = new ArrayList<Person>();
	// friends can be real in multi mode

	// object
	protected ArrayList<GameObject> inventoryHouse = new ArrayList<GameObject>();
	// list that contains all the object, even PNJ got one with their object
	// (cloths,...)

	public Person(int X, int Y, String name, String firstName, Date birthDate, String gender, int money,
			ArrayList<GameObject> inventoryHouse) {

		// general spec in mother class
		super(X, Y);

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

	
	
	protected void setRelationship(int point) {//changer le nom car pas set mais augmente TODO
		relationPoint += point;
	}



	public void setCaracteristic() {
		// TODO Auto-generated method stub
		
	}

}
