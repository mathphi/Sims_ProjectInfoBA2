package Model;

import Tools.Point;
import Tools.Size;

import java.util.ArrayList;
import java.util.Date;

public abstract class Person extends GameObject implements Directable {
	private static Size SIZE = new Size(1,1);

    private int direction = EAST; 
    
	// general information, name already given in object
	protected String firstName;
	protected String lastName;
	protected Date birthDate;
	protected String gender;
	protected int relationPoint; //TODO change in player in a dictionnaire quoi qui aura objet -> relation
	// visible caract.
	protected int money;
	protected int energy;

	// relations
	protected ArrayList<Person> friendList = new ArrayList<Person>();
	// friends can be real in multi mode

	// object
	protected ArrayList<GameObject> inventoryHouse = new ArrayList<GameObject>();
	// list that contains all the object, even PNJ got one with their object
	// (cloths,...)

	public Person(Point pos, String firstName, String lastName, String gender) {

		// general spec in mother class
		super(pos, SIZE);

		// specific spec for character (player or not) implemented here
		this.firstName = firstName;
		this.gender = gender;
		this.lastName = lastName;
		relationPoint = 0; // caracterise the relation with the player
	}

    public void move(Point p) {
        this.setPos(this.getPos().add(p));
    }

    public void rotate(Point p) {
        if(p.getX() == 0 && p.getY() == -1)
            direction = NORTH;
        else if(p.getX() == 0 && p.getY() == 1)
            direction = SOUTH;
        else if(p.getX() == 1 && p.getY() == 0)
            direction = EAST;
        else if(p.getX() == -1 && p.getY() == 0)
            direction = WEST;
    }
	
	public int getDirection() {
	    return direction;
	}

	public boolean isObstacle() {
		return true;
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

	public int getEnergy() {
		// TODO
		return 0;
	}
}
