package Model;

import Tools.Point;
import Tools.Size;

public class Bush extends Vegetation {
	private static final long serialVersionUID = -7699679260544683609L;
	private static final Size SIZE = new Size(4, 3);
	private static final Size VIEW_SIZE = new Size(4, 4);
	
	public Bush(Point pos) {
		super(pos, SIZE, VIEW_SIZE);
	}

	@Override
	public GameObject clone() {
		return (GameObject) new Bush(getPos());
	}
}
