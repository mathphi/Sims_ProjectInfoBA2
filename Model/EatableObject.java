package Model;

import Tools.Point;
import Tools.Size;

public abstract class EatableObject extends TakableObject {
	protected float nutritionalValue;
	
	public EatableObject(Point pos, Size sz, String name, int price) {
		super(pos, sz, name, price);
		//TODO: random ?
		this.nutritionalValue = nutritionalValue;
	}
	public float getNutritionalValue() {
		return nutritionalValue;
	}
	
}
