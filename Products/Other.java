package Products;

public class Other extends Product implements AgeLimited {
	private static final long serialVersionUID = 2501725112673122860L;
	
	private int minimumAge = 0;
	
	public Other(
			String name,
			String desc,
			int price,
			int moodImpact,
			int energyImpact,
			int hungerImpact,
			int hygieneImpact,
			int generalKnowledgeImpact,
			int otherImpressionImpact,
			int minimumAge)
	{
		super(name, desc, price);
		
		this.moodImpact = moodImpact;
		this.energyImpact = energyImpact;
		this.hungerImpact = hungerImpact;
		this.hygieneImpact = hygieneImpact;
		this.generalKnowledgeImpact = generalKnowledgeImpact;
		this.otherImpressionImpact = otherImpressionImpact;
		this.minimumAge = minimumAge;
	}

	@Override
	public int getMinimumAge() {
		return minimumAge;
	}
}
