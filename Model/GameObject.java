package Model;

import Tools.Point;
import Tools.Size;
import Tools.Rect;

import java.awt.Color;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class GameObject implements Serializable {
	private static final long serialVersionUID = 5238309657819264811L;
	
	private Rect rect;
	private Color color;
	private ArrayList<GameObject> allMapObjects = new ArrayList<GameObject>();

	public GameObject(Point pos, Size size, Color color) {
		this.rect = new Rect(pos, size);
		this.color = color;
	}

	public Point getPos() {
		return this.rect.getPos();
	}

	public Size getSize() {
		return this.rect.getSize();
	}
	
	public Rect getRect() {
		return this.rect;
	}
	
	//TODO: remove this...
	public Color getColor() {
		return color;
	}

	public void setPos(Point p) {
		rect.setPos(p);
	}

	public void setSize(Size sz) {
		rect.setSize(sz);
	}
	
	public boolean isAtPosition(Point p) {
		return rect.contains(p);
	}

	public ArrayList<GameObject> getObjectsAround() {
		// Create a rectangle a bit larger than the object
		Rect test_rect = new Rect(rect.getPos().add(-1,-1), rect.getSize().add(2,2));
		
		ArrayList<GameObject> lst = new ArrayList<GameObject>();
		
		// Test all objects and keep these that overlaps the test_rect
		for (GameObject o : allMapObjects) {
			if (o == this)
				continue;
			
			if (o.getRect().overlaps(test_rect)) {
				lst.add(o);
			}
		}

		return lst;
	}

	public void setMapObjectsList(ArrayList<GameObject> objects) {
		allMapObjects = objects;
	}
	
	public abstract boolean isObstacle();
	
	public abstract void clickedEvent();
	
	public abstract void proximityEvent(GameObject o);
}