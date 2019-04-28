package Model;

import java.util.Timer;
import java.util.TimerTask;

public class GameTime {	
	public static final int DAY_LEN = 24*60*60; // in seconds
	public static final int YEAR_LEN = 365*24*60*60; // in seconds
	private final int TIMER_PERIOD = 1000; // in microseconds
	
	// The scale factor between real time and virtual time speed (ie: one day is 1 seconds)
	private final double TIME_SCALE_FACTOR = DAY_LEN / 1;
	
	private long timeFromStart = 0;
	private boolean isActive = false;
	
	private Timer timer;
	private Game game;
	
	public GameTime(Game g, long t) {
		game = g;
		timeFromStart = t;
		timer = new Timer();
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!isActive)
					return;
				
				timeFromStart++;
				
				synchronized (game) {
					game.updateGame();
				}
			}
		}, 1000, TIMER_PERIOD/4);
	}
	
	public void start() {
		isActive = true;
	}
	
	public void stop() {
		if (isActive) {
			isActive = false;
		}
	}
	
	public void cancel() {
		timer.cancel();
	}
	
	public long getTimeFromStart() {
		return timeFromStart;
	}
	
	//TODO: return a time in days ?
	public long getVirtualTime() {
		// Return the scaled time for the game
		return (long)(timeFromStart * TIME_SCALE_FACTOR);
	}
	
	public int getDays() {
		long t = getVirtualTime();
	
		return (int)((t % YEAR_LEN) / DAY_LEN);
	}
	
	public int getYears() {
		long t = getVirtualTime();
	
		return (int)(t / YEAR_LEN);
	}
	
	public String getCurrentTimeString() {
		int y = getYears();
		int d = getDays();
		
		String strTime;
		
		if (y > 0) {
			strTime = String.format("%d années, %d jours", y, d);
		}
		else if (d == 0) {
			strTime = String.format("%d années", y);
		}
		else {
			strTime = String.format("%d jours", d);
		}
		
		return strTime;
	}
}