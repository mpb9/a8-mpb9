package a8;

public class ThreadSzn extends Thread {
	private boolean done;
	private LifeWidget widgy;

	public ThreadSzn(LifeWidget widgy) {
		this.widgy = widgy;
		done = !widgy.startButton.isSelected();

	}

	public void halt() {
		done = true;
	}

	public void run() {

		while (!done) {
			try {
				Thread.sleep(widgy.timeBetweenRuns);
			} catch (InterruptedException e) {
			}
			widgy.runOneStep();
			done = !widgy.startButton.isSelected();
		}
		widgy.runOneStep();
	}
}
