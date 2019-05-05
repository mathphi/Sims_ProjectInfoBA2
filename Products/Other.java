package Products;

public class Other extends Product {
	public Other(String name, int price, int moodImpact, int energyImpact, int hungerImpact, int hygieneImpact,
			int generalKnowledgeImpact, int otherImpressionImpact) {
		super(name, price);
		this.moodImpact = moodImpact;
		this.energyImpact = energyImpact;
		this.hungerImpact = hungerImpact;
		this.hygieneImpact = hygieneImpact;
		this.generalKnowledgeImpact = generalKnowledgeImpact;
		this.otherImpressionImpact = otherImpressionImpact;
	}

}
