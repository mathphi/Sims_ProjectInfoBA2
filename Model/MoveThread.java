package Model;

import Tools.Point;

public class MoveThread implements Runnable {
	private final int STEPS = 10;
	
	private boolean active = true;
	private Person p;
	private Point deltaPos;
	private Point initPos;

	public MoveThread(Person p, Point deltaPos) {
		this.p = p;
		this.deltaPos = deltaPos;
		this.initPos = p.getPos();
	}
	
	public void abort() {
		active = false;
	}

	@Override
	public void run() {
		for (int i = 0 ; i < STEPS && active ; i++) {
			// Add the deltaPos divided by the number of steps
			Point nextPos = p.getPos().add(deltaPos.multiply(1.0/STEPS));
			p.setPos(nextPos);
			p.refresh();
				
			try {
				Thread.sleep(180 / STEPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Set an integer endPos to ensure the Person is aligned with the grid
		Point endPos = initPos.add(deltaPos);
		p.setPos(new Point(Math.round(endPos.getX()), Math.round(endPos.getY())));
		p.refresh();
	}
}
