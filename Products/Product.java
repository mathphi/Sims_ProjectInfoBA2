package Products;

import javax.swing.Icon;

public abstract class Product {
	private int weight;
	private int price;
	private String name;
	
	protected int moodImpact = 0;

	public Product(int weight, String name,int price){
		this.weight = weight;
		this.name = name;
		this.price = price;		

	}

	public String getType() {
		return name;
	}

	public int getMoodImpact() {
		return moodImpact;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
