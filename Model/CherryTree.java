package Model;

import Tools.Point;
import Tools.Size;

public class CherryTree extends Vegetation {
	private static final long serialVersionUID = -7699679260544683609L;
	private static final Size SIZE = new Size(3, 2);
	private static final Size VIEW_SIZE = new Size(7, 8);
	
	public CherryTree(Point pos) {
		super(pos, SIZE, VIEW_SIZE);
	}

	@Override
	public GameObject clone() {
		return (GameObject) new CherryTree(getPos());
	}
}
