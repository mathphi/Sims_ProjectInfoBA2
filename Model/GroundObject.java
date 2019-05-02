package Model;

import java.awt.Color;
import java.awt.Graphics;

import Tools.Point;
import Tools.Size;

public abstract class GroundObject extends GameObject {
	private static final long serialVersionUID = -5500377942506217655L;

	public GroundObject(Point pos, Size s, Color c) {
		super(pos, s, c);
	}

	@Override
	public boolean isObstacle() {
		return false;
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
