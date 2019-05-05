package Products;

public class Toy extends Product {
	public Toy(String name, int price, int moodImpact, int energyImpact) {
		super(name, price);
		this.moodImpact = moodImpact;
		this.energyImpact = energyImpact;
	
	}



}