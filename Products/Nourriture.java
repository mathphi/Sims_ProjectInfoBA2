package Products;

public class Nourriture extends Product {
	public Nourriture(String name, int price, int moodImpact, int energyImpact, int hungerImpact) {
		super(name, price);
		this.moodImpact = moodImpact;
		this.energyImpact = energyImpact;
		this.hungerImpact = hungerImpact;
	
	}

	

}