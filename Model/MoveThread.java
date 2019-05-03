package Model;

import Tools.Point;

public class MoveThread implements Runnable {
	private final int STEPS = 5;

	private boolean stopped = false;
	private boolean active = false;
	private Person p;
	private Point initPos = null;
	private Point deltaPos = null;
	private Point nextDeltaPos = null;

	public MoveThread(Person p) {
		this.p = p;
	}
	
	public void abort() {
		stopped = true;
	}
	
	public boolean isActive() {
		return active;
	}

	public void newMovement(Point delta) {
		if (deltaPos == null) {
			deltaPos = delta;
		}
		else if (nextDeltaPos == null) {
			nextDeltaPos = delta;
		}
	}
	
	@Override
	public void run() {
		while (!stopped) {
			if (deltaPos == null) {
				if (nextDeltaPos != null) {
					deltaPos = nextDeltaPos;
					nextDeltaPos = null;
				}
				else {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					continue;
				}
			}

			initPos = p.getPos();
			active = true;
			
			for (int i = 0 ; i < STEPS && !stopped ; i++) {
				// Save the start time
				long start_t = System.currentTimeMillis();
				
				// Add the deltaPos divided by the number of steps
				Point nextPos = p.getPos().add(deltaPos.multiply(1.0/STEPS));
				p.setPos(nextPos);
				p.refresh();
					
				try {
					// Compute the elapsed time to refresh,... and remove it from sleep time
					long dt = System.currentTimeMillis() - start_t;
					Thread.sleep((170 - dt) / STEPS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// Set an integer endPos to ensure the Person is aligned with the grid
			Point endPos = initPos.add(deltaPos);
			p.setPos(new Point(Math.round(endPos.getX()), Math.round(endPos.getY())));
			
			deltaPos = null;
			initPos = p.getPos();
			
			active = false;
		}
	}
}
