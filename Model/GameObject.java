package Model;

import Tools.ImagesFactory;
import Tools.Point;
import Tools.Size;
import Tools.Rect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class GameObject implements Directable, Serializable {
	private static final long serialVersionUID = 5238309657819264811L;

	private Direction direction = Direction.SOUTH;

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

		if (direction == Direction.EAST || direction == Direction.WEST) {
			s = s.permuted();
		}

		return s;
	}

	public Rect getRect() {
		Rect r = rect;

		if (direction == Direction.EAST || direction == Direction.WEST) {
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
		if (direction == Direction.EAST || direction == Direction.WEST) {
			sz = sz.permuted();
		}

		rect.setSize(sz);
	}

	public boolean isAtPosition(Point p) {
		return getRect().contains(p);
	}

	public ArrayList<GameObject> getObjectsAround() {
		// Create a rectangle a bit larger than the object
		Rect test_rect = new Rect(getRect().getPos().add(-0.5, -0.5), getRect().getSize().add(1, 1));

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

	public abstract void clickedEvent(GameObject o);

	public abstract void proximityEvent(GameObject o);

	public abstract GameObject clone();
	
	private String getDirectionLetter() {
		String letter = "S";
		
		switch (direction) {
		case NORTH:
			letter = "N";
			break;
		case EAST:
			letter = "E";
			break;
		case WEST:
			letter = "W";
			break;
		case SOUTH:
			letter = "S";
			break;
		default:
			break;
		}
		
		return letter;
	}
	
	public BufferedImage getCurrentImage() {
		String imgID = String.format("%s_%s", this.getClass().getSimpleName(), getDirectionLetter());
		BufferedImage img = ImagesFactory.getImage(imgID);
		
		// Try to get the SOUTH image (default image)
		if (img == null) {
			imgID = String.format("%s_S", this.getClass().getSimpleName());
			img = ImagesFactory.getImage(imgID);
		}
		
		return img;
	}

	public void paint(Graphics g, int BLOC_SIZE) {
		BufferedImage img = getCurrentImage();
		
		if (img != null) {
			g.drawImage(
					img,
					(int)(getPos().getX() * BLOC_SIZE),
					(int)(getPos().getY() * BLOC_SIZE),
					getSize().getWidth() * BLOC_SIZE,
					getSize().getHeight() * BLOC_SIZE,
					null);
		}
		else {
			g.setColor(color);
			g.fillRect(
					(int)(getPos().getX() * BLOC_SIZE),
					(int)(getPos().getY() * BLOC_SIZE),
					(BLOC_SIZE * getSize().getWidth()) - 2,
					(BLOC_SIZE * getSize().getHeight()) - 2);
	
			g.setColor(Color.BLACK);
	
			g.drawRect(
					(int)(getPos().getX() * BLOC_SIZE),
					(int)(getPos().getY() * BLOC_SIZE),
					(BLOC_SIZE * getSize().getWidth()) - 2,
					(BLOC_SIZE * getSize().getHeight()) - 2);
		}
	}
}