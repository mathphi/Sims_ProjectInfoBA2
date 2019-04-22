package gameObject;

public class GameObject { // was abstraite in exemple of code but function not -> normally can be done...
	protected int posX;
	protected int posY;


	public GameObject(int X, int Y) {
		this.posX = X;
		this.posY = Y;
	
	}

	public int getPosX() {
		return this.posX;
	}

	public int getPosY() {
		return this.posY;
	}


	public boolean isAtPosition(int x, int y) {
		return this.posX == x && this.posY == y;
	}

	public boolean isObstacle() {
		// was the only function abstract
		return true ; //was not 
	}
	
}