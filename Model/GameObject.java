package Model;

import Tools.Point;
import Tools.Size;
import Tools.Rect;

public abstract class GameObject {
	private Rect rect;
	private int color;


	public GameObject(Point pos, Size size) {
		this.rect = new Rect(pos, size);
		this.color = 0;
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
	public int getColor() {
		return color;
	}

	public boolean isAtPosition(Point p) {
		return this.rect.getPos().getX() == p.getX() &&
				this.rect.getPos().getY() == p.getY();
	}

	public abstract boolean isObstacle();
}