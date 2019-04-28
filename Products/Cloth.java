package Products;

public class Cloth extends Product implements Wearable {
	private int otherVisionGain;
	public Cloth() {
		super(2, "Vêtement", 10); //weight, name, price
		otherVisionGain = 10;	
		
	}
	public int getOtherVisionGain(){
		return otherVisionGain;
	}
}
