package Model;

import Tools.Point;
import Tools.Size;
import Tools.Rect;

import java.awt.Color;

public abstract class GameObject {
	private Rect rect;
	private Color color;


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
		return this.rect.getPos().getX() == p.getX() &&
				this.rect.getPos().getY() == p.getY();
	}

	public abstract boolean isObstacle();
	
	public abstract void clickedEvent();
}