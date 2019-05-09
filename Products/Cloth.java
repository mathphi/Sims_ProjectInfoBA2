package Products;

public class Cloth extends Product implements GenderConstrained {
	private GenderConstraint genderConstrain;
	
	public Cloth(
			String name,
			String desc,
			int price,
			int moodImpact,
			int otherImpressionImpact,
			GenderConstraint genderConstrain)
	{
		super(name, desc, price);
		
		this.moodImpact = moodImpact;
		this.otherImpressionImpact = otherImpressionImpact;
		this.genderConstrain = genderConstrain;
	}

	@Override
	public GenderConstraint getGenderConstraint() {
		return genderConstrain;
	}
}
