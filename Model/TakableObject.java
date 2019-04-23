package Model;

import Tools.Point;
import Tools.Size;

public abstract class TakableObject extends GameObject {
	int weight;
	int price;

	public TakableObject(Point pos, Size sz, String name, int price) {
		super(pos, sz);
		this.price = price;

	}

	public int getPrice() {
		return price;
	}
}
