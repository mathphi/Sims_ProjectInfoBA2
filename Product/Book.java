package Product;

public class Book extends Product implements Usable{

	private int knwoledgeAdd;
	private int energyNeed; //cost of energy to use
	public Book(){
		super(10, "Livre", 5);		
		knwoledgeAdd = 5;
		energyNeed = 5;
	}
	public int getKnwoledgeAdd() {
		return knwoledgeAdd;
	}
	public int getEnergyNeed() {
		
		return energyNeed;
	}

}