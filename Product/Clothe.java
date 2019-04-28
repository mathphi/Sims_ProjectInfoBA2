package Product;

public class Clothe extends Product {
	private int otherVisionGain;
	public Clothe() {
		super(2, "Vêtement", 10); //weight, name, price
		otherVisionGain = 10;	
		
	}
	public int getOtherVisionGain(){
		return otherVisionGain;
	}
}
