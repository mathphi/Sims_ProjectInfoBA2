package gameObject;

public class EatableObject extends TakableObject {
	protected float nutritionalValue;
	public EatableObject(int X, int Y, int weight, String name, int price) {
		super( X, Y, weight,  name,price);
		//TODO: random ?
		this.nutritionalValue = nutritionalValue;
	}
	public float getNutritionalValue() {
		return nutritionalValue;
	}
	
}
