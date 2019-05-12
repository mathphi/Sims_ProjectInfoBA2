package Model;

import Tools.Point;
import Tools.Size;

public class Tree extends Vegetation {
	private static final long serialVersionUID = -7699679260544683609L;
	private static final Size SIZE = new Size(3, 2);
	private static final Size VIEW_SIZE = new Size(5, 8);
	
	public Tree(Point pos) {
		super(pos, SIZE, VIEW_SIZE);
	}

	@Override
	public GameObject clone() {
		return (GameObject) new Tree(getPos());
	}
}
