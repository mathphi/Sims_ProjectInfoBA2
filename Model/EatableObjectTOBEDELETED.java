package Model;

import Tools.Point;
import Tools.Size;
//TODO to be deleted
public abstract class EatableObjectTOBEDELETED extends TakableObject {
	private static final long serialVersionUID = -7927255042490223537L;
	
	protected float nutritionalValue;
	
	public EatableObjectTOBEDELETED(Point pos, Size sz, String name, int price) {
		super(pos, sz, name, price);
		//TODO: random ?
		this.nutritionalValue = 10;
	}
	public float getNutritionalValue() {
		return nutritionalValue;
	}
	
}
