package Model;

import Tools.Point;

public class MoveThread implements Runnable {
	private final int STEPS = 5;

	private boolean stopped = false;
	private boolean active = false;
	private Person p;
	private Point initPos;
	private Point deltaPos;
	private Point nextDeltaPos;

	public MoveThread(Person p, Point deltaPos) {
		this.p = p;
		this.deltaPos = deltaPos;
		this.initPos = p.getPos();
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
			
			active = true;
			
			for (int i = 0 ; i < STEPS && !stopped ; i++) {
				// Add the deltaPos divided by the number of steps
				Point nextPos = p.getPos().add(deltaPos.multiply(1.0/STEPS));
				p.setPos(nextPos);
				p.refresh();
					
				try {
					Thread.sleep(160 / STEPS);
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
