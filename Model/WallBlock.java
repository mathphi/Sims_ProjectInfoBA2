package Model;

import java.awt.Color;
import java.awt.Graphics;

import Tools.Point;
import Tools.Size;

public class WallBlock extends GameObject {
	private static final long serialVersionUID = -1900225227739246245L;
	
	private int type = 1;

	public WallBlock(int x, int y) {
		this(new Point(x, y));
	}

	public WallBlock(int x, int y, int type) {
		this(new Point(x, y));
		this.setSize(new Size(type, 1));
		this.type = type;
	}

	public WallBlock(Point pos, int type) {
		this(pos);
		this.setSize(new Size(type, 1));
		this.type = type;
	}
	
	public WallBlock(Point pos) {
		super(pos, new Size(1,1), Color.DARK_GRAY);
	}

	public boolean isObstacle() {
		return true;
	}
	
	// A wall does nothing
	public void clickedEvent(GameObject o) {}
	
	// A wall does nothing
	public void proximityEvent(GameObject o) {}
	
	public GameObject clone() {
		WallBlock clone = new WallBlock(getPos(), type);
		clone.rotate(this.getDirection());
		return (GameObject) clone;
	}
	
	@Override
	public void paint(Graphics g, int BLOC_SIZE) {
		g.setColor(getColor());
        g.fillRect(
        		(int)(getPos().getX() * BLOC_SIZE-1),
        		(int)(getPos().getY() * BLOC_SIZE-1),
        		(BLOC_SIZE * getSize().getWidth()),
        		(BLOC_SIZE * getSize().getHeight()));
	}
}
