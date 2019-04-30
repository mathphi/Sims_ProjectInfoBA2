package Model;

public interface Directable {
    
	public enum Direction {
		EAST,
		NORTH,
		WEST,
		SOUTH
	}
    
    public Direction getDirection();

}
