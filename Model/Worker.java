package Model;

import java.time.LocalDateTime;

public interface Worker {	
	public void work();
	
	public boolean isWorking();
	
	public void resetLastWorkTime();
	
	public LocalDateTime getLastWorkTime();
}
