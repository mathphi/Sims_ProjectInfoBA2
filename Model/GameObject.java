package Model;

import Tools.Point;
import Tools.Size;
import Tools.Rect;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class GameObject implements Directable, Serializable {
	private static final long serialVersionUID = 5238309657819264811L;

	private Direction direction = Direction.EAST;

	private Rect rect;
	private Color color;
	private ArrayList<GameObject> allMapObjects = new ArrayList<GameObject>();

	public GameObject(Point pos, Size size, Color color) {
		this.rect = new Rect(pos, size);
		this.color = color;
	}

	public Direction getDirection() {
		return direction;
	}

	public void rotate(Direction d) {
		direction = d;
	}

	public Point getPos() {
		return this.rect.getPos();
	}

	public Size getSize() {
		Size s = rect.getSize();
		
		if (direction == Direction.NORTH || direction == Direction.SOUTH) {
			s = s.permuted();
		}
		
		return s;
	}
	
	public Rect getRect() {
		Rect r = rect;

		if (direction == Direction.NORTH || direction == Direction.SOUTH) {
			r = r.permuted();
		}
		
		return r;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		color = c;
	}

	public void setPos(Point p) {
		rect.setPos(p);
	}

	public void setSize(Size sz) {
		if (direction == Direction.NORTH || direction == Direction.SOUTH) {
			sz = sz.permuted();
		}
		
		rect.setSize(sz);
	}
	
	public boolean isAtPosition(Point p) {
		return getRect().contains(p);
	}

	public ArrayList<GameObject> getObjectsAround() {
		// Create a rectangle a bit larger than the object
		Rect test_rect = new Rect(getRect().getPos().add(-1,-1), getRect().getSize().add(2,2));
		
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
	
	public abstract GameObject clone();
	
	public void paint(Graphics g, int BLOC_SIZE) {
        g.setColor(color);
        g.fillRect(
        		getPos().getX() * BLOC_SIZE,
        		getPos().getY() * BLOC_SIZE,
        		(BLOC_SIZE * getSize().getWidth()) - 2,
        		(BLOC_SIZE * getSize().getHeight()) - 2);

        g.setColor(Color.BLACK);

        g.drawRect(
        		getPos().getX() * BLOC_SIZE,
        		getPos().getY() * BLOC_SIZE,
        		(BLOC_SIZE * getSize().getWidth()) - 2,
        		(BLOC_SIZE * getSize().getHeight()) - 2);
	}
}