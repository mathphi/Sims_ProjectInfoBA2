package Products;

public class Cloth extends Product implements GenderConstrained {
	private static final long serialVersionUID = 2384544436230416316L;
	
	private GenderConstraint genderConstrain;
	
	public Cloth(
			String name,
			String desc,
			int price,
			String imgID,
			int moodImpact,
			int otherImpressionImpact,
			GenderConstraint genderConstrain)
	{
		super(name, desc, price, imgID);
		
		this.moodImpact = moodImpact;
		this.otherImpressionImpact = otherImpressionImpact;
		this.genderConstrain = genderConstrain;
	}

	@Override
	public GenderConstraint getGenderConstraint() {
		return genderConstrain;
	}
}
