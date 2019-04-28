package Model;

import java.awt.Color;

import Tools.Point;
import Tools.Size;

public abstract class TakableObject extends GameObject {
	private static final long serialVersionUID = -7682427739183023327L;
	
	int weight;
	int price;

	public TakableObject(Point pos, Size sz, String name, int price) {
		super(pos, sz, Color.YELLOW);
		this.price = price;

	}

	public int getPrice() {
		return price;
	}
}
