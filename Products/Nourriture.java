package Products;

public class Nourriture extends Product implements Usable{
	private int nutritionalValue;
	private int energyNeed; //cost of energy to use
	
	public Nourriture() {
		super(10, "Nourriture", 2); //weight, name, price
		nutritionalValue = 10;	
		energyNeed = 5;
		
		moodImpact = 5;//TODO to chznge !
	}

	public int getNutritionalValue() {
		return nutritionalValue;
	}
	public int getEnergyNeed() {
		
		return energyNeed;
	
	}
	
	
	
	

}
