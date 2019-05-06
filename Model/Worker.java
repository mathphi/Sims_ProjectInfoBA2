package Model;

public interface Worker {
	public void work(int energyImpact, int moodImpact, int salary, int duration);
	
	public boolean isWorking();
}
