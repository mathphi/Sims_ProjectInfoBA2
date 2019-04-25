package Model;

import Tools.Point;

public class AStarThread implements Runnable{
	private Game g;
	private Person p;
	private Point pos;

	public AStarThread(Game g, Person p, Point pos) {
		this.g = g;
		this.p = p;
		this.pos = pos;
	}
	
	@Override
	public void run() {
		int direction = 0;
		synchronized(p) {
		while(direction != -1) {
			direction = (new AStar(g.getMapSize(), p.getPos(), pos, g.getGameObjects())).getNextStep();
			switch (direction) {
				case 0 : g.movePlayer(1,0); break;
				case 1 : g.movePlayer(0,-1); break;
				case 2 : g.movePlayer(-1,0); break;
				case 3 : g.movePlayer(0,1); break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	}
		

}
