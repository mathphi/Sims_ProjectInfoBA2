package Model;

import java.time.LocalDateTime;

public interface Worker {
	public void work(int energyImpact, int moodImpact, int salary, int duration);
	
	public boolean isWorking();
	
	public void resetLastWorkTime();
	
	public LocalDateTime getLastWorkTime();
}
