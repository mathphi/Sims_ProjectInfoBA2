package Model;

public abstract class TakableObject extends GameObject {
	int weight;
	int price;

	public TakableObject(int X, int Y, int weight, String name, int price) {
		super(X, Y);
		this.weight = weight;
		this.price = price;

	}

	public int getPrice() {
		return price;
	}
}
