package Products;

public interface Wearable {
	//can be use every time the player want once buy 
	public int othersImpressionGain = 10;
	public default int getOthersImpressionGain() {
		return othersImpressionGain;
	}
}
