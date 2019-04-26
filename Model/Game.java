package Model;

import View.Window;
import View.Map;
import View.Status;

import Tools.Point;
import Tools.Size;
import Tools.Rect;

import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Game implements DeletableObserver {
	private final int ARTIFICIAL_SCROLL_RADIUS = 500;

	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private ArrayList<Person> population = new ArrayList<Person>();
	private Person activePerson = null;
	private Window window;
	private Map map;
	private Status status;
	
	private Size mapSize;
	
	private GameTime gameTime;

	public Game(Window window) {
		this.window = window;
		map = window.getMap();
		status = window.getStatus();
		mapSize = map.getMapSize();
		
		gameTime = new GameTime(this);
		
		// TODO: replace this list by a class
		// Creating one Player at position (1,1)
		ArrayList<Double> psychologicFactor = new ArrayList<Double>(); // list that will give more importance in some
																			// caracteristic for automatic answering,
																			// randmoly generated for pnj, encoded for
																			// player
		psychologicFactor.add(20.0); // mood
		psychologicFactor.add(12.0); // health
		psychologicFactor.add(23.0); // generalKnwoledge
		psychologicFactor.add(20.0); // otherVision

		Person p = new Kid(new Point(10, 10), "Test", "Person", "m", 10, 0, objects, null, null, psychologicFactor); // modifi to launch
																									// the game
		objects.add(p);
		population.add(p);
		setActivePerson(p);

		// Map building
		// A sample of room
		for (int i = 0; i < 20; i++) {
			objects.add(new WallBlock(i, 0));
			objects.add(new WallBlock(0, i));

			// Make a door in the wall
			if (i != 10 && i != 11) {
				objects.add(new WallBlock(20 - 1, i));
				objects.add(new WallBlock(i, 20 - 1));
			}
		}

		// A second sample of room
		for (int i = 0; i < 15; i++) {
			objects.add(new WallBlock(20 + i, 0));

			// Make a door in the wall
			if (i != 10 && i != 11) {
				objects.add(new WallBlock(20 + i, 15 - 1));
			}
		}

		// A third sample of room
		for (int i = 0; i < 25; i++) {
			objects.add(new WallBlock(35 + i, 0));
			objects.add(new WallBlock(35 - 1, i));
			objects.add(new WallBlock(35 + i, 25 - 1));

			// Make a door in the wall
			if (i != 15 && i != 16) {
				objects.add(new WallBlock(35 + 25 - 1, i));
			}
		}

		window.setGameObjects(this.getGameObjects());
		notifyView();
	}

	public Size getMapSize() {
		return mapSize;
	}

	public void movePlayer(int x, int y) {
		movePlayer(new Point(x, y));
	}

	public void movePlayer(Point p) {
		Point nextPos = activePerson.getPos().add(p);
		boolean unreachable = false;

		// Check if the nextPos is in the map
		Rect mapRect = new Rect(new Point(0, 0), mapSize);
		unreachable = !mapRect.contains(nextPos);

		for (GameObject object : objects) {
			if (object.isAtPosition(nextPos)) {
				unreachable = object.isObstacle();
				break;
			}
		}

		activePerson.rotate(p);

		if (!unreachable) {
			activePerson.move(p);
		}

		notifyView();

		// Scroll the map view to the player (scoll after notifyView for fluidity)
		// The ARTIFICIAL_SCROLL_RADIUS is used to keep a space between the player and
		// the map's borders
		map.scrollRectToVisible(new Rectangle(
				activePerson.getPos().getX() * map.getBlockSize().getWidth() - ARTIFICIAL_SCROLL_RADIUS,
				activePerson.getPos().getY() * map.getBlockSize().getHeight() - ARTIFICIAL_SCROLL_RADIUS,
				map.getBlockSize().getWidth() + 2 * ARTIFICIAL_SCROLL_RADIUS,
				map.getBlockSize().getHeight() + 2 * ARTIFICIAL_SCROLL_RADIUS));
	}

	public void action() {
		/*
		 * Activable aimedObject = null; for(GameObject object : objects){
		 * if(object.isAtPosition(activePerson.getFrontX(),activePerson.getFrontY())){
		 * if(object instanceof Activable){ aimedObject = (Activable) object; } } } if
		 * (aimedObject != null) { aimedObject.activate(); notifyView(); }
		 */

	}

	private void notifyView() {
		window.update();
	}

	public ArrayList<GameObject> getGameObjects() {
		return this.objects;
	}

	@Override
	synchronized public void delete(Deletable ps, ArrayList<GameObject> loot) {
		objects.remove(ps);
		if (loot != null) {
			objects.addAll(loot);
		}
		notifyView();
	}

	public void playerPos() {
		System.out.println(activePerson.getPos().getX() + ":" + activePerson.getPos().getY());

	}

	public void stop() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}

	public void sendPlayer(Point p) {
		Thread t = new Thread(new AStarThread(this, activePerson, p));
		t.start();
	}
	
	private void updateAllPopulation() {
		//TODO: Necessary ?
	}
	
	private void updateActivePerson() {
		activePerson.updateNeeds();
	}
	
	public void updateGame() {
		// Update the time string in the status area
		status.setGameTimeStr(gameTime.getCurrentTimeString());
		
		long t = gameTime.getVirtualTime();

		// New year
		if (t % gameTime.YEAR_LEN == 0) {
			
		}
		// New month (approx 30 days...)
		if (t % (gameTime.DAY_LEN*30) == 0) {
			updateActivePerson();
		}
		
		notifyView();
	}
	
	public void setActivePerson(Person p) {
		activePerson = p;
		status.setActivePerson(p);
	}

	public void startGame() {
		// TODO
		gameTime.start();

	}

}