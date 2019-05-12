package Model;

import Tools.Point;
import Tools.Random;
import Tools.Rect;
import Tools.Size;

/**
 * The NPC can move in a Rect of RECT_SIZE around they initial position
 */
public class NonPlayableThread implements Runnable {
	private static final Size RECT_SIZE = new Size(10, 10);
	private static final int MAX_ITINERARY_LENGTH = 15;
	private static final double SPEED_FACTOR = 0.6;
	
	private boolean stopped = false;
	
	private Game g;
	private Person p;
	private MoveThread mt;
	
	private Rect initialRect;

	public NonPlayableThread(Game g, Person p, MoveThread mt) {
		this.g = g;
		this.p = p;
		this.mt = mt;
		
		mt.setSpeedFactor(SPEED_FACTOR);
		
		Point rectCenter = p.getPos().remove(RECT_SIZE.getWidth() / 2.0, RECT_SIZE.getHeight() / 2.0);
		initialRect = new Rect(rectCenter, RECT_SIZE);
	}
	
	private void standby(int duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private boolean canMove() {
		boolean canMove = true;
		
		if (!g.isRunning() || p.isLocked()) {
			canMove = false;
		}
		
		for (GameObject o : p.getObjectsAround()) {
			if (o instanceof Person && ((Person) o).isPlayable()) {
				canMove = false;
			}
		}
		
		return canMove;
	}

	@Override
	public void run() {
		while (!stopped) {
			synchronized (mt) {
				// Wait for the previous itinerary finished
				while (mt.getItineraryLength() > 0 || !canMove()) {
					// Stay 5-10s on place before starting a new itinerary
					standby(5000 + Random.rangeInt(0, 5000));
				}
				
				// Get a random position in the initial rectangle
				Point randPos = Random.randomPosition(initialRect);
				
				// Use AStar to test if the itinerary is not too long
				AStar aStarTest = (new AStar(g.getMapSize(), p, randPos, g.getGameObjects()));

				// Recompute new random target if this one is too long
				if (aStarTest.getItinerary().size() > MAX_ITINERARY_LENGTH) {
					standby(100);
					continue;
				}
				
				// The target is ok, send the player there
				g.sendPlayer(p, randPos);
			}
		}
	}
}
