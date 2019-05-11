package Model;

import View.Window;
import View.Map;
import View.CatalogDialog;
import View.FriendListDialog;
import View.GameMenu;
import View.InteractionMenu;
import View.InventoryDialog;
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
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Controller.ImagesFactory;
import Model.Directable.Direction;
import Model.Person.InteractionType;

public class Game implements RefreshableObserver, MessagesListener {
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private ArrayList<Person> population = new ArrayList<Person>();
	private HashMap<Person,MoveThread> moveThreadsList = new HashMap<Person,MoveThread>();
	private Person activePerson = null;
	private Window window;
	private Map map;
	private Status status;
	private MessagesZone msgZone;
	private GameMenu mainMenu;

	private GameTime gameTime;

	private boolean isRunning = false;

	public Game(Window window) {
		this.window = window;
		map = window.getMap();
		status = window.getStatus();
		msgZone = window.getMsgZone();

		gameTime = new GameTime(this, 0);

		/*
		 *  Add an invisible obstacle at the position of the bottom left corner of
		 *  the map to avoid to move objects under the minimap.
		 */
		addMinimapObstacleArtefact();
		
		window.addGameMenuButtonAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGameMenu();
			}
		});
		
		window.addFriendListButtonAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFriendListDialog();
			}
		});
		
		window.addInventoryButtonAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openInventoryDialog();
			}
		});

		mainMenu = new GameMenu(window, this);
		

		Person p1 = new Adult(new Point(10, 10), "Test Person", 8, Person.Gender.Male, null, null);
		Person p2 = new Adult(new Point(17, 13), "Second Player", 18, Person.Gender.Female, null, null);
		Person p3 = new Adult(new Point(11, 16), "Third People", 30, Person.Gender.Male, null, null);

		// Marry these persons
		((Adult) p1).setPartner((Adult) p2);
		((Adult) p2).setPartner((Adult) p1);
		
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
			if (i != 9 && i != 10 && i != 11 && i != 12) {
				attachObjectToGame(new WallBlock(20 - 1, i));
				attachObjectToGame(new WallBlock(i, 20 - 1));
			}
		}

		// A second sample of room
		for (int i = 0; i < 15; i++) {
			attachObjectToGame(new WallBlock(20 + i, 0));

			// Make a door in the wall
			if (i != 9 && i != 10 && i != 11 && i != 12) {
				attachObjectToGame(new WallBlock(20 + i, 15 - 1));
			}
		}

		// A third sample of room
		for (int i = 0; i < 25; i++) {
			attachObjectToGame(new WallBlock(35 + i, 0));
			attachObjectToGame(new WallBlock(35 - 1, i));
			attachObjectToGame(new WallBlock(35 + i, 25 - 1));

			// Make a door in the wall
			if (i != 13 && i != 14 && i != 15 && i != 16) {
				attachObjectToGame(new WallBlock(35 + 25 - 1, i));
			}
		}

		// A sample of toilet room
		for (int i = 0; i < 7; i++) {
			attachObjectToGame(new WallBlock(i, 20 + 7 - 1));
			attachObjectToGame(new WallBlock(0, 20 + i - 1));

			// Make a door in the wall
			if (i != 0 && i != 1 && i != 2 && i != 3) {
				attachObjectToGame(new WallBlock(7, 20 + i));
			}
		}
		WaterClosed wc = new WaterClosed(new Point(1, 21));
		wc.rotate(Direction.EAST);
		attachObjectToGame(wc);

		Bed bed = new Bed(new Point(1, 12));
		bed.rotate(Direction.EAST);
		attachObjectToGame(bed);

		Sofa sofa = new Sofa(new Point(23, 1));
		attachObjectToGame(sofa);

		Computer computer = new Computer(new Point(31, 5));
		computer.rotate(Direction.WEST);
		attachObjectToGame(computer);
		
		Shower shower = new Shower(new Point(1, 1));
		attachObjectToGame(shower);
		
		Bath bath = new Bath(new Point(7, 1));
		bath.rotate(Direction.EAST);
		attachObjectToGame(bath);
		
		
		/*
		 * Load all images at starting of the program
		 */
		try {
			ImagesFactory.loadAllImages();
		} catch (Exception e) {
			e.printStackTrace();
		}

		notifyView();
	}

	public Size getMapBlockSize() {
		return map.getBlockSize();
	}
	
	public Point getMapViewOffset() {
		return map.getViewOffset();
	}

	public Rect getMinimapRect() {
		return map.getMinimapRect();
	}

	public double getMinimapScale() {
		return map.getMinimapScale();
	}

	public Point getMinimapOffset() {
		return map.getMinimapOffset();
	}

	/**
	 *  This function adds an invisible obstacle at the position of the bottom left
	 *  corner of the map to avoid to move objects under the minimap.
	 */
	private void addMinimapObstacleArtefact() {
		Rect minimapRect = map.getMinimapRect();
		Rect mapRect = map.getViewRect();

		// Compute the size and the position of the minimap in the map limit
		int wm = minimapRect.getWidth() / map.getBlockSize().getWidth();
		int hm = minimapRect.getHeight() / map.getBlockSize().getHeight();
		
		double xm = 0 ;
		double ym = map.getMapSize().getHeight() - hm ;
		
		double criticalY = mapRect.getSize().getHeight() / map.getBlockSize().getHeight() - hm;
		
		ym = (criticalY > ym ? criticalY : ym);
		
		// Add the invisible obstacle to the game
		attachObjectToGame(new InvisibleObstacle(new Point(xm, ym), new Size(wm, hm)));
	}
	
	public void playerMoveEvent(Person p) {
		ArrayList<GameObject> obj_lst = p.getObjectsAround();

		for (GameObject o : obj_lst) {
			if (o instanceof Activable) {
				((Activable) o).proximityEvent(p);
			}
		}
	}

	public void mouseLeftClickEvent(Point pos) {
		GameObject object = getObjectAtPosition(pos);
		
		if (object == null || !object.isObstacle()) {
			sendPlayer(pos);
			return;
		}
		
		if (activePerson.getObjectsAround().contains(object)) {
			if (object instanceof Person && object != activePerson) {
				Person other = (Person) object;
				interractWith(other);
			}
		}

		if (object instanceof Activable && !activePerson.isLocked()) {
			((Activable) object).clickedEvent(activePerson);
		}
	}

	public void mouseRightClickEvent(Point pos) {
		GameObject object = getObjectAtPosition(pos);

		if (object != null && object instanceof Person) {
			Person selectedPerson = (Person) (object);
			setActivePerson(selectedPerson);
		}
	}
	
	public void activePlayerAction() {
		if (activePerson.isLocked())
			return;
		
		// Get the object at front of the activePerson
		Point targetPos = activePerson.getFrontPos();
		GameObject targetObject = getObjectAtPosition(targetPos);
		
		if (targetObject != null && targetObject instanceof Activable) {
			((Activable) targetObject).clickedEvent(activePerson);
		}
		else if (targetObject != null && targetObject instanceof Person) {
			interractWith((Person) targetObject);
		}
	}
	
	public void selectNextActivePerson(boolean backward) {
		if (population.size() == 0)
			return;
		
		// Get the index of the activePerson to find the next person in the list
		int index = population.indexOf(activePerson);
		
		// The increment is -1 for backward, +1 forward
		int increment = (backward ? -1 : 1);
		
		int nextIndex = index + increment;
		
		// If next index is -1 -> get the last person in the list
		if (nextIndex < 0) {
			nextIndex = population.size() - 1;
		}
		// If the next index is out of the list -> get the first person
		else if (nextIndex > population.size() - 1) {
			nextIndex = 0;
		}

		// Select the next Person in the list
		setActivePerson(population.get(nextIndex));
	}

	public GameObject getObjectAtPosition(Point pos) {
		GameObject obj = null;

		for (GameObject o : objects) {
			if (o.isAtPosition(pos)) {
				// Get important object before any GroundObject
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
		return map.getMapSize();
	}

	public void sendPlayer(Point pos) {
		// Don't act on the Person if he is locked
		if (activePerson.isLocked())
			return;
		
		// Try to place the bottom of the player on the mouse
		pos = pos.add(0, -activePerson.getSize().getHeight()+1);
		
		Rect r = new Rect(0, 0, map.getMapSize().getWidth(), map.getMapSize().getHeight());
		
		if (!r.contains(pos)) {
			pos = new Point(
					Math.max(0, Math.min(pos.getX(), map.getMapSize().getWidth()-1)),
					Math.max(0, Math.min(pos.getY(), map.getMapSize().getHeight()-1)));
		}
		
		getPlayerMoveThread(activePerson).setTargetPosition(pos);
	}

	public void moveActivePlayer(int x, int y) {
		movePlayer(activePerson, x, y);
	}

	public void moveActivePlayer(Point movement) {
		movePlayer(activePerson, movement);
	}

	public void movePlayer(Person pers, int x, int y) {
		movePlayer(pers, new Point(x, y));
	}

	public void movePlayer(Person pers, Point movement) {
		// Don't act on the Person if he is sleeping or working
		if (pers.isLocked())
			return;
		
		Point nextPos = pers.getPos().add(movement);
		
		if (!isTargetUnreachable(pers, nextPos)) {
			getPlayerMoveThread(pers).addMovement(movement);
		}
		else {
			pers.rotate(movement);
			pers.notifyRefresh();
		}
	}
	
	private MoveThread getPlayerMoveThread(Person p) {
		MoveThread mt = moveThreadsList.getOrDefault(p, null);
		
		// If the Person hasn't any MoveThread yet, create it and run it in a new Thread
		if (mt == null) {
			mt = new MoveThread(this, p);
			
			Thread t = new Thread(mt);
			t.start();
			
			moveThreadsList.put(p, mt);
		}
		
		return mt;
	}
	
	public boolean isTargetUnreachable(Person pers, Point pos) {
		boolean unreachable = false;

		// Check if the nextPos is in the map
		Rect mapRect = new Rect(new Point(0, 0), map.getMapSize());
		Rect nextRect = new Rect(pos, pers.getSize());
		
		unreachable = !mapRect.contains(nextRect);

		// Check if the nextPos overlaps any obstacle
		for (GameObject object : objects) {
			if (object == (GameObject) pers)
				continue;

			if (object.isObstacle() && object.getRect().overlaps(nextRect)) {
				unreachable = true;
				break;
			}
		}
		
		return unreachable;
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

		centerViewOnPlayer();
		notifyView();
	}

	public void loadGameMapPacket(GameMapPacket gmp) {
		map.setMapSize(gmp.getMapSize());
		
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
			p.attachMessagesListener(this);
		}
		
		// Re-attach refresh listener (transient property)
		for (Person p : population) {
			p.attachRefreshableObserver(this);
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
	
	public void openFriendListDialog() {
		pauseGame();
		FriendListDialog friendListDialog = new FriendListDialog(window, getActivePerson());
		friendListDialog.showDialog();
		resumeGame();
	}

	public void openInventoryDialog() {
		pauseGame();
		InventoryDialog inventoryDialog = new InventoryDialog(window, getActivePerson(), this);
		inventoryDialog.showDialog();
		resumeGame();
	}
	
	public void openCatalogDialog() {
		pauseGame();
		CatalogDialog catalogDialog = new CatalogDialog(window, getActivePerson());
		catalogDialog.showDialog();
		resumeGame();
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

		// WARNING: The order is very important and must be the same as the restoring order !
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

		restoreFromFile(chooser.getSelectedFile().getPath());
	}

	public void openGame() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier de carte de jeu", "map");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Ouvrir un fichier de carte de jeu");
		int returnVal = chooser.showOpenDialog(window);

		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}

		restoreFromFile(chooser.getSelectedFile().getPath());
	}
	
	private void restoreFromFile(String filepath) {
		gameTime.stop();
		gameTime.cancel();

		ObjectRestorer restorer = new ObjectRestorer(filepath);

		// WARNING: The order is very important and must be the same as the saving order !
		GameMapPacket mapPacket = (GameMapPacket) (restorer.readNextObjectFromSave());

		restorer.closeSaveFile();
		
		if (mapPacket == null) {
			JOptionPane.showMessageDialog(window,
				    "Impossible de charger le fichier sélectionné",
				    "Erreur",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}

		loadGameMapPacket(mapPacket);

		mainMenu.closeMenu();
		startGame();
	}

	public GameMapPacket getGameMapPack() {
		return new GameMapPacket(
				map.getMapSize(),
				objects, population,
				activePerson,
				gameTime.getTimeFromStart());
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
		
		p.attachRefreshableObserver(this);
		p.attachMessagesListener(this);
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

	public void refresh(Refreshable r) {
		centerViewOnPlayer();
		map.redraw();
		
		if (r instanceof Person) {
			playerMoveEvent((Person) r);
		}
	}

	@Override
	public void messageEvent(MessagesSender sender, Message msg) {
		if (sender instanceof Person && sender == getActivePerson()) {
			msgZone.appendMessage(msg);
		}
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
		// Don't interact with a sleeping or working people...
		if (other.isLocked() || activePerson.isLocked()) {
			// Show a message if we try to call a locked Person
			if (other.isLocked()) {
				activePerson.addMessage(
						"Cette personne est occupée. Réessayez plus tard",
						MsgType.Problem);
			}
			return;
		}
		
		// Rotate Persons face-to-face
		activePerson.rotateToObjectDirection(other);
		other.rotateToObjectDirection(activePerson);
		
		notifyView();
		
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
