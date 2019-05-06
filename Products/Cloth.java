package Products;

public class Cloth extends Product implements Wearable{
	public Cloth(String name, int price, int otherImpressionImpact, int moodImpact) {
		super(name, price);
		this.otherImpressionImpact =otherImpressionImpact;
		this.moodImpact =moodImpact;
	}


}
