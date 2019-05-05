package Products;

import javax.swing.Icon;

public abstract class Product {
	private int price;
	private String name;

	protected int moodImpact = 0;
	protected int energyImpact = 0;
	protected int hungerImpact = 0;
	protected int hygieneImpact = 0;
	protected int generalKnowledgeImpact = 0;
	protected int otherImpressionImpact = 0;

	public Product(String name, int price) {
		this.name = name;
		this.price = price;

	}

	public int getMoodImpact() {
		return moodImpact;
	}
	public int getEnergyImpact() {
		return energyImpact;
	}
	public int getHungerImpact() {
		return hungerImpact;
	}
	public int getHygieneImpact() {
		return hygieneImpact;
	}
	public int getGeneralKnowledgeImpact() {
		return generalKnowledgeImpact;
	}
	public int getOtherImpressionImpact() {
		return otherImpressionImpact;
	}
	

	public String getName() {
		return name;
	}

	public int getPrice() {

		return price;
	}


	
}
