package Model;

public abstract class GameObject {
	private int posX;
	private int posY;
	private int color;


	public GameObject(int X, int Y) {
		this.posX = X;
		this.posY = Y;
		this.color = 0;
	}

	public int getPosX() {
		return this.posX;
	}

	public int getPosY() {
		return this.posY;
	}
	
	public int getColor() {
		return color;
	}


	public boolean isAtPosition(int x, int y) {
		return this.posX == x && this.posY == y;
	}

	public abstract boolean isObstacle();
}