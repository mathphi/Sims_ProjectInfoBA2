package Product;

public class Game extends Product {
	private int moodAdd;
	private int energyNeed; //cost of energy to use
	public Game(){
		super(15, "Jeux", 2);		
		moodAdd = 10;
		energyNeed = 10;
	}
	public int getMoodAdd() {
		return moodAdd;
	}
	public int getEnergyNeed() {
		
		return energyNeed;
	}

}
