package Model;

import View.Window;
import View.Map;
import View.MenuDialog;
import View.MessagesZone;
import View.Status;
import Tools.ObjectRestorer;
import Tools.ObjectSaver;
import Tools.Point;
import Tools.Size;
import Tools.Rect;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Game implements DeletableObserver {
	private final int SAVEFILE_VERSION_ID = 2;
	private final int ARTIFICIAL_SCROLL_RADIUS = 500;

	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private ArrayList<Person> population = new ArrayList<Person>();
	private Person activePerson = null;
	private Window window;
	private Map map;
	private Status status;
	private MessagesZone msgZone;
	private MenuDialog mainMenu;
	
	private Size mapSize;
	
	private GameTime gameTime;
	
	private boolean isPaused = false;

	public Game(Window window) {
		this.window = window;
		map = window.getMap();
		status = window.getStatus();
		msgZone = window.getMsgZone();
		mapSize = map.getMapSize();
		
		gameTime = new GameTime(this, 0);
		
		window.addMenuButtonAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGameMenu();
			}
		});
		
		mainMenu = new MenuDialog(window, this);
		
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

		Person p1 = new Kid(new Point(10, 10), "Test", "Person", Person.Gender.Male, 10, 0, null, null, psychologicFactor);
		Person p2 = new Kid(new Point(17, 13), "Second", "Player", Person.Gender.Female, 10, 0, null, null, psychologicFactor);
		Person p3 = new Adult(new Point(10, 17), "Third", "People", Person.Gender.Female, 10, 0, null, null, psychologicFactor);

		attachPersonToGame(p1);
		attachPersonToGame(p2);
		attachPersonToGame(p3);
		setActivePerson(p1);


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

		map.setObjects(this.getGameObjects());
		notifyView();
	}
	
	public void mouseLeftClickEvent(Point pos) {
		GameObject object = getObjectAtPosition(pos);
		
		if (object != null && object.isObstacle()) {
			object.clickedEvent();
		}
		else {
			sendPlayer(pos);
		}
	}
	
	public void mouseRightClickEvent(Point pos) {
		GameObject object = getObjectAtPosition(pos);
		
		if (object != null && object instanceof Person) {
			Person selectedPerson = (Person)(object);
			setActivePerson(selectedPerson);
		}
	}
	
	public GameObject getObjectAtPosition(Point pos) {
		GameObject obj = null;
		
		for (GameObject o : objects) {
			if (o.isAtPosition(pos)) {
				obj = o;
				break;
			}
		}
		
		return obj;
	}

	public Person getActivePerson() {
		return activePerson;
	}
	
	public Size getMapSize() {
		return mapSize;
	}
	
	public void moveActivePlayer(int x, int y) {
		movePlayer(activePerson, x, y);
	}

	public void moveActivePlayer(Point pos) {
		movePlayer(activePerson, pos);
	}

	public void movePlayer(Person pers, int x, int y) {
		movePlayer(pers, new Point(x, y));
	}
	
	public void movePlayer(Person pers, Point pos) {
		Point nextPos = pers.getPos().add(pos);
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

		pers.rotate(pos);

		if (!unreachable) {
			pers.move(pos);
		}

		notifyView();
		centerViewOnPlayer();
	}
	
	public void centerViewOnPlayer() {
		// Scroll the map view to the active person (scoll after notifyView for fluidity)
		// The ARTIFICIAL_SCROLL_RADIUS is used to keep a space between the player and
		// the map's borders
		map.scrollRectToVisible(new Rectangle(
				activePerson.getPos().getX() * map.getBlockSize().getWidth() - ARTIFICIAL_SCROLL_RADIUS,
				activePerson.getPos().getY() * map.getBlockSize().getHeight() - ARTIFICIAL_SCROLL_RADIUS,
				map.getBlockSize().getWidth() + 2 * ARTIFICIAL_SCROLL_RADIUS,
				map.getBlockSize().getHeight() + 2 * ARTIFICIAL_SCROLL_RADIUS));
	}

	private void notifyView() {
		window.update();
	}

	public ArrayList<GameObject> getGameObjects() {
		return this.objects;
	}

	@Override
	synchronized public void delete(Deletable ps, ArrayList<GameObject> loot) {
		objects.remove((GameObject)ps);
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
	/*
	private void updateAllPopulation() {
		//TODO: Necessary ?
	}*/
	
	private void updateActivePerson() {
		activePerson.updateNeeds();
	}
	
	public void updateGame() {
		// Update the time string in the status area
		status.setGameTimeStr(gameTime.getCurrentTimeString());
		
		long t = gameTime.getVirtualTime();

		// New year
		if (t % GameTime.YEAR_LEN == 0) {
			
		}
		// New month (approx 30 days...)
		if (t % (GameTime.DAY_LEN*30) == 0) {
			updateActivePerson();
		}
		
		notifyView();
	}
	
	public void setActivePerson(Person p) {		
		activePerson = p;
		status.setActivePerson(p);

		if (p != null) {
			msgZone.setMessagesList(p.getMessagesHistory());
		}
		
		for (Person people : population) {
			people.setActivePerson(people == p);
		}
		
		notifyView();
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public void pauseGame() {
		isPaused = true;
		gameTime.stop();
		
	}
	
	public void resumeGame() {
		isPaused = false;
		gameTime.start();
		
	}

	public void startGame() {
		// TODO
		
		gameTime.start();
		
		notifyView();
		centerViewOnPlayer();
	}
	
	public void openGameMenu() {
		pauseGame();
		mainMenu.showMenu();
		resumeGame();
	}
	
	public void saveGame() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Fichier de sauvegarde", "sav");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Destination du fichier de sauvegarde");
		int returnVal = chooser.showSaveDialog(window);
		
	    if(returnVal != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }
		
		ObjectSaver saver = new ObjectSaver(chooser.getSelectedFile().getPath(), SAVEFILE_VERSION_ID);
		
		// WARNING: The order is very important and must be the same as the restoring order !
		saver.addObjectToSave(objects);
		saver.addObjectToSave(population);
		saver.addObjectToSave(activePerson);
		saver.addObjectToSave(gameTime.getTimeFromStart());
		saver.writeSaveToFile();
	}
	
	@SuppressWarnings("unchecked")
	public void restoreGame() {		
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Fichier de sauvegarde", "sav");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Ouvrir un fichier de sauvegarde");
		int returnVal = chooser.showOpenDialog(window);
		
	    if(returnVal != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }
		
		ObjectRestorer restorer = new ObjectRestorer(chooser.getSelectedFile().getPath());
		
		if (!restorer.versionMatches(SAVEFILE_VERSION_ID)) {
			System.out.println("Save-file version mismatch");
			return;
		}
		
		gameTime.stop();
		gameTime.cancel();
		
		// WARNING: The order is very important and must be the same as the saving order !
		objects = (ArrayList<GameObject>)(restorer.readNextObjectFromSave());
		population = (ArrayList<Person>)(restorer.readNextObjectFromSave());
		setActivePerson((Person)(restorer.readNextObjectFromSave()));
		gameTime = new GameTime(this, (long)(restorer.readNextObjectFromSave()));
		
		restorer.closeSaveFile();
		
		// Replace objects on the map
		map.setObjects(objects);
		
		// Re-attach message listener (transient property)
		for (Person p : population) {
			attachMessageListener(p);
		}
		
		mainMenu.closeMenu();
		startGame();
	}
	
	private void attachPersonToGame(Person p) {
		if (p == null)
			return;
		
		objects.add(p);
		population.add(p);
		attachMessageListener(p);
	}
	
	private void attachMessageListener(Person p) {
		Game that = this;
		p.addMessageEventListener(new MessageEventListener() {
			private static final long serialVersionUID = 2371305630711900167L;
			public void messageEvent(String msg) {
				if (p == that.getActivePerson()) {
					msgZone.appendMessage(msg);
				}
			}
		});
	}
	
	public void sendMessageTo(Person p, String msg) {
		p.addMessage(msg);
	}
	
	public void sendMessageToAll(String msg) {
		for (Person p : population) {
			sendMessageTo(p, msg);
		}
	}
}
