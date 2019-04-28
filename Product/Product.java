package Product;
import Model.GameObject;;

public class Product {
	private int weight;
	private int price;
	private String name;

	public Product(int weight, String name,int price){
		this.weight = weight;
		this.name = name;
		this.price = price;		

	}

	public String getType() {
		return name;
	}

}
