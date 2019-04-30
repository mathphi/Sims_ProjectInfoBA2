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

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import Model.Person.Gender;
import Tools.ObjectRestorer;
import Tools.ObjectSaver;
import Tools.Point;
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
				mainMenu.showMenu();
			}
		});
		
		editorPanel.addPlacementActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPlacing != null) {
					objects.remove(currentPlacing);
				}
				try {
					if (e.getID() == 1) {
						currentPlacing = (GameObject) Class.forName(e.getActionCommand())
								.getDeclaredConstructor(Point.class).newInstance(new Point(0,0));
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
								.getDeclaredConstructor(Point.class, String.class, Gender.class, Adult.class, Adult.class)
								.newInstance(
										new Point(0,0),
										form.getName(),
										form.getGender(),
										form.getFather(),
										form.getMother());
						
						population.add((Person) currentPlacing);
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

	public void start() {
		this.isActive = true;
		map.setObjects(objects);
	}
	
	public void stop() {
		this.isActive = false;
	}
	
	public Size getMapBlockSize() {
		return map.getBlockSize();
	}
	
	public void mouseLeftClickEvent(Point pos) {
		if (currentPlacing != null) {
			if (currentPlacing.getPos().getDistance(pos) > 2.0) {
				// Don't place the object if it is far the mouse pointer
				return;
			}
			if (controlKeyPressed) {
				// Place another object of same type if Ctrl is pressed
				try {
					currentPlacing = currentPlacing.getClass()
						.getDeclaredConstructor(Point.class).newInstance(new Point(0,0));
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
			if (!positionIsObstacle(pos)) {
				currentPlacing.setPos(pos);
				notifyView();
			}
			return;
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
	
	public void openEditorMenu() {
		
	}
	
	public void addObject(GameObject o) {
		if (o == null)
			return;
		
		objects.add(o);
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
	
	public boolean positionIsObstacle(Point pos) {
		boolean isObstacle = false;
		
		for (GameObject o : objects) {
			if (o.isAtPosition(pos) && o.isObstacle()) {
				isObstacle = true;
			}
		}
		
		return isObstacle;
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
	    
	    // Add .sav if not added automatically
	    if (!path.endsWith(".map")) {
	    	path += ".map";
	    }
		
		ObjectSaver saver = new ObjectSaver(path);
		
		// WARNING: The order is very important and must be the same as the restoring order !
		saver.addObjectToSave(objects);
		saver.addObjectToSave(population);
		saver.addObjectToSave(activePerson);
		saver.addObjectToSave((long) 0);
		saver.writeSaveToFile();
	}
	
	@SuppressWarnings("unchecked")
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
		objects = (ArrayList<GameObject>)(restorer.readNextObjectFromSave());
		population = (ArrayList<Person>)(restorer.readNextObjectFromSave());
		activePerson = ((Person)(restorer.readNextObjectFromSave()));
		
		restorer.closeSaveFile();
		
		// Replace objects on the map
		map.setObjects(objects);
		
		mainMenu.closeMenu();
		
		notifyView();
	}
}
