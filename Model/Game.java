package Model;

import View.Window;
import View.Map;
import View.GameMenu;
import View.InteractionMenu;
import View.InteractionMenu.InteractionType;
import View.Message;
import View.Message.MsgType;
import View.MessagesZone;
import View.Status;
import Tools.ObjectRestorer;
import Tools.ObjectSaver;
import Tools.Point;
import Tools.Size;
import Tools.Rect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import Model.Directable.Direction;

public class Game implements DeletableObserver {

	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private ArrayList<Person> population = new ArrayList<Person>();
	private Person activePerson = null;
	private Window window;
	private Map map;
	private Status status;
	private MessagesZone msgZone;
	private GameMenu mainMenu;
	private Size mapSize;

	private GameTime gameTime;

	private boolean isRunning = false;

	public Game(Window window) {
		this.window = window;
		map = window.getMap();
		status = window.getStatus();
		msgZone = window.getMsgZone();
		mapSize = map.getMapSize();

		gameTime = new GameTime(this, 0);

		window.addGameMenuButtonAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGameMenu();
			}
		});

		mainMenu = new GameMenu(window, this);

		Person p1 = new Kid(new Point(10, 10), "Test Person", 8, Person.Gender.Male, null, null);
		Person p2 = new Adult(new Point(17, 13), "Second Player", 11, Person.Gender.Female, null, null);
		Person p3 = new Adult(new Point(11, 16), "Third People", 43, Person.Gender.Female, null, null);

		attachPersonToGame(p1);
		attachPersonToGame(p2);
		attachPersonToGame(p3);
		setActivePerson(p1);

		// Map building
		// A sample of room
		for (int i = 0; i < 20; i++) {
			attachObjectToGame(new WallBlock(i, 0));
			attachObjectToGame(new WallBlock(0, i));

			// Make a door in the wall
			if (i != 10 && i != 11) {
				attachObjectToGame(new WallBlock(20 - 1, i));
				attachObjectToGame(new WallBlock(i, 20 - 1));
			}
		}

		// A second sample of room
		for (int i = 0; i < 15; i++) {
			attachObjectToGame(new WallBlock(20 + i, 0));

			// Make a door in the wall
			if (i != 10 && i != 11) {
				attachObjectToGame(new WallBlock(20 + i, 15 - 1));
			}
		}

		// A third sample of room
		for (int i = 0; i < 25; i++) {
			attachObjectToGame(new WallBlock(35 + i, 0));
			attachObjectToGame(new WallBlock(35 - 1, i));
			attachObjectToGame(new WallBlock(35 + i, 25 - 1));

			// Make a door in the wall
			if (i != 15 && i != 16) {
				attachObjectToGame(new WallBlock(35 + 25 - 1, i));
			}
		}

		// A sample of toilet room
		for (int i = 0; i < 7; i++) {
			attachObjectToGame(new WallBlock(i, 20 + 7 - 1));
			attachObjectToGame(new WallBlock(0, 20 + i - 1));

			// Make a door in the wall
			if (i != 2 && i != 3) {
				attachObjectToGame(new WallBlock(7, 20 + i));
			}
		}
		WaterClosed wc = new WaterClosed(new Point(1, 22));
		attachObjectToGame(wc);

		Bed bed = new Bed(new Point(1, 12));
		attachObjectToGame(bed);

		notifyView();
	}

	public Size getMapBlockSize() {
		return map.getBlockSize();
	}
	
	public Point getMapViewOffset() {
		return map.getViewOffset();
	}

	public void playerMoveEvent(Person p) {
		ArrayList<GameObject> obj_lst = p.getObjectsAround();

		for (GameObject o : obj_lst) {
			o.proximityEvent(p);
		}
	}

	public void mouseLeftClickEvent(Point pos) {
		GameObject object = getObjectAtPosition(pos);

		if (object == null || !object.isObstacle()) {
			sendPlayer(pos);
		}
		
		else if (activePerson.getObjectsAround().contains(object)) {
			object.clickedEvent(activePerson);
			
			if (object instanceof Person && object != activePerson) {
				Person other = (Person) object;
				interractWith(other);
			}
		}
	}

	public void mouseRightClickEvent(Point pos) {
		GameObject object = getObjectAtPosition(pos);

		if (object != null && object instanceof Person) {
			Person selectedPerson = (Person) (object);
			setActivePerson(selectedPerson);
		}
	}

	public GameObject getObjectAtPosition(Point pos) {
		GameObject obj = null;

		for (GameObject o : objects) {
			if (o.isAtPosition(pos)) {
				if (obj == null || (obj instanceof GroundObject)) {
					obj = o;
				}
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

	public void sendPlayer(Point pos) {
		Rect r = new Rect(0, 0, (int)map.getSize().getWidth(), (int)map.getSize().getHeight());
		
		if (r.contains(pos)) {
			Thread t = new Thread(new AStarThread(this, activePerson, pos));
			t.start();
		}
		else {
			activePerson.setPos(pos);
		}
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
			if (object == (GameObject) pers) {
				continue;
			}
			
			Rect nextRect = new Rect(nextPos, pers.getSize());

			if (object.getRect().overlaps(nextRect) && object.isObstacle()) {
				unreachable = true;
				break;
			}
		}

		int x = pos.getXInt();
		int y = pos.getYInt();
		Direction direction = Direction.EAST;

		if (x == 0 && y == -1)
			direction = Direction.NORTH;
		else if (x == 0 && y == 1)
			direction = Direction.SOUTH;
		else if (x == 1 && y == 0)
			direction = Direction.EAST;
		else if (x == -1 && y == 0)
			direction = Direction.WEST;

		pers.rotate(direction);
		
		if (!unreachable) {
			pers.move(pos);
		}
	}

	public void centerViewOnPlayer() {
		if (activePerson == null)
			return;
		
		map.scrollToObject(activePerson);
	}

	private void notifyView() {
		window.update();
	}

	public ArrayList<GameObject> getGameObjects() {
		return this.objects;
	}

	@Override
	synchronized public void delete(Deletable ps, ArrayList<GameObject> loot) {
		objects.remove((GameObject) ps);
		if (loot != null) {
			attachObjectsToGame(loot);
		}
		notifyView();
	}

	public void quit() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}

	private void updateAllPopulation() {
		// We cannot use the 'foreach' structure here because population
		// can be modified in updatePerson() resulting in a
		// ConcurrentModificationException
		for (int i = 0; i < population.size(); i++) {
			Person p = population.get(i);
			updatePerson(p);
		}
	}

	private void updatePerson(Person p) {
		if (p == null)
			return;

		p.update(gameTime.getVirtualTime());

		// If this Person reached the age to evolve to the next Person level :
		// Create a new object of the new level with the same properties to replace this
		// Person.
		if (p.maxAgeReached()) {
			if (p instanceof Adult) {
				// TODO: He dies...
				p.addMessage("Vous êtes mort !", MsgType.Problem);
			} else if (p instanceof Teenager) {
				Person newPers = new Adult(p);
				replacePerson(p, newPers);
				newPers.addMessage("Vous êtes maintenant un adulte !", MsgType.Info);
			} else if (p instanceof Kid) {
				Person newPers = new Teenager(p);
				replacePerson(p, newPers);
				newPers.addMessage("Vous êtes maintenant un adolescent !", MsgType.Info);
			}
		}
	}

	private void replacePerson(Person from, Person to) {
		removePersonFromGame(from);
		attachPersonToGame(to);

		//
		if (from.isActivePerson()) {
			setActivePerson(to);
		}
	}

	public void updateGame() {
		// Update the time string in the status area
		status.setGameTimeStr(gameTime.getCurrentTimeString());

		long t = gameTime.getVirtualTime();

		// New year
		if (t % 365 == 0) {

		}
		// New week (7 days...)
		if (t % 7 == 0) {

		}

		updateAllPopulation();

		notifyView();
	}

	public void setActivePerson(Person p) {
		if (p != null && !p.isPlayable())
			return;

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

	public void loadGameMapPacket(GameMapPacket gmp) {
		objects = gmp.getObjects();
		population = gmp.getPopulation();
		activePerson = gmp.getActivePerson();
		gameTime = new GameTime(this, gmp.getTimeFromStart());

		// Replace objects on the map
		map.setObjects(objects);

		// Update activePerson status,...
		setActivePerson(activePerson);

		// Re-attach message listener (transient property)
		for (Person p : population) {
			attachMessageListener(p);
		}
		
		// Re-attach refresh listener (transient property)
		for (Person p : population) {
			attachRefreshListener(p);
		}

		notifyView();
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void pauseGame() {
		isRunning = false;
		gameTime.stop();
	}

	public void resumeGame() {
		isRunning = true;
		gameTime.start();

	}

	public void stopGame() {
		isRunning = false;
		gameTime.cancel();
	}

	public void startGame() {
		window.switchGameMode();
		isRunning = true;
		map.setObjects(this.getGameObjects());

		gameTime.start();
		status.setGameTimeStr(gameTime.getCurrentTimeString());

		// Select the first person found in the game if none has been selected
		if (activePerson == null) {
			selectDefaultActivePerson();
		}

		centerViewOnPlayer();
		notifyView();
	}

	public void openGameMenu() {
		pauseGame();
		mainMenu.showMenu();
	}

	public void closeGameMenu() {
		mainMenu.closeMenu();
	}

	public void saveGame() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier de sauvegarde", "sav");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Destination du fichier de sauvegarde");
		int returnVal = chooser.showSaveDialog(window);

		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}

		String path = chooser.getSelectedFile().getPath();

		// Add .sav if not added automatically
		if (!path.endsWith(".sav")) {
			path += ".sav";
		}

		ObjectSaver saver = new ObjectSaver(path);

		// WARNING: The order is very important and must be the same as the restoring
		// order !
		saver.addObjectToSave(getGameMapPack());
		saver.writeSaveToFile();
	}

	public void restoreGame() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier de sauvegarde", "sav");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Ouvrir un fichier de sauvegarde");
		int returnVal = chooser.showOpenDialog(window);

		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}

		gameTime.stop();
		gameTime.cancel();

		ObjectRestorer restorer = new ObjectRestorer(chooser.getSelectedFile().getPath());

		// WARNING: The order is very important and must be the same as the saving order
		// !
		GameMapPacket mapPacket = (GameMapPacket) (restorer.readNextObjectFromSave());

		restorer.closeSaveFile();

		loadGameMapPacket(mapPacket);

		mainMenu.closeMenu();
		startGame();
	}

	public GameMapPacket getGameMapPack() {
		return new GameMapPacket(objects, population, activePerson, gameTime.getTimeFromStart());
	}

	private void attachObjectsToGame(ArrayList<GameObject> lst) {
		for (GameObject o : lst) {
			attachObjectToGame(o);
		}
	}

	private void attachObjectToGame(GameObject o) {
		objects.add(o);
		o.setMapObjectsList(objects);
	}

	private void removeObjectFromGame(GameObject o) {
		objects.remove(o);
	}

	private void attachPersonToGame(Person p) {
		if (p == null)
			return;

		population.add(p);
		attachObjectToGame(p);
		attachRefreshListener(p);
		attachMessageListener(p);
	}

	private void removePersonFromGame(Person p) {
		if (p == null)
			return;

		population.remove(p);
		removeObjectFromGame(p);

		if (p.isActivePerson()) {
			selectDefaultActivePerson();
		}
	}

	private void selectDefaultActivePerson() {
		for (Person p : population) {
			if (p.isPlayable) {
				setActivePerson(p);
				break;
			}
		}
	}

	private void attachRefreshListener(Person p) {
		p.addRefreshListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				centerViewOnPlayer();
				map.redraw();
				playerMoveEvent(p);
			}
		});
	}

	private void attachMessageListener(Person p) {
		Game that = this;
		p.addMessageEventListener(new MessageEventListener() {
			private static final long serialVersionUID = 2371305630711900167L;

			public void messageEvent(Message msg) {
				if (p == that.getActivePerson()) {
					msgZone.appendMessage(msg);
				}
			}
		});
	}

	public void sendMessageTo(Person p, Message msg) {
		p.addMessage(msg);
	}

	public void sendMessageToAll(Message msg) {
		for (Person p : population) {
			sendMessageTo(p, msg);
		}
	}

	public void setCreatorAction(ActionListener a) {
		mainMenu.setCreatorAction(a);
	}
	
	public void interractWith(Person other) {
		InteractionType interract_type = InteractionMenu.showInterractionMenu(
				window, other.getName(),
				activePerson.getRelationship(other),
				activePerson,
				other);
		
		if (interract_type == InteractionType.None)
			return;
		
		activePerson.characterInteraction(other, interract_type);
	}
}
