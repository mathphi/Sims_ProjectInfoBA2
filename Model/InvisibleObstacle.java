package Model;

import java.awt.Graphics;

import Tools.Point;
import Tools.Size;

public class InvisibleObstacle extends GameObject {
	private static final long serialVersionUID = 988157490322992149L;

	public InvisibleObstacle(Point pos, Size size) {
		super(pos, size, null);
	}

	@Override
	public boolean isObstacle() {
		return true;
	}
	
	@Override
	public GameObject clone() {
		return null;
	}
	
	@Override
	public void paint(Graphics g, int BLOC_SIZE) {
		return;
	}
}
