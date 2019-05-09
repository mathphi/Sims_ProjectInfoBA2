package Products;

public interface GenderConstrained {
	public enum GenderConstraint {
		Male,
		Female,
		Both
	}
	
	public GenderConstraint getGenderConstraint();
}
