package Model;

import java.io.Serializable;
import java.util.ArrayList;

import Tools.Size;

/**
 * This class is used to serialise all game objects and data.
 * That is simpler to save and restore a map/game.
 */
public class GameMapPacket implements Serializable {
	private static final long serialVersionUID = -6168233993736275354L;
	
	private Size mapSize;
	private ArrayList<GameObject> objects;
	private ArrayList<Person> population;
	private Person activePerson = null;
	private long timeFromStart = 0;
	
	public GameMapPacket(Size mapSize,
			ArrayList<GameObject> objects,
			ArrayList<Person> population,
			Person activePerson,
			long timeFromStart)
	{
		this.mapSize = mapSize;
		this.objects = objects;
		this.population = population;
		this.activePerson = activePerson;
		this.timeFromStart = timeFromStart;
	}

	public GameMapPacket(Size mapSize,
			ArrayList<GameObject> objects,
			ArrayList<Person> population,
			Person activePerson)
	{
		this.mapSize = mapSize;
		this.objects = objects;
		this.population = population;
		this.activePerson = activePerson;
	}

	public GameMapPacket(Size mapSize,
			ArrayList<GameObject> objects,
			ArrayList<Person> population)
	{
		this.mapSize = mapSize;
		this.objects = objects;
		this.population = population;
	}
	
	public Size getMapSize() {
		return this.mapSize;
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
