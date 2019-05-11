package Model;

import java.util.ArrayList;

import Tools.Point;

public class MoveThread implements Runnable {
	private final int STEPS = 2;

	private boolean stopped = false;
	
	private Game g;
	private Person p;
	private Point targetPos = null;
	private ArrayList<Point> itinerary = null;

	public MoveThread(Game g, Person p) {
		this.g = g;
		this.p = p;
	}
	
	public void setTargetPosition(Point pos) {
		targetPos = pos;
		itinerary = (new AStar(g.getMapSize(), p, targetPos, g.getGameObjects())).getItinerary();
	}
	
	public void addMovement(Point movement) {
		// Don't add movement is AStar is running
		if (targetPos != null)
			return;
		
		if (itinerary == null) {
			itinerary = new ArrayList<Point>();
		}
		
		// Don't add more than 2 movements in the queue
		if (itinerary.size() < 2) {
			itinerary.add(movement);
		}
	}
	
	private void standby(int duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!stopped) {
			if (itinerary == null) {
				standby(100);
				continue;
			}
			
			synchronized (p) {
				while (itinerary.size() > 0 && !stopped) {
					synchronized (itinerary) {
						Point initPos = p.getPos();
						Point deltaPos = itinerary.get(0);
						Point nextPos = initPos.add(deltaPos);
						
						if (g.isTargetUnreachable(p, nextPos)) {
							if (targetPos != null) {
								// Recompute the itinerary if the next position is blocked.
								// This can happen if another object move also on the map.
								itinerary = (new AStar(g.getMapSize(), p, targetPos, g.getGameObjects())).getItinerary();
								continue;
							}
							else {
								// If we are not in itinerary mode -> don't go to the next point
								break;
							}
						}
						
						p.rotate(deltaPos);
						
						for (int i = 0 ; i < STEPS ; i++) {
							// Standby the personage moving when the game is paused
							while (!g.isRunning()) {
								standby(100);
							}
							
							// Move of deltaPos divided by the number of steps
							Point subPos = p.getPos().add(deltaPos.multiply(1.0/STEPS));
							p.setPos(subPos);
							p.incrementAnimIndex();
							p.refresh();
								
							// The speed varies with the energy of the player
							standby((int)(200 - 75.0 * p.getEnergy()) / STEPS );
						}
						
						// Set an integer endPos to ensure the Person is aligned with the grid
						p.setPos(new Point(Math.round(nextPos.getX()), Math.round(nextPos.getY())));
						p.refresh();
		
						itinerary.remove(0);
						
						// Cancel itinerary if the person is locked
						if (p.isLocked()) {
							itinerary = new ArrayList<Point>();
						}
					}
				}
				
				p.resetAnimIndex();
				p.refresh();
				
				itinerary = null;
				targetPos = null;
			}
		}
	}
}
