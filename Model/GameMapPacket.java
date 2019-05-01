package Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to serialise all game objects and data.
 * That is simpler to save and restore a map/game.
 */
public class GameMapPacket implements Serializable {
	private static final long serialVersionUID = -6168233993736275354L;
	
	private ArrayList<GameObject> objects;
	private ArrayList<Person> population;
	private Person activePerson = null;
	private long timeFromStart = 0;
	
	public GameMapPacket(ArrayList<GameObject> objects,
			ArrayList<Person> population,
			Person activePerson,
			long timeFromStart)
	{
		this.objects = objects;
		this.population = population;
		this.activePerson = activePerson;
		this.timeFromStart = timeFromStart;
	}

	public GameMapPacket(ArrayList<GameObject> objects,
			ArrayList<Person> population,
			Person activePerson)
	{
		this.objects = objects;
		this.population = population;
		this.activePerson = activePerson;
	}

	public GameMapPacket(ArrayList<GameObject> objects,
			ArrayList<Person> population)
	{
		this.objects = objects;
		this.population = population;
	}
	
	public ArrayList<GameObject> getObjects() {
		return this.objects;
	}
	
	public ArrayList<Person> getPopulation() {
		return this.population;
	}
	
	public Person getActivePerson() {
		return this.activePerson;
	}
	
	public long getTimeFromStart() {
		return this.timeFromStart;
	}
}
