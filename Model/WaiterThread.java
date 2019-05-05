package Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaiterThread implements Runnable {
	private ActionListener endAction;
	private int sleepDelay;
	
	public WaiterThread(int sleepDelay, ActionListener a) {
		this.endAction = a;
		this.sleepDelay = sleepDelay;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(sleepDelay * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		endAction.actionPerformed(new ActionEvent(this, 0, "Timeout"));
	}
	
	public static void wait(int delay_seconds, ActionListener a) {
		Thread t = new Thread(new WaiterThread(delay_seconds, a));
		t.start();
	}
}
