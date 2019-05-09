package Products;

public class Toy extends Product {
	private static final long serialVersionUID = 4102385827634988285L;

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