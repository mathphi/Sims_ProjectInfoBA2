package Model;

import java.awt.Color;

import Tools.Point;
import Tools.Size;

public class WallBlock extends GameObject {

	public WallBlock(int x, int y) {
		this(new Point(x, y));
	}
	
	public WallBlock(Point pos) {
		super(pos, new Size(1,1), Color.DARK_GRAY);
	}

	public boolean isObstacle() {
		return true;
	}
	
	public void clickedEvent() {
		// A wall does nothing when clicked
	}
}
