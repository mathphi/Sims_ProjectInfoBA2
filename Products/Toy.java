package Products;

public class Toy extends Product {
	public Toy(
			String name,
			String desc,
			int price,
			int moodImpact,
			int energyImpact)
	{
		super(name, desc, price);
		
		this.moodImpact = moodImpact;
		this.energyImpact = energyImpact;
	}
}