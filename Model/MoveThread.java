package Model;

import java.util.ArrayList;

import Tools.Point;

public class MoveThread implements Runnable {
	private final int STEPS = 5;

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
					Point initPos = p.getPos();
					Point deltaPos = itinerary.get(0);
					Point nextPos = initPos.add(deltaPos);
					
					if (targetPos != null && g.isTargetUnreachable(p, nextPos)) {
						// Recompute the itinerary if the next position is blocked.
						// This can happen if another object move also on the map.
						itinerary = (new AStar(g.getMapSize(), p, targetPos, g.getGameObjects())).getItinerary();
						continue;
					}
					
					p.rotate(deltaPos);
					
					for (int i = 0 ; i < STEPS ; i++) {
						// Move of deltaPos divided by the number of steps
						Point subPos = p.getPos().add(deltaPos.multiply(1.0/STEPS));
						p.setPos(subPos);
						p.refresh();
							
						// The speed varies with the energy of the player
						standby((int)(200 - 75 * p.getEnergy()) / STEPS );
					}
					
					// Set an integer endPos to ensure the Person is aligned with the grid
					p.setPos(new Point(Math.round(nextPos.getX()), Math.round(nextPos.getY())));
					p.refresh();
	
					itinerary.remove(0);
				}
				
				itinerary = null;
				targetPos = null;
			}
		}
	}
}
