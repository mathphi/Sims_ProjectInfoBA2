package Products;

public class Food extends Product {
	private static final long serialVersionUID = 3798187371654542068L;

	public Food(
			String name,
			String desc,
			int price,
			String imgID,
			int moodImpact,
			int energyImpact,
			int hungerImpact)
	{
		super(name, desc, price, imgID);
		
		this.moodImpact = moodImpact;
		this.energyImpact = energyImpact;
		this.hungerImpact = hungerImpact;
	}
}