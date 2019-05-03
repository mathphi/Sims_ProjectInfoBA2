package Model;

import View.EditorMenu;
import View.EditorPanel;
import View.Map;
import View.NewPersonForm;
import View.Window;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import Model.Directable.Direction;
import Model.Person.Gender;
import Tools.ObjectRestorer;
import Tools.ObjectSaver;
import Tools.Point;
import Tools.Rect;
import Tools.Size;

public class Editor {
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private ArrayList<Person> population = new ArrayList<Person>();

	private Window window;
	private Map map;
	private EditorPanel editorPanel;
	private EditorMenu mainMenu;
	
	private boolean isActive = false;
	private boolean controlKeyPressed = false;
	
	private GameObject currentPlacing = null;
	private Person activePerson = null;
	
	public Editor(Window window) {
		this.window = window;
		this.map = window.getMap();
		this.editorPanel = window.getEditorPanel();

		mainMenu = new EditorMenu(window, this);
		
		window.addEditorMenuButtonAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openEditorMenu();
			}
		});
		
		editorPanel.addPlacementActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPlacing != null) {
					objects.remove(currentPlacing);
				}
				try {
					// Event id 0 is for « add ground object to map » action
					// Event id 1 is for « add furniture object to map » action
					// Event id 2 is for « add person to map » action
					if (e.getID() == 0) {
						List<String> args = Arrays.asList(e.getActionCommand().split("\t"));

						int typeArg = 0;
						if (args.size() > 1) {
							typeArg = Integer.valueOf(args.get(1));
						}
						
						// Place the new object outside the map to be invisible (he will be moved to the mouse later)
						currentPlacing = (GameObject) Class.forName(args.get(0))
								.getDeclaredConstructor(Point.class, int.class)
								.newInstance(new Point(-100,-100), typeArg);
					}
					else if (e.getID() == 1) {
						// Place the new object outside the map to be invisible (he will be moved to the mouse later)
						currentPlacing = (GameObject) Class.forName(e.getActionCommand())
								.getDeclaredConstructor(Point.class)
								.newInstance(new Point(-100,-100));
					}
					else if (e.getID() == 2) {
						// Place a Person object
						// Ask user for Person informations (name,...) by using the NewPersonForm
						NewPersonForm form = new NewPersonForm(window, population);
						boolean ans = form.showForm();
						
						// If form has been aborted
						if (!ans)
							return;

						currentPlacing = (GameObject) Class.forName(e.getActionCommand())
								.getDeclaredConstructor(
										Point.class,
										String.class,
										int.class,
										Gender.class,
										Adult.class,
										Adult.class)
								.newInstance(
										new Point(-100,-100),
										form.getName(),
										form.getAge(),
										form.getGender(),
										form.getFather(),
										form.getMother());
						
						Person p = (Person) currentPlacing;
						p.setPlayable(form.getPlayable());
						
						population.add(p);
					}
					addObject(currentPlacing);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		notifyView();
	}

	private void notifyView() {
		window.update();
	}
	
	public boolean isActive() {
		return isActive;
	}

	public void closeEditorMenu() {
		mainMenu.closeMenu();
	}

	public void openEditorMenu() {
		mainMenu.showMenu();
	}
	
	public void resetEditor() {
		objects = new ArrayList<GameObject>();
		population = new ArrayList<Person>();
		activePerson = null;
		
		map.setObjects(objects);
		
		map.scrollRectToVisible(new Rectangle(0,0,1,1));
		
		notifyView();
	}
	
	public void start() {
		this.isActive = true;
		
		window.switchEditorMode();
		
		resetEditor();
	}
	
	public void stop() {
		this.isActive = false;
	}
	
	public Size getMapBlockSize() {
		return map.getBlockSize();
	}
	
	public void mouseLeftClickEvent(Point pos) {
		if (currentPlacing != null) {
			if (currentPlacing.getPos().getDistance(pos) > 2.0 ||
				!map.getRect().contains(currentPlacing.getPos()))
			{
				// Don't place the object if it is far the mouse pointer
				return;
			}
			if (controlKeyPressed && !(currentPlacing instanceof Person)) {
				// Place another object of same type if Ctrl is pressed
				// Don't add twice the same Person !
				try {
					currentPlacing = (GameObject) currentPlacing.clone();
					currentPlacing.setPos(new Point(-100, -100));
					addObject(currentPlacing);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				currentPlacing = null;
			}
			notifyView();
			return;
		}
		
		// If we are not placing an object and the clicked object is a Person instance
		GameObject clickedObject = getObjectAtPosition(pos);
		
		if (clickedObject instanceof Person) {
			// If clickedObject is null, we just select nobody as activePerson
			Person p = (Person) clickedObject;
			setActivePerson(p);
		}
		else {
			setActivePerson(null);
		}
	}
	
	public void mouseRightClickEvent(Point pos) {
		if (currentPlacing != null) {
			// Cancel current placement on right click
			objects.remove(currentPlacing);
			currentPlacing = null;
			notifyView();
			return;
		}
		
		GameObject obj = getObjectAtPosition(pos);
		
		if (obj != null) {
			// Remove object under cursor on right click
			objects.remove(obj);
			notifyView();
		}
	}
	
	public void mouseMoveEvent(Point pos) {
		if (currentPlacing != null) {
			Rect r = new Rect(pos, currentPlacing.getSize());
			
			// Accept only free places (or any place if GroundObject)
			if (currentPlacing instanceof GroundObject || rectIsPlacable(r)) {
				currentPlacing.setPos(pos);
				notifyView();
			}
			return;
		}
	}
	
	public void mouseExitedEvent() {
		if (currentPlacing != null) {
			// Move the object outside the map (to hide it)
			currentPlacing.setPos(new Point(-100, -100));
			notifyView();
		}
	}
	
	public void setControlKeyPressed(boolean pressed) {
		controlKeyPressed = pressed;
	}
	
	public void moveView(int dx, int dy) {
		java.awt.Point l = map.getVisibleRect().getLocation();
		l.translate(dx * getMapBlockSize().getWidth(),
					dy * getMapBlockSize().getHeight());
		
		Rectangle r = map.getVisibleRect();
		r.setLocation(l);
		
		map.scrollRectToVisible(r);
	}
	
	public void addObject(GameObject o) {
		if (o == null)
			return;
		
		objects.add(o);
		o.setMapObjectsList(objects);
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
	
	public boolean positionIsObstacle(Point pos) {
		boolean isObstacle = false;
		
		for (GameObject o : objects) {
			if (o.isAtPosition(pos) && o.isObstacle()) {
				isObstacle = true;
			}
		}
		
		return isObstacle;
	}
	
	public boolean rectIsPlacable(Rect r) {
		boolean isPlacable = true;
		
		for (GameObject o : objects) {
			if (o != currentPlacing && o.isObstacle() && r.overlaps(o.getRect())) {
				isPlacable = false;
			}
		}
		
		return isPlacable;
	}

	public void quit() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}

	public void saveMap() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Fichier de carte de jeu", "map");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Destination du fichier de carte de jeu");
		int returnVal = chooser.showSaveDialog(window);
		
	    if (returnVal != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }
	    
	    String path = chooser.getSelectedFile().getPath();
	    
	    // Add .map if not added automatically
	    if (!path.endsWith(".map")) {
	    	path += ".map";
	    }
		
		ObjectSaver saver = new ObjectSaver(path);
		
		// WARNING: The order is very important and must be the same as the restoring order !
		saver.addObjectToSave(getGameMapPacket());
		saver.writeSaveToFile();
	}
	
	public void loadMap() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Fichier de carte de jeu", "map");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Ouvrir un fichier de carte de jeu");
		int returnVal = chooser.showOpenDialog(window);
		
	    if(returnVal != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }
		
		ObjectRestorer restorer = new ObjectRestorer(chooser.getSelectedFile().getPath());
		
		// WARNING: The order is very important and must be the same as the saving order !
		GameMapPacket mapPacket = (GameMapPacket)(restorer.readNextObjectFromSave());
		
		restorer.closeSaveFile();
		
		objects = mapPacket.getObjects();
		population = mapPacket.getPopulation();
		activePerson = mapPacket.getActivePerson();
		
		// Replace objects on the map
		map.setObjects(objects);
		
		mainMenu.closeMenu();
		
		notifyView();
	}
	
	public void setStartGameAction(ActionListener a) {
		mainMenu.setStartGameAction(a);
	}
	
	public GameMapPacket getGameMapPacket() {
		// Remove objects that are outside the map
		for (int i = 0 ; i < objects.size() ; i++) {
			GameObject o = objects.get(i);
			
			if (!map.getRect().contains(o.getPos())) {
				objects.remove(i);
				i--;
			}
		}
		
		return new GameMapPacket(objects, population, activePerson);
	}

	public void setActivePerson(Person p) {
		if (p != null && !p.isPlayable())
			return;
		
		activePerson = p;

		for (Person people : population) {
			people.setActivePerson(people == p);
		}
		
		notifyView();
	}
	
	public void rotatePlacement() {
		if (currentPlacing != null) {
			Direction d = Direction.EAST;
			
			switch (currentPlacing.getDirection()) {
			case NORTH:
				d = Direction.EAST;
				break;
			case EAST:
				d = Direction.SOUTH;
				break;
			case SOUTH:
				d = Direction.WEST;
				break;
			case WEST:
				d = Direction.NORTH;
				break;
			}
			
			currentPlacing.rotate(d);
			notifyView();
		}
	}
}
